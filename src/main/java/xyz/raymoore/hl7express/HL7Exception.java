package xyz.raymoore.hl7express;

public class HL7Exception extends RuntimeException {
    public HL7Exception(String message) {
        super(message);
    }

    public HL7Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
