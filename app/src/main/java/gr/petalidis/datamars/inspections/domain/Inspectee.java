package gr.petalidis.datamars.inspections.domain;

public class Inspectee {

    private String tin  = "";
    private String name = "";

    public Inspectee(String tin, String name) {
        this.tin = tin;
        this.name = name;
    }

    public String getTin() {
        return tin;
    }

    public String getName() {
        return name;
    }

    public String toString()
    {
        return name;
    }
}
