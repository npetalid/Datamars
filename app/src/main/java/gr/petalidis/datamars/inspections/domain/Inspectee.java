package gr.petalidis.datamars.inspections.domain;

import java.io.Serializable;

public class Inspectee implements Serializable {

    private String tin  = "";
    private String name = "";
    private static Inspectee dummyInspectee = null;
    public Inspectee(String tin, String name) {
        this.tin = tin;
        this.name = name;
    }

    public static Inspectee getDummyInspectee()
    {

        if (dummyInspectee==null) {
            dummyInspectee = new Inspectee("000000000","Άλλος");
        }
        return dummyInspectee;
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

    public boolean isDummy()
    {
        return this.equals(Inspectee.getDummyInspectee());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Inspectee inspectee = (Inspectee) o;

        return getTin().equals(inspectee.getTin());
    }

    @Override
    public int hashCode() {
        return getTin().hashCode();
    }
}
