package xyz.raymoore.hl7express;

public class HL7Encoding {
    public static HL7Encoding DEFAULT = new HL7Encoding();

    private final char fieldSeparator;
    private final char componentSeparator;
    private final char fieldRepSeparator;
    private final char escapeCharacter;
    private final char subcomponentSeparator;

    // ---

    public HL7Encoding() {
        this("|", "^~\\&");  // De facto standard in HL7
    }

    public HL7Encoding(String msh1, String msh2) {
        this.fieldSeparator = msh1.charAt(0);
        this.componentSeparator = msh2.charAt(0);
        this.fieldRepSeparator = msh2.charAt(1);
        this.escapeCharacter = msh2.charAt(2);
        this.subcomponentSeparator = msh2.charAt(3);
    }

    // ---

    public String escape(String value) {
        return value;  // TODO: Implement
    }

    public String unescape(String value) {
        return value;  // TODO: Implement
    }

    public char getFieldSeparator() {
        return fieldSeparator;
    }

    public char getComponentSeparator() {
        return componentSeparator;
    }

    public char getFieldRepSeparator() {
        return fieldRepSeparator;
    }

    public char getEscapeCharacter() {
        return escapeCharacter;
    }

    public char getSubcomponentSeparator() {
        return subcomponentSeparator;
    }

    @Override
    public String toString() {
        return String.format("%c%c%c%c%c", fieldSeparator, componentSeparator, fieldRepSeparator, escapeCharacter, subcomponentSeparator);
    }
}
