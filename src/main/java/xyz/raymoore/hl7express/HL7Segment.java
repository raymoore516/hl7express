package xyz.raymoore.hl7express;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class HL7Segment implements HL7Express<HL7Segment> {
    private final TreeMap<Integer, List<HL7Field>> fields = new TreeMap<>();  // Typically a list of one

    // ---

    public HL7Segment() {
    }

    public HL7Segment(String code) {
        this.fields.put(0, List.of(HL7Field.of(code)));
    }

    // ---

    public String getCode() {
        return getField(0) == null ? null : getField(0).getValue();
    }

    public HL7Field getField(int field) {
        if (fields.get(field) == null) {
            return null;
        }

        return fields.get(field).get(0);  // Most fields are non-repeating
    }

    public void setField(int field, String value) {
        setField(field, HL7Field.of(value));
    }

    public void setField(int field, HL7Field... data) {
        setField(field, Arrays.asList(data));
    }

    public void setField(int field, List<HL7Field> data) {
        this.fields.put(field, data);
    }

    // ---

    @Override
    public HL7Segment fromString(String str, HL7Encoding ec) {
        StringBuilder sb = new StringBuilder();
        String delimiter = Character.toString(ec.getRepetitionSeparator());

        int num = -1;  // Increment to index 0 (not 1) for the segment code
        for (char c: str.toCharArray()) {
            if (c == ec.getFieldSeparator()) {
                // Special logic for MSH-1 and MSH-2
                if (Code.MSH.equals(getCode()) && num == 0) {
                    fields.put(++num, List.of(HL7Field.of(ec.getFieldSeparator())));
                    fields.put(++num, List.of(HL7Field.of(sb.toString())));
                    sb.setLength(0);
                    continue;
                }
                if (sb.length() == 0) {
                    ++num;
                    continue;  // Do not create empty HL7Field
                }
                List<HL7Field> repetitions = Arrays.stream(sb.toString().split(delimiter))
                        .map(substr -> new HL7Field().fromString(substr, ec))
                        .toList();
                fields.put(++num, repetitions);
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        if (sb.length() > 0) {
            List<HL7Field> repetitions = Arrays.stream(sb.toString().split(delimiter))
                    .map(substr -> new HL7Field().fromString(substr, ec))
                    .toList();
            fields.put(++num, repetitions);
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

        for (int n = 0; n <= fields.lastKey(); n++) {
            // Special logic for MSH-1 and MSH-2
            if (Code.MSH.equals(getCode()) && n > 0 && n < 3) {
                sb.append(ec);
                n = 2;
                continue;
            }
            // Append separator for all fields
            if (n > 0) {
                sb.append(ec.getFieldSeparator());
            }
            // Append field if exists
            if (fields.get(n) == null) {
                continue;
            }
            for (int i = 0; i < fields.get(n).size(); i++) {
                if (i > 0) {
                    sb.append(ec.getRepetitionSeparator());
                }
                sb.append(fields.get(n).get(i).toString(ec));
            }
        }

        return sb.toString();
    }

    public interface Code {
        String MSH = "MSH";  // TODO: Add all standard HL7 segment codes
    }
}
