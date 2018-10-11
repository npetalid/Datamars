package gr.petalidis.datamars.inspections.dto;

public class InspectionDateProducer {
    private String id;
    private String inspectionDate = "";
    private String mainProducer = "";

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
}
