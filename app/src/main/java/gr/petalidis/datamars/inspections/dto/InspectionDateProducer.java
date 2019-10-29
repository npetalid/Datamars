package gr.petalidis.datamars.inspections.dto;

import androidx.annotation.NonNull;

public class InspectionDateProducer {
    private final String id;
    private String inspectionDate;
    private String mainProducer;

    public InspectionDateProducer(String uuid, String inspectionDate, String mainProducer)
    {
        this.id = uuid;
        this.inspectionDate = inspectionDate;
        this.mainProducer = mainProducer;
    }

    public String getInspectionDate() {
        return inspectionDate;
    }

    public String getMainProducer() {
        return mainProducer;
    }

    public String getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return mainProducer;
    }
}
