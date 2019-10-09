package gr.petalidis.datamars.inspections.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Entry implements Serializable {

    private final static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    private UUID id = UUID.randomUUID();

    private UUID inspectionId;

    private String country = "";
    private String tag = "";
    private Date tagDate = new Date();
    private boolean isInRegister = true;
    private String producer = "";
    private String producerTin = "";
    private String animalType = AnimalType.SHEEP_ANIMAL.title;
    private String animalGenre = AnimalGenre.NONE.getName();
    private CommentType comment = CommentType.EMPTY;
    public Entry()
    {

    }
    public Entry(Inspection inspection, String country, String tag, Date date) {
        this.tag = tag;
        this.country = country;
        this.tagDate = date;
        this.isInRegister = true;
        this.inspectionId = inspection.getId();
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

    public String getTag() {
        return tag;
    }

    public String getOwner() {
        return tag.substring(4,8);
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getTagDate() {
        return tagDate;
    }

    public void setTagDate(Date tagDate) {
        this.tagDate = tagDate;
    }

    public boolean isInRegister() {
        return isInRegister;
    }

    public void setInRegister(boolean inRegister) {
        isInRegister = inRegister;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public void setProducerTin(String producerTin)
    {
        this.producerTin = producerTin==null? "": producerTin.trim();
    }
    public String getProducerTin() {
        return producerTin;
    }

    public CommentType getComment() {
        return comment;
    }

    public void setComment(CommentType comment) {
        this.comment = comment;
    }

    public boolean isDummy()
    {
        return this.getProducerTin().equals(Inspectee.getDummyInspectee().getTin());
    }

    public String getAnimalGenre() {
        return animalGenre;
    }

    public void setAnimalGenre(String animalGenre) {
        this.animalGenre = animalGenre;
    }

    @Override
    public String toString()
    {
        return country+","+tag+","+format.format(tagDate)+","+(isInRegister==true?"ΝΑΙ":"ΟΧΙ")+","+producer+","+producerTin+","+animalType+","+animalGenre+","+ comment.title;
    }
}
