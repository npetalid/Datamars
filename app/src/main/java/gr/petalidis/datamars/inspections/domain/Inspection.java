package gr.petalidis.datamars.inspections.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import gr.petalidis.datamars.inspections.dto.ThumbnailDto;
import gr.petalidis.datamars.rsglibrary.Rsg;

public class Inspection implements Serializable {
    private final static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

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

    private Set<OtherEntry> conventionalTotal = new HashSet<>();
    private Set<OtherEntry> conventionalInRegister = new HashSet<>();

    private List<ScannedDocument> scannedDocuments = new ArrayList<>();

    private double latitude = 0.0f;
    private double longitude = 0.0f;

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
    public List<Entry> getEntriesFor(String tin) {
        return entries.stream().filter(x->x.getProducerTin().equals(tin)).collect(Collectors.toList());
    }
    public List<ScannedDocument> getScannedDocuments() {
        return scannedDocuments;
    }

    public void setScannedDocuments(List<ScannedDocument> scannedDocuments) {
        this.scannedDocuments = scannedDocuments;
    }

    public void initScannedDocuments(List<ThumbnailDto> thumbnailDtos) {
        this.scannedDocuments = thumbnailDtos.stream().map(x-> new ScannedDocument(this.id,x.getImagePath())).collect(Collectors.toList());
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
    public void associateEntries(String tagPart, String tin, String name)
    {

        this.entries.stream().filter(x->x.getTag().substring(4,8).equals(tagPart)).forEach(x->{
            x.setProducer(name);
            x.setProducerTin(tin);
        });
    }
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Entry> getValidEntries()
    {
        return this.entries.stream()
                .filter(x->!x.getProducer().isEmpty()
                        && !x.getProducer().equals("Άλλος") ).collect(Collectors.toList());
    }

    public Set<OtherEntry> getConventionalTotal() {
        return conventionalTotal;
    }

    public void setConventionalTotal(Set<OtherEntry> conventionalTotal) {
        this.conventionalTotal = conventionalTotal;
    }

    public Set<OtherEntry> getConventionalInRegister() {
        return conventionalInRegister;
    }

    public void setConventionalTotalFor(Inspectee inspectee, String animal, int number) {
        conventionalTotal.remove(new OtherEntry(inspectee,animal,number, OtherEntryType.CONVENTIONAL,id));
        conventionalTotal.add(new OtherEntry(inspectee,animal,number, OtherEntryType.CONVENTIONAL,id));
    }
    public void setConventionalInRegisterFor(Inspectee inspectee, String animal, int number) {
        conventionalInRegister.remove(new OtherEntry(inspectee,animal,number, OtherEntryType.NO_EARRING,id));
        conventionalInRegister.add(new OtherEntry(inspectee,animal,number, OtherEntryType.NO_EARRING,id));
    }

    public int getConventionalTotalFor(Inspectee inspectee, String animal) {
        OtherEntry otherEntry = new OtherEntry(inspectee,animal,0,OtherEntryType.CONVENTIONAL,id);
        OtherEntry result = conventionalTotal.stream()
                .filter(x -> x.equals(otherEntry)).findAny().orElse(otherEntry);

        return result.getCount();
    }

    public List<OtherEntry> getConventionalTotalFor(Inspectee inspectee) {
        return conventionalTotal.stream()
                .filter(x -> x.getInspectee().equals(inspectee)).collect(Collectors.toList());
    }

    public List<OtherEntry> getConventionalInRegisterFor(Inspectee inspectee) {
        return conventionalInRegister.stream()
                .filter(x -> x.getInspectee().equals(inspectee)).collect(Collectors.toList());
    }
    public int getConventionalInRegisterFor(Inspectee inspectee, String animal) {
        OtherEntry otherEntry = new OtherEntry(inspectee,animal,0,OtherEntryType.NO_EARRING,id);
        OtherEntry result = conventionalInRegister.stream()
                .filter(x -> x.equals(otherEntry)).findAny().orElse(otherEntry);

        return result.getCount();
    }
    public void setConventionalInRegister(Set<OtherEntry> conventionalInRegister) {
        this.conventionalInRegister = conventionalInRegister;
    }

    public List<Inspectee> getProducers()
    {
        List<Inspectee> names =getProducersWithNoDummy();
        names.add(Inspectee.getDummyInspectee());
        return names;
    }

    public List<Inspectee> getProducersWithNoDummy()
    {
        List<Inspectee> names = new ArrayList<>();
        names.add(new Inspectee(producer1Tin,producer1Name));
        if (!producer2Name.trim().isEmpty()) names.add(new Inspectee(producer2Tin,producer2Name));
        if (!producer3Name.trim().isEmpty()) names.add(new Inspectee(producer3Tin,producer3Name));
        if (!producer4Name.trim().isEmpty()) names.add(new Inspectee(producer4Tin,producer4Name));
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
    //Καταμετρηθέντα + Οσα σκαναρίστηκαν εκτός των διπλών πλην αλόγων + όσα με συμβατικά ενώτια
    public long getCount(String producer1Tin) {
        long allCountElectronicTag = getEntries().stream().filter(x -> !x.isDummy()
                && x.getProducerTin().equals(producer1Tin)).count();
        long allCountElectronicTagWithoutHorses = allCountElectronicTag -
                getCount(producer1Tin,Animals.HORSE_ANIMAL);
        long conventionalTagCount =
                conventionalTotal.stream().filter(x->x.getInspectee().getTin().equals(producer1Tin)
                    && !x.getAnimal().equals(Animals.HORSE_ANIMAL)).count();
        return allCountElectronicTagWithoutHorses+conventionalTagCount;
    }

    //Όσα είναι στο μητρώο
    public long getInRegisterCount(String producer1Tin) {
        return getEntries().stream()
                .filter(x->!x.isDummy()
                        && x.isInRegister()==true
                        && x.getProducerTin().equals(producer1Tin)
                        && !x.getAnimalType().equals(Animals.HORSE_ANIMAL)
                        && !x.getComment().equals(CommentType.DOUBLE)
                        && !x.getComment().equals(CommentType.CUT)
                        && !x.getComment().equals(CommentType.SOLD)
                        && !x.getComment().equals(CommentType.DEAD)).count()
                + conventionalInRegister.stream().filter(x->x.getInspectee().getTin().equals(producer1Tin)
                && !x.getAnimal().equals(Animals.HORSE_ANIMAL)).count();
    }
    public long getUniqueTag(String producer1Tin) {
        return getEntries().stream()
                .filter(x->!x.isDummy()
                        && x.isInRegister()==true
                        && x.getProducerTin().equals(producer1Tin)
                        && x.getComment().equals(CommentType.SINGLE)).count()
                ;
    }
    //Καταμετρηθέντα
    public long getCount(String producer1Tin, String animals) {
        return getEntries().stream().filter(x->!x.isDummy()
                && x.getAnimalType().trim().equals(animals) && x.getProducerTin()
                .equals(producer1Tin)).count()
                + conventionalTotal.stream().filter(x->x.getInspectee().getTin().equals(producer1Tin)
                && x.getAnimal().equals(animals)).count();
    }
    //Στο μητρώο
    public long getInRegisterCount(String producer1Tin, String animals) {
        return getEntries().stream().filter(x->!x.isDummy()
                && x.isInRegister()==true
                && !x.getComment().equals(CommentType.CUT)
                && !x.getComment().equals(CommentType.SOLD)
                && !x.getComment().equals(CommentType.DEAD)
                && !x.getComment().equals(CommentType.DOUBLE)
                && x.getAnimalType().trim().equals(animals) && x.getProducerTin()
                .equals(producer1Tin)).count()
                + conventionalTotal.stream().filter(x->x.getInspectee().getTin().equals(producer1Tin)
                && x.getAnimal().equals(animals)).count();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<String> toStrings()
    {
        List<String> strings = new ArrayList<>();
        String line0="Ημερομηνία:" + "," + format.format(date)+"\n";
        String line1="ΑΦΜ Παραγωγού:" +"," + producer1Tin +","+"Ονοματεπώνυμο:" + "," + producer1Name;
        String line2="ΑΦΜ 1ου συστεγαζόμενου:" +"," + producer2Tin +","+"Ονοματεπώνυμο:" + "," + producer2Name;
        String line3="ΑΦΜ 2ου συστεγαζόμενου:" +"," + producer3Tin +","+"Ονοματεπώνυμο:" + "," + producer3Name;
        String line4="ΑΦΜ 3ου συστεγαζόμενου:" +"," + producer4Tin +","+"Ονοματεπώνυμο:" + "," + producer4Name;

        String line5 = "Συντεταγμένες:" +"," +latitude + "," + longitude;
        strings.add(line0);
        strings.add(line1);
        strings.add(line2);
        strings.add(line3);
        strings.add(line4);
        strings.add(line4);
        strings.add(line5);
        String header = "Χώρα, Ενώτιο,Ημ/νια Ελέγχου,Βρέθηκε στο μητρώο,Ονοματεπώνυμο Παραγωγού,ΑΦΜ Παραγωγού,Είδος ζώου,Φυλή, Σχόλια";
        strings.add(header);
        strings.addAll(entries.stream().filter(x->!x.isDummy()).map(x->x.toString()).collect(Collectors.toList()));
        return strings;
    }
}
