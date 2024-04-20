package xyz.raymoore.hl7express;

import java.util.ArrayList;
import java.util.List;

public class HL7Message implements HL7Express<HL7Message> {
    private final List<HL7Segment> segments = new ArrayList<>();

    // ---

    public static Builder builder() {
        return new Builder();
    }

    public void addSegment(HL7Segment segment) {
        this.segments.add(segment);
    }

    public void addSegments(List<HL7Segment> segments) {
        this.segments.addAll(segments);
    }

    public HL7Segment getSegment(String code) {
        for (HL7Segment segment : segments) {
            if (code.equals(segment.getCode())) {
                return segment;
            }
        }

        return null;
    }

    public List<HL7Segment> getSegments() {
        return segments;
    }

    public List<HL7Segment> getSegments(String code) {
        return segments.stream().filter(segment -> code.equals(segment.getCode())).toList();
    }

    public List<HL7Segment> getSegmentNotes(HL7Segment parent) {
        List<HL7Segment> notes = new ArrayList<>();

        int start = segments.indexOf(parent);
        if (start < 0) {
            return notes;
        }

        for (int i = start + 1; i < segments.size(); i++) {
            HL7Segment child = segments.get(i);
            if (HL7Segment.Code.NTE.equals(child.getCode())) {
                notes.add(child);
            } else {
                return notes;
            }
        }

        return notes;
    }

    // ---

    public HL7Message fromString(String str) throws HL7Exception {
        String code = str.substring(0, 3);
        if (!HL7Segment.Code.MSH.equals(code)) {
            throw new HL7Exception("Message must begin with MSH header segment");
        }
        String msh1 = str.substring(3, 4);
        String msh2 = str.substring(4, 8);

        return fromString(str, new HL7Encoding(msh1, msh2));
    }

    @Override
    public HL7Message fromString(String str, HL7Encoding ec) {
        for (String substr : str.split("\\R")) {
            segments.add(new HL7Segment().fromString(substr, ec));
        }

        return this;
    }

    @Override
    public String toString() {
        return toString(HL7Encoding.DEFAULT);
    }

    @Override
    public String toString(HL7Encoding ec) {
        StringBuilder sb = new StringBuilder();

        for (HL7Segment segment : segments) {
            sb.append(segment.toString(ec));
            sb.append('\n');
        }

        return sb.toString();
    }

    // ---

    public static class Builder {
        private final HL7Message message = new HL7Message();

        public Builder segment(HL7Segment segment) {
            this.message.addSegment(segment);
            return this;
        }

        public HL7Message build() {
            return message;
        }
    }
}
