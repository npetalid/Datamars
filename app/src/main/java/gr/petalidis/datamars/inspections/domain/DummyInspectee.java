package gr.petalidis.datamars.inspections.domain;

public class DummyInspectee extends Inspectee {

    private static Inspectee inspectee = null;
    private DummyInspectee() {
        super("000000000", "Άλλος");
    }

    public static Inspectee getInstance()
    {
        if (inspectee == null) {
            inspectee = new DummyInspectee();
        }
        return inspectee;
    }
}
