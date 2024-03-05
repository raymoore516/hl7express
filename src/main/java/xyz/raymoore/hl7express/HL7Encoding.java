package xyz.raymoore.hl7express;

public class HL7Encoding {
    public static HL7Encoding DEFAULT = new HL7Encoding();

    // https://docs.intersystems.com/irisforhealthlatest/csp/docbook/DocBook.UI.Page.cls?KEY=EHL72_escape_sequences
    public final CharSequence FIELD_ESCAPE_SEQ = "\\F\\";
    public final CharSequence COMPONENT_ESCAPE_SEQ = "\\S\\";
    public final CharSequence REPETITION_ESCAPE_SEQ = "\\R\\";
    public final CharSequence ESCAPE_ESCAPE_SEQ = "\\E\\";
    public final CharSequence SUBCOMPONENT_ESCAPE_SEQ = "\\T\\";
    public final CharSequence LINE_BREAK_ESCAPE_SEQ = "\\.br\\";
    public final CharSequence LINE_FEED_ESCAPE_SEQ = "\\X0A\\";
    public final CharSequence CARRIAGE_RETURN_ESCAPE_SEQ = "\\X0D\\";

    private final char fieldSeparator;
    private final char componentSeparator;
    private final char repetitionSeparator;
    private final char escapeCharacter;
    private final char subcomponentSeparator;

    // ---

    public HL7Encoding() {
        this("|", "^~\\&");  // De facto standard in HL7
    }

    public HL7Encoding(String msh1, String msh2) {
        this.fieldSeparator = msh1.charAt(0);
        this.componentSeparator = msh2.charAt(0);
        this.repetitionSeparator = msh2.charAt(1);
        this.escapeCharacter = msh2.charAt(2);
        this.subcomponentSeparator = msh2.charAt(3);
    }

    // ---

    public String escape(String str) {
        StringBuilder sb = new StringBuilder();

        if (str == null || str.isEmpty()) {
            return "";
        }

        for (String substr : str.split("\\R")) {
            if (sb.length() > 0) {
                sb.append(LINE_BREAK_ESCAPE_SEQ);
            }
            for (char c : substr.toCharArray()) {
                if (c == fieldSeparator) {
                    sb.append(FIELD_ESCAPE_SEQ);
                } else if (c == componentSeparator) {
                    sb.append(COMPONENT_ESCAPE_SEQ);
                } else if (c == repetitionSeparator) {
                    sb.append(REPETITION_ESCAPE_SEQ);
                } else if (c == escapeCharacter) {
                    sb.append(ESCAPE_ESCAPE_SEQ);
                } else if (c == subcomponentSeparator) {
                    sb.append(SUBCOMPONENT_ESCAPE_SEQ);
                } else {
                    sb.append(c);
                }
            }
        }

        return sb.toString();
    }

    public String unescape(String str) throws HL7Exception {
        StringBuilder sb = new StringBuilder();
        StringBuilder eb = new StringBuilder();
        boolean isEscaped = false;

        for (char c : str.toCharArray()) {
            if (c == escapeCharacter) {
                isEscaped = !isEscaped;
                eb.append(c);
                if (!isEscaped) {
                    CharSequence seq = eb.toString();
                    eb.setLength(0);  // Reset escape sequence builder
                    if (FIELD_ESCAPE_SEQ.equals(seq)) {
                        sb.append(fieldSeparator);
                    } else if (COMPONENT_ESCAPE_SEQ.equals(seq)) {
                        sb.append(componentSeparator);
                    } else if (REPETITION_ESCAPE_SEQ.equals(seq)) {
                        sb.append(repetitionSeparator);
                    } else if (ESCAPE_ESCAPE_SEQ.equals(seq)) {
                        sb.append(escapeCharacter);
                    } else if (SUBCOMPONENT_ESCAPE_SEQ.equals(seq)) {
                        sb.append(subcomponentSeparator);
                    } else if (LINE_BREAK_ESCAPE_SEQ.equals(seq)) {
                        sb.append('\n');
                    } else if (LINE_FEED_ESCAPE_SEQ.equals(seq)) {
                        sb.append('\n');
                    } else if (CARRIAGE_RETURN_ESCAPE_SEQ.equals(seq)) {
                        sb.append('\r');
                    } else {
                        throw new HL7Exception(String.format("Unrecognized escape sequence [seq: %s]", seq));
                    }
                }
            } else if (isEscaped) {
                eb.append(c);
            } else {
                sb.append(c);  // This is the default scenario with no escape considerations
            }
        }

        return sb.toString();
    }

    public char getFieldSeparator() {
        return fieldSeparator;
    }

    public char getComponentSeparator() {
        return componentSeparator;
    }

    public char getRepetitionSeparator() {
        return repetitionSeparator;
    }

    public char getEscapeCharacter() {
        return escapeCharacter;
    }

    public char getSubcomponentSeparator() {
        return subcomponentSeparator;
    }

    @Override
    public String toString() {
        return String.format("%c%c%c%c%c", fieldSeparator, componentSeparator, repetitionSeparator, escapeCharacter, subcomponentSeparator);
    }
}
