package gr.petalidis.datamars.inspections.domain;

import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.rsglibrary.Rsg;

public class Inspection implements Serializable {

    private final String KID_ANIMAL = "Ερίφιο";
    private final String GOAT_ANIMAL ="Γίδα";
    private final String LAMB_ANIMAL ="Αρνί";
    private final String SHEEP_ANIMAL ="Προβατίνα";
    private final String HEGOAT_ANIMAL ="Τράγος";
    private final String RAM_ANIMAL = "Κριάρι";

    private UUID id = UUID.randomUUID();

    private Date date = new Date();

    private String producer1Tin = "";
    private String producer1Name = "";
    private String producer2Tin = "";
    private String producer2Name = "";
    private String producer3Tin = "";
    private String producer3Name = "";
    private String producer4Tin = "";
    private String producer4Name = "";

    private List<Entry> entries = new ArrayList<>();

    public Inspection(Date date, String producer1Tin, String producer1Name) {
        this.date = date;
        this.producer1Tin = producer1Tin;
        this.producer1Name = producer1Name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProducer1Tin() {
        return producer1Tin;
    }

    public void setProducer1Tin(String producer1Tin) {
        this.producer1Tin = producer1Tin;
    }

    public String getProducer1Name() {
        return producer1Name;
    }

    public void setProducer1Name(String producer1Name) {
        this.producer1Name = producer1Name;
    }

    public String getProducer2Tin() {
        return producer2Tin;
    }

    public void setProducer2Tin(String producer2Tin) {
        this.producer2Tin = producer2Tin;
    }

    public String getProducer2Name() {
        return producer2Name;
    }

    public void setProducer2Name(String producer2Name) {
        this.producer2Name = producer2Name;
    }

    public String getProducer3Tin() {
        return producer3Tin;
    }

    public void setProducer3Tin(String producer3Tin) {
        this.producer3Tin = producer3Tin;
    }

    public String getProducer3Name() {
        return producer3Name;
    }

    public void setProducer3Name(String producer3Name) {
        this.producer3Name = producer3Name;
    }

    public String getProducer4Tin() {
        return producer4Tin;
    }

    public void setProducer4Tin(String producer4Tin) {
        this.producer4Tin = producer4Tin;
    }

    public String getProducer4Name() {
        return producer4Name;
    }

    public void setProducer4Name(String producer4Name) {
        this.producer4Name = producer4Name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public List<Entry> getEntries() {
        return entries;
    }

    public void initEntries(Set<Rsg> rsgs)
    {

        this.entries = rsgs
                .stream()
                .map(x->new Entry(this,
                        x.getCountryCode(),
                        x.getIdentificationCode(),
                        x.getDate())).collect(Collectors.toList());
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Entry> getValidEntries()
    {
        return this.entries.stream().filter(x->!x.getProducer().isEmpty() && !x.getProducer().equals("Άλλος") ).collect(Collectors.toList());
    }

    public List<Inspectee> getProducers()
    {
        List<Inspectee> names = new ArrayList<>();
        names.add(new Inspectee(producer1Tin,producer1Name));
        if (!producer2Name.trim().isEmpty()) names.add(new Inspectee(producer2Tin,producer2Name));
        if (!producer3Name.trim().isEmpty()) names.add(new Inspectee(producer3Tin,producer3Name));
        if (!producer4Name.trim().isEmpty()) names.add(new Inspectee(producer4Tin,producer4Name));
        names.add(Inspectee.getDummyInspectee());
        return names;
    }

    public List<String> getProducerTins()
    {
        List<String> names = new ArrayList<>();
        names.add(producer1Tin);
        if (!producer2Tin.trim().isEmpty()) names.add(producer2Tin);
        if (!producer3Tin.trim().isEmpty()) names.add(producer3Tin);
        if (!producer4Tin.trim().isEmpty()) names.add(producer4Tin);
        names.add(Inspectee.getDummyInspectee().getName());
        return names;
    }

    public long getValidEntriesCount() {
        return getEntries().stream().filter(x->!x.isDummy()).count();
    }

    public long getInRegisterCount() {
        return getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true).count();
    }

    public long getLambCount() {
        return getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(LAMB_ANIMAL)).count();
    }
    public long getSheepCount(){
        return getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(SHEEP_ANIMAL)).count();
    }
    public long getKidCount() {
        return getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(KID_ANIMAL)).count();
    }
    public long getGoatCount() {
        return getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(GOAT_ANIMAL)).count();
    }
    public long getRamCount() {
        return getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(RAM_ANIMAL)).count();
    }
    public long getHeGoatCount() {
        return getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(HEGOAT_ANIMAL)).count();
    }


}
