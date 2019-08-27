package gr.petalidis.datamars.inspections.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class OtherEntry implements Serializable {
    private UUID id = UUID.randomUUID();

    private UUID inspectionId;

    private OtherEntryType entryType = OtherEntryType.CONVENTIONAL;

    private Inspectee inspectee = new Inspectee("","");
    private String animal = "";
    private int count;

    public OtherEntry() {
    }

    public OtherEntry(Inspectee producer, String animal, int count, OtherEntryType type, UUID inspectionId) {
        this.inspectee = producer;
        this.animal = animal;
        this.count = count;
        this.inspectionId = inspectionId;
        this.entryType = type;
    }

    public Inspectee getInspectee() {
        return inspectee;
    }

    public void setInspectee(Inspectee inspectee) {
        this.inspectee = inspectee;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(UUID inspectionId) {
        this.inspectionId = inspectionId;
    }

    public OtherEntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(OtherEntryType entryType) {
        this.entryType = entryType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OtherEntry that = (OtherEntry) o;
        return inspectee.equals(that.inspectee) &&
                animal.equals(that.animal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inspectee, animal);
    }
}
