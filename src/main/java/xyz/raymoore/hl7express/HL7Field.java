package xyz.raymoore.hl7express;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class HL7Field implements HL7Express<HL7Field> {
    private final TreeMap<Integer, HL7Component> components = new TreeMap<>();

    // ---

    public HL7Field() {
    }

    public HL7Field(HL7Component data) {
        this();
        this.set(1, data);
    }

    // ---

    public HL7Component get(int component) {
        return components.get(component);  // Most components are simple values
    }

    public void set(int component, HL7Component data) {
        this.components.put(component, data);
    }

    // ---

    public List<String> getSubcomponents(int component) {
        if (component < 1) {
            throw new IndexOutOfBoundsException("Component index must be greater than zero");
        }

        return components.get(component).getPieces();
    }

    public String getValue() {
        return getValue(1, 1);
    }

    public String getValue(int component) {
        return getValue(component, 1);
    }

    public String getValue(int component, int subcomponent) {
        if (component < 1) {
            throw new IndexOutOfBoundsException("Component index must be greater than zero");
        } else if (subcomponent < 1) {
            throw new IndexOutOfBoundsException("Subcomponent index must be greater than zero");
        }

        HL7Component data = components.get(component);
        return data == null ? null : data.getPiece(subcomponent);
    }

    public void setValue(int component, String value) {
        setValues(component, value);
    }

    public void setValues(int component, String... values) {
        this.components.put(component, new HL7Component(values));
    }

    // ---

    /**
     * This section is used to construct fields of simple HL7 data types (ST, NM, etc.)
     * These will never have more than one component value (i.e., always one subcomponent)
     */

    public static HL7Field of(String component) {
        return new HL7Field(new HL7Component(component));
    }

    public static HL7Field of(String... components) {
        return of(Arrays.asList(components));
    }

    public static HL7Field of(List<String> components) {
        HL7Field field = new HL7Field();

        for (int i = 0; i < components.size(); i++) {
            field.set(i + 1, new HL7Component(components.get(i)));
        }

        return field;
    }

    // ---

    public static HL7Field of(char value) {
        return of(String.valueOf(value));
    }

    public static HL7Field of(int value) {
        return of(String.valueOf(value));
    }

    public static HL7Field of(long value) {
        return of(String.valueOf(value));
    }

    // ---

    @Override
    public HL7Field fromString(String str, HL7Encoding ec) {
        StringBuilder sb = new StringBuilder();

        int num = 0;
        for (char c: str.toCharArray()) {
            if (c == ec.getComponentSeparator()) {
                if (sb.length() == 0) {
                    ++num;
                    continue;  // Do not create empty HL7Component
                }
                components.put(++num, new HL7Component().fromString(sb.toString(), ec));
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        if (sb.length() > 0) {
            components.put(++num, new HL7Component().fromString(sb.toString(), ec));
        }

        return this;
    }

    @Override
    public String toString() {
        return toString(HL7Encoding.DEFAULT);
    }

    @Override
    public String toString(HL7Encoding ec) {
        if (components.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (int n = 1; n <= components.lastKey(); n++) {
            // Append separator for all but first component
            if (n > 1) {
                sb.append(ec.getComponentSeparator());
            }
            // Append component if exists
            if (components.get(n) == null) {
                continue;
            }
            sb.append(components.get(n).toString(ec));
        }

        return sb.toString();
    }
}
