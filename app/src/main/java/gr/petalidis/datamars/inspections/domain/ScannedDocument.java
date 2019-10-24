package gr.petalidis.datamars.inspections.domain;

import java.io.Serializable;
import java.util.UUID;

public class ScannedDocument implements Serializable {
    private UUID id = UUID.randomUUID();

    private UUID inspectionId;

    private String imagePath;

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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ScannedDocument(UUID inspectionId, String imagePath) {
        this.inspectionId = inspectionId;
        this.imagePath = imagePath;
    }
}
