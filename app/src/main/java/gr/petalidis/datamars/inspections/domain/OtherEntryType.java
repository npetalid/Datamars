package gr.petalidis.datamars.inspections.domain;

import java.io.Serializable;

public enum OtherEntryType implements Serializable {
    CONVENTIONAL("CONVENTIONAL"),
    SINGLE("SINGLE"),
    ILLEGAL("ILLEGAL"),
    NO_EARRING("NO_EARRING"),
    OUT_OF_REGISTRY("OUT_OF_REGISTRY");
    private final String name;

    OtherEntryType(String name) {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
