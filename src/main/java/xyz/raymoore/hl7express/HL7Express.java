package xyz.raymoore.hl7express;

public interface HL7Express<T> {
    T fromString(String str, HL7Encoding ec);
    String toString(HL7Encoding ec);}
