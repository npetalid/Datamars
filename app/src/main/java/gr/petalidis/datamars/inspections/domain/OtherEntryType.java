package gr.petalidis.datamars.inspections.domain;

import java.io.Serializable;

public enum OtherEntryType implements Serializable {
    CONVENTIONAL("CONVENTIONAL"),
    NO_EARRING("NO_EARRING");

    private String name;

    OtherEntryType(String name) {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
