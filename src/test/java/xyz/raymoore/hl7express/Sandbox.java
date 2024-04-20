package xyz.raymoore.hl7express;

public class Sandbox {
    public static void main(String[] args) {
        test();

//        HL7Segment msh = new HL7Segment(HL7Segment.Code.MSH);
//        msh.setField(3, "AcmeHealth");
//        msh.setField(4, "FakeEMR");
//        System.out.println(msh);
//
//        HL7Segment msh2 = new HL7Segment("MSH").fromString(msh.toString(), HL7Encoding.DEFAULT);
//        System.out.println(msh2);
//
//        HL7Segment msh3 = new HL7Segment("MSH").fromString(msh2.toString(), HL7Encoding.DEFAULT);
//        System.out.println(msh3);
    }

    private static void test() {
        HL7Component c1 = new HL7Component("ABC", "XYZ");
        System.out.println(c1);

        String text1 = "ABC&XYZ";
        HL7Component c2 = new HL7Component().fromString(text1, HL7Encoding.DEFAULT);
        System.out.println(c2);

        HL7Field f1 = new HL7Field();
        f1.setValues(1, "MOORE");
        f1.setValues(2, "RAYMOND");
        f1.setValues(3, "EVERS");
        System.out.println(f1);

        String text2 = "MOORE^RAYMOND^EVERS";
        HL7Field f2 = new HL7Field().fromString(text2, HL7Encoding.DEFAULT);
        System.out.println(f2);

        HL7Segment msh = new HL7Segment("MSH");
        msh.setField(3, "AcmeHealth");
        msh.setField(4, "FakeEMR");
        msh.setField(10, "TRACE00001234");

        HL7Segment pid = new HL7Segment("PID");
        pid.setField(1, HL7Field.of(1));
        pid.setField(3, HL7Field.of("1234","","","ACMEMRN"), HL7Field.of("ABC123","","","MOCKMRN"));
        pid.setField(5, HL7Field.of("MOORE", "RAYMOND", "EVERS"));
        System.out.println(pid);

        HL7Segment nte = new HL7Segment("NTE");
        nte.setField(1, HL7Field.of(1));
        nte.setField(3, HL7Field.of("Ben & Jerry's |ce Cream ^_^ \\|/"));

        HL7Message m1 = new HL7Message();
        m1.addSegment(msh);
        m1.addSegment(pid);
        m1.addSegment(nte);
        System.out.println(m1);

        HL7Message m2 = new HL7Message().fromString(m1.toString());
        System.out.println(m2);

        // @formatter:off
        HL7Message m3 = HL7Message.builder()
                .segment(HL7Segment.builder("MSH")
                        .field(3, "AcmeHealth")
                        .field(4, "FakeEMR")
                        .field(10, "TRACE00001234")
                        .build())
                .segment(HL7Segment.builder("PID")
                        .field(1, "1")
                        .field(3,
                                HL7Field.builder()
                                        .component(1, "1234")
                                        .component(4, "ACMEMRN")
                                        .build(),
                                HL7Field.builder()
                                        .component(1, "ABC123")
                                        .component(4, "MOCKMRN")
                                        .build())
                        .field(5, HL7Field.builder()
                                .component(1, "MOORE")
                                .component(2, "RAYMOND")
                                .component(3, "EVERS")
                                .build())
                        .build())
                .segment(HL7Segment.builder("NTE")
                        .field(1, "1")
                        .field(3, "Ben & Jerry's |ce Cream ^_^ \\|/")
                        .build())
                .segment(HL7Segment.builder("NTE")
                        .field(1, "2")
                        .field(3, "So yummy! Am I right?!")
                        .build())
                .segment(HL7Segment.builder("ORC")
                        .field(1, "RE")
                        .build())
                .segment(HL7Segment.builder("OBR")
                        .field(1, "1")
                        .field(2, "XYZ123")
                        .build())
                .segment(HL7Segment.builder("NTE")
                        .field(1, "1")
                        .field(3, "ATTENTION: This is fake.")
                        .build())
                .segment(HL7Segment.builder("NTE")
                        .field(1, "2")
                        .field(3, "")
                        .build())
                .segment(HL7Segment.builder("NTE")
                        .field(1, "3")
                        .field(3, "CONCLUSION: Don't try to file this as a result.")
                        .build())
                .build();
        // @formatter:on

        System.out.println(m3);
    }
}
