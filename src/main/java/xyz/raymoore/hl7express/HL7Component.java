package xyz.raymoore.hl7express;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HL7Component implements HL7Express<HL7Component> {
    private final List<String> subcomponents;  // Typically a list of one

    // ---

    public HL7Component() {
        this(new ArrayList<>());
    }

    public HL7Component(String... subcomponents) {
        this(Arrays.asList(subcomponents));
    }

    public HL7Component(List<String> subcomponents) {
        this.subcomponents = subcomponents;
    }

    // ---

    public String getFirst() {
        return getPiece(1);  // Most components are simple values
    }

    public String getPiece(int subcomponent) {
        if (subcomponent < 1) {
            throw new IndexOutOfBoundsException("Subcomponent index must be greater than zero");
        }
        return subcomponents.get(subcomponent - 1);
    }

    public List<String> getPieces() {
        return subcomponents;
    }

    // ---

    @Override
    public HL7Component fromString(String str, HL7Encoding ec) {
        StringBuilder sb = new StringBuilder();

        for (char c : str.toCharArray()) {
            if (c == ec.getSubcomponentSeparator()) {
                // Add subcomponent and reset builder
                subcomponents.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        if (sb.length() > 0) {
            subcomponents.add(sb.toString());
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

        for (int i = 0; i < subcomponents.size(); i++) {
            // Append separator if multiple subcomponents
            if (i > 0) {
                sb.append(ec.getSubcomponentSeparator());
            }
            sb.append(subcomponents.get(i));
        }

        return sb.toString();
    }
}
