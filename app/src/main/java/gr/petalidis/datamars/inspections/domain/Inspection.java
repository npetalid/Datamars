
/*
 * Copyright 2017-2019 Nikolaos Petalidis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package gr.petalidis.datamars.inspections.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import gr.petalidis.datamars.inspections.dto.ThumbnailDto;
import gr.petalidis.datamars.rsglibrary.Rsg;

public class Inspection implements Serializable {
    private final static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    private UUID id = UUID.randomUUID();

    private Date date;

    private String producer1Tin;
    private String producer1Name;
    private String producer2Tin = "";
    private String producer2Name = "";
    private String producer3Tin = "";
    private String producer3Name = "";
    private String producer4Tin = "";
    private String producer4Name = "";

    private List<Entry> entries = new ArrayList<>();

    private Set<OtherEntry> conventionalTags = new HashSet<>();

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

    public String getProducer1Name() {
        return producer1Name;
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
        Map<Rsg,Set<Rsg>> doubles = Rsg.getEntriesMatching(rsgs, rsg-> rsg::isDoubleOf);
        Map<Rsg,Set<Rsg>> errors = Rsg.getEntriesMatching(rsgs, rsg-> rsg::isInErrorOf);

        Set<Rsg> flattenedDoubles = doubles.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        Set<Rsg> flattenedErrors = errors.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());

        this.entries = rsgs
                .stream()
                .map(x-> {
                    Entry entry = new Entry(this,
                            x.getCountryCode(),
                            x.getIdentificationCode(),
                            x.getDate());
                    if (flattenedDoubles.contains(x)) {
                        entry.setComment(CommentType.DOUBLE);
                        entry.setInRegister(false);
                    }
                    if (flattenedErrors.contains(x)) {
                        entry.setComment(CommentType.FAULT);
                        entry.setInRegister(false);
                    }
                    return entry;
                })
                .collect(Collectors.toList());

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

    public Set<OtherEntry> getConventionalTags() {
        return conventionalTags;
    }

    public void setLegalConventionalTag(Inspectee inspectee, String animal, int number) {
        conventionalTags.remove(new OtherEntry(inspectee,animal,number, OtherEntryType.CONVENTIONAL,id));
        conventionalTags.add(new OtherEntry(inspectee,animal,number, OtherEntryType.CONVENTIONAL,id));
    }

    public void setConventionalTags(Set<OtherEntry> conventionalTags) {
        this.conventionalTags = conventionalTags;
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

    //Καταμετρηθέντα + Οσα σκαναρίστηκαν εκτός των FALSE και αλόγων + όσα με συμβατικά ενώτια
    private long getCount(String producer1Tin) {
        long allCountElectronicTag = getEntries().stream().filter(x -> !x.isDummy()
                && x.getProducerTin().equals(producer1Tin) && !x.getComment().equals(CommentType.FAULT)).count();

        long allCountElectronicTagWithoutHorses = allCountElectronicTag -
                getCount(producer1Tin, AnimalType.HORSE_ANIMAL);
        long conventionalTagCount =
                conventionalTags.stream().filter(x->x.getInspectee().getTin().equals(producer1Tin)).map(OtherEntry::getCount).reduce(0,Integer::sum);
        return allCountElectronicTagWithoutHorses+conventionalTagCount;
    }

    //Καταμετρηθέντα
    private long getCount(String producer1Tin, AnimalType animalType) {
        return getEntries().stream().filter(x->!x.isDummy()
                && x.getAnimalType().trim().equals(animalType.title) && x.getProducerTin()
                .equals(producer1Tin)).count()
                + conventionalTags.stream().filter(x->x.getInspectee().getTin().equals(producer1Tin)
                && x.getAnimal().equals(animalType.title)).map(OtherEntry::getCount).reduce(0,Integer::sum);
    }

    //Επιλέξιμα: Όσα είναι στο μητρώο + όσα είναι νομιμα συμβατικα  (ανά είδος)
    private long getSelectableCount(String producer1Tin, String animals) {
        long singleElectronic = getEntries().stream().filter(x -> !x.isDummy()
                && x.isInRegister()
                && x.getComment().equals(CommentType.SINGLE)
                && x.getAnimalType().trim().equals(animals) && x.getProducerTin()
                .equals(producer1Tin)).count();
        Integer singleConventional = conventionalTags.stream().filter(x -> x.getInspectee().getTin().equals(producer1Tin)
                && x.getAnimal().equals(animals) &&
                x.getEntryType() == OtherEntryType.SINGLE).map(OtherEntry::getCount).reduce(0, Integer::sum);

        long allOthers = getEntries().stream().filter(x -> !x.isDummy()
                && x.isInRegister()
                && !x.getComment().equals(CommentType.SLAUGHTERED)
                && !x.getComment().equals(CommentType.SOLD)
                && !x.getComment().equals(CommentType.DEAD)
                && !x.getComment().equals(CommentType.DOUBLE)
                && !x.getComment().equals(CommentType.FAULT)
                && !x.getComment().equals(CommentType.SINGLE)
                && x.getAnimalType().trim().equals(animals) && x.getProducerTin()
                .equals(producer1Tin)).count()
                +
                conventionalTags.stream().filter(x -> x.getInspectee().getTin().equals(producer1Tin)
                        && x.getAnimal().equals(animals) && x.getEntryType() == OtherEntryType.CONVENTIONAL)
                        .map(OtherEntry::getCount).reduce(0, Integer::sum);

        long total = singleConventional+singleElectronic+allOthers;
        float singles = singleConventional+singleElectronic;
        if (total==0) {
            return 0;
        }

        float pct = singles / total;
        if (pct<=0.2f) { //Count animals with single tags only when they are <=20% of the total selectables
            return total;
        } else {
            return allOthers;
        }
    }

    //Ζώα χωρίς ενώτια ούτε ηλ. σήμανση:Εκτός ιστορικής περιόδου συμβατικά + νούμερο που εισάγει ο χρήστης + όσα έχουν χαρακτηριστεί διπλά
    private long getNoTag(String producerTin)
    {
        long doubles = getEntries().stream().filter(x -> !x.isDummy()
                && x.getComment().equals(CommentType.DOUBLE)
                && x.getProducerTin()
                .equals(producerTin)).count();

        Integer untagged = conventionalTags.stream().filter(x -> x.getInspectee().getTin().equals(producerTin)
                && x.getEntryType() == OtherEntryType.NO_EARRING
                && x.getAnimal().equals("Over6")).map(OtherEntry::getCount).reduce(0, Integer::sum);
        return doubles+untagged;
    }

    //Πολλαπλή:
    //Χωρίς ηλ. σήμανση: συμβατικά παράνομα
    private long getNoElectronicTag(String producerTin)
    {
        return  conventionalTags.stream().filter(x->x.getInspectee().getTin().equals(producerTin)
                && (x.getEntryType()==OtherEntryType.ILLEGAL
                    ||
                (x.getEntryType()==OtherEntryType.NO_EARRING
                && x.getAnimal().equals("Over6")))).map(OtherEntry::getCount).reduce(0,Integer::sum);
    }

    //Ζώα με ένα ένωτιο: Είτε συμβατικό είτε ηλεκτρονικό
    private long getUniqueTag(String producer1Tin) {
        return getEntries().stream()
                .filter(x->!x.isDummy()
                        && x.isInRegister()
                        && x.getProducerTin().equals(producer1Tin)
                        && x.getComment().equals(CommentType.SINGLE)).count()
                + conventionalTags.stream().filter(x->x.getInspectee().getTin().equals(producer1Tin)
               && x.getEntryType()==OtherEntryType.SINGLE).map(OtherEntry::getCount).reduce(0,Integer::sum);
    }



    //Καταμετρηθέντα ζώα με σήμανση που δεν αναγράφονται στο μητρώο: Όσα δεν έχουν τσεκ στο μητρώο (απο ηλεκτρονικά)
    //+ νόμιμα συμβατικά που δεν είναι στο μητρώο
    private long getOutOfRegistryTagged(String producer1Tin) {
        long outOfRegistry = getEntries().stream().filter(x -> !x.isDummy()
                && !x.isInRegister()
                && !x.getComment().equals(CommentType.DOUBLE)
                && !x.getComment().equals(CommentType.FAULT)
                && x.getProducerTin()
                .equals(producer1Tin)).count();

        Integer outOfRegistryUntagged = conventionalTags.stream().filter(x -> x.getInspectee().getTin().equals(producer1Tin)
                && x.getEntryType() == OtherEntryType.OUT_OF_REGISTRY //Χωρίς ενώτιο και άνω των 6 μηνών
                ).map(OtherEntry::getCount).reduce(0, Integer::sum);
        return outOfRegistry+outOfRegistryUntagged;

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
        strings.addAll(entries.stream().filter(x->!x.isDummy()).map(Entry::toString).collect(Collectors.toList()));
        return strings;
    }

    public void setSingleConventionalTag(Inspectee inspectee, String animal, int number) {
        conventionalTags.remove(new OtherEntry(inspectee,animal,number, OtherEntryType.SINGLE,id));
        conventionalTags.add(new OtherEntry(inspectee,animal,number, OtherEntryType.SINGLE,id));
    }

    public void setIllegalConventionalTag(Inspectee inspectee, String animal, int number) {
        conventionalTags.remove(new OtherEntry(inspectee,animal,number, OtherEntryType.ILLEGAL,id));
        conventionalTags.add(new OtherEntry(inspectee,animal,number, OtherEntryType.ILLEGAL,id));
    }

    public void setConventionalOutOfRegistry(Inspectee inspectee, String animal, int number) {
        conventionalTags.remove(new OtherEntry(inspectee,animal,number, OtherEntryType.OUT_OF_REGISTRY,id));
        conventionalTags.add(new OtherEntry(inspectee,animal,number, OtherEntryType.OUT_OF_REGISTRY,id));
    }

    public int getLegalConventionalTagFor(Inspectee inspectee, String animal) {
        OtherEntry otherEntry = new OtherEntry(inspectee,animal,0,OtherEntryType.CONVENTIONAL,id);
        OtherEntry result = conventionalTags.stream()
                .filter(x -> x.equals(otherEntry)).findAny().orElse(otherEntry);

        return result.getCount();
    }

    public int getSingleConventionalTagFor(Inspectee inspectee, String animal) {
        OtherEntry otherEntry = new OtherEntry(inspectee,animal,0,OtherEntryType.SINGLE,id);
        OtherEntry result = conventionalTags.stream()
                .filter(x -> x.equals(otherEntry)).findAny().orElse(otherEntry);

        return result.getCount();
    }

    public int getIllegalConventionalTagFor(Inspectee inspectee, String animal) {
        OtherEntry otherEntry = new OtherEntry(inspectee,animal,0,OtherEntryType.ILLEGAL,id);
        OtherEntry result = conventionalTags.stream()
                .filter(x -> x.equals(otherEntry)).findAny().orElse(otherEntry);

        return result.getCount();
    }

    public int getOutOfRegistryTagFor(Inspectee inspectee, String animal) {
        OtherEntry otherEntry = new OtherEntry(inspectee,animal,0,OtherEntryType.OUT_OF_REGISTRY,id);
        OtherEntry result = conventionalTags.stream()
                .filter(x -> x.equals(otherEntry)).findAny().orElse(otherEntry);

        return result.getCount();

    }

    public void setNoTagUnder6Month(Inspectee inspectee, int number) {
        conventionalTags.remove(new OtherEntry(inspectee,"Under6",number, OtherEntryType.NO_EARRING,id));
        conventionalTags.add(new OtherEntry(inspectee,"Under6",number, OtherEntryType.NO_EARRING,id));
    }

    public void setNoTagOver6Month(Inspectee inspectee, int number) {
        conventionalTags.remove(new OtherEntry(inspectee,"Over6",number, OtherEntryType.NO_EARRING,id));
        conventionalTags.add(new OtherEntry(inspectee,"Over6",number, OtherEntryType.NO_EARRING,id));
    }

    public int getNoTagOver6MonthOld(Inspectee inspectee) {
        OtherEntry otherEntry = new OtherEntry(inspectee,"Over6",0,OtherEntryType.NO_EARRING,id);
        OtherEntry result = conventionalTags.stream()
                .filter(x -> x.equals(otherEntry)).findAny().orElse(otherEntry);

        return result.getCount();
    }

    public int getNoTagUnder6MonthOld(Inspectee inspectee) {
            OtherEntry otherEntry = new OtherEntry(inspectee,"Under6",0,OtherEntryType.NO_EARRING,id);
            OtherEntry result = conventionalTags.stream()
                    .filter(x -> x.equals(otherEntry)).findAny().orElse(otherEntry);

            return result.getCount();
    }

    public Report generateReportFor(Inspectee inspectee)
    {
        Map<AnimalType,Long> selectablesMap = new HashMap<>();
        EnumSet.allOf(AnimalType.class).stream().filter(x->x!=AnimalType.KID_ANIMAL && x!=AnimalType.LAMB_ANIMAL).forEach(animal->
                selectablesMap.put(animal,getSelectableCount(inspectee.getTin(),  animal.getTitle())));

        long kids = getSelectableCount(inspectee.getTin(),AnimalType.KID_ANIMAL.getTitle());
        long lambs =getSelectableCount(inspectee.getTin(),AnimalType.LAMB_ANIMAL.getTitle());

        selectablesMap.put(AnimalType.KIDLAMB_ANIMAL,
                kids+
                        lambs+
                + getNoTagUnder6MonthOld(inspectee));

        return new Report(inspectee,
                getCount(inspectee.getTin()),
                getNoTag(inspectee.getTin()),
                getNoTagUnder6MonthOld(inspectee),
                getNoElectronicTag(inspectee.getTin()),
                getUniqueTag(inspectee.getTin()),
                getOutOfRegistryTagged(inspectee.getTin()),
                selectablesMap
                );
    }
}
