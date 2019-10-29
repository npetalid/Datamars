package gr.petalidis.datamars.testUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.AnimalType;
import gr.petalidis.datamars.rsglibrary.CsvRootDirectory;
import gr.petalidis.datamars.rsglibrary.Rsg;
import gr.petalidis.datamars.rsglibrary.RsgExporter;

public class DataEntry {
    private static Set<Rsg> rsgsArray;

    public final static String USB_NAME = "testDevice";
    public final static String TEST_FILE_NAME = "2018-02-22.csv";
    static {
        try {
            rsgsArray = new HashSet<>(Arrays.asList(
                    new Rsg("01234560300062037570054", "22022018", "091614"),
                    new Rsg("01234560300062037570071", "22022018", "091715"),
                    new Rsg("01234560300062037570055", "22022018", "091816"),
                    new Rsg("01234560300062037570012", "22022018", "091917"),
                    new Rsg("01234560300062037570021", "22022018", "092018"),
                    new Rsg("01234560300062037570076", "22022018", "092119"),
                    new Rsg("01234560300062037570027", "22022018", "092220"),
                    new Rsg("01234560300062036290063", "22022018", "092321"),
                    new Rsg("01234560300062037570061", "22022018", "092422"),
                    new Rsg("01234560300062037570072", "22022018", "092523"),
                    new Rsg("01234560300062037570066", "22022018", "092624"),
                    new Rsg("01234560300062037570013", "22022018", "092725"),
                    new Rsg("01234560300062037570068", "22022018", "092826"),
                    new Rsg("01234560300062036290068", "22022018", "092927"),
                    new Rsg("01234560300062037570065", "22022018", "093028"),
                    new Rsg("01234560300062037570081", "22022018", "093129")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private List<Producer> producers;
    private List<TagEntry> tagEntries;
    private List<ConventionalEntry> conventionalEntries;
    private List<ConventionalEntry> noTagEntries;
    private List<ResultEntry> resultEntries;


    public DataEntry(List<Producer> producers, List<TagEntry> tagEntries, List<ConventionalEntry> conventionalEntries,
                     List<ConventionalEntry> noTagEntries,
                     List<ResultEntry> resultEntries) {
        this.producers = producers;
        this.tagEntries = tagEntries;
        this.conventionalEntries = conventionalEntries;
        this.noTagEntries = noTagEntries;
        this.resultEntries = resultEntries;
    }

    public List<Producer> getProducers() {
        return producers;
    }

    public List<TagEntry> getTagEntries() {
        return tagEntries;
    }

    public List<ConventionalEntry> getConventionalEntries() {
        return conventionalEntries;
    }

    public List<ConventionalEntry> getNoTagEntries() {
        return noTagEntries;
    }

    public List<ResultEntry> getResultEntries() {
        return resultEntries;
    }

    public Object[] toArray()
    {
        return new Object[] {
                getProducers(),
                getTagEntries(),
                getConventionalEntries(),
                getNoTagEntries(),
                getResultEntries()
        };
    }

    public static class ProducerBuilder {
        private List<Producer> producers = new ArrayList<>();
        public static ProducerBuilder getInstance() {
            return new ProducerBuilder();
        }
        public  ProducerBuilder addProducer(String name, String tin, String tag)
        {
            int tinId = R.id.producer1TinText;
            int nameId = R.id.producer1NameText;
            int tagId = R.id.producer1TagValue;
            if (producers.size()==1) {
               tinId = R.id.producer2TinText;
               nameId = R.id.producer2NameText;
               tagId = R.id.producer2TagValue;
            } else if (producers.size()==2) {
                tinId = R.id.producer3TinText;
                nameId = R.id.producer3NameText;
                tagId = R.id.producer3TagValue;
            } else if (producers.size()==3) {
                tinId = R.id.producer4TinText;
                nameId = R.id.producer4NameText;
                tagId = R.id.producer3TagValue;
            }
            producers.add(new Producer(producers.size(),name,tin,tag,tinId,nameId,tagId));
            return this;
        }
        public List<Producer> build()
        {
            return producers;
        }
    }
    public static class Producer {
         final private String name;
        final private String tin;
        final private String tag;
        final private int tinId;
        final private int nameId;
        final private int number;
        private final int tagId;

        public Producer(int number, String name, String tin, String tag, int tinId, int nameId, int tagId) {
            this.name = name;
            this.tin = tin;
            this.tag = tag;
            this.tinId = tinId;
            this.nameId = nameId;
            this.number = number;
            this.tagId = tagId;
        }

        public String getName() {
            return name;
        }

        public String getTin() {
            return tin;
        }

        public String getTag() {
            return tag;
        }

        public int getTinId() {
            return tinId;
        }

        public int getNameId() {
            return nameId;
        }

        public int getTagId() {
            return tagId;
        }

        public int getIndex()
        {
            return number;
        }
        public boolean isFirst() {
            return number==0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Producer producer = (Producer) o;
            return tin.equals(producer.tin);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tin);
        }
    }

    public static class TagEntry {
        final private String tag;
        final private String animal;
        final private boolean inRegister;
        final private String comment;
        private final String producer;

        public TagEntry(String tag, String animal, String producer, boolean inRegister, String comment) {
            this.tag = tag;
            this.animal = animal;
            this.inRegister = inRegister;
            this.comment = comment;
            this.producer = producer;

        }

        public String getTag() {
            return tag;
        }

        public String getAnimal() {
            return animal;
        }

        public boolean isInRegister() {
            return inRegister;
        }

        public String getComment() {
            return comment;
        }

        public String getProducer() {
            return producer;
        }
    }

    public static class ConventionalEntry {
            private final int fieldId;
            private final String number;
            private final String tin;
        public ConventionalEntry(int fieldId, int number, String tin) {
            this(fieldId,number+"",tin);
        }
        public ConventionalEntry(int fieldId, String number, String tin) {
            this.fieldId = fieldId;
            this.number = number;
            this.tin = tin;
        }

        public int getFieldId() {
            return fieldId;
        }

        public String getNumber() {
            return number;
        }

        public String getTin() {
            return tin;
        }
    }

    public static class ResultEntryBuilder {
        private List<ResultEntry> resultEntryList = new ArrayList<>();
        private String tin;

        public ResultEntryBuilder(String tin) {
            this.tin = tin;
        }

        public ResultEntryBuilder addTotal(String value)
        {
            resultEntryList.add(new ResultEntry(tin+"-total",value+""));
            return this;
        }
        public ResultEntryBuilder addNoTagUnder6(String value)
        {
            resultEntryList.add( new ResultEntry(tin+"-noTagUnder6",value+""));
            return this;
        }
        public ResultEntryBuilder addNoElectronicTag(String value)
        {
            resultEntryList.add( new ResultEntry(tin+"-noElectronicTag",value+""));
            return this;
        }
        public ResultEntryBuilder addSingleTag(String value)
        {
            resultEntryList.add( new ResultEntry(tin+"-singleTag",value+""));
            return this;
        }
        public ResultEntryBuilder addCountedButNotInRegistry(String value)
        {
            resultEntryList.add( new ResultEntry(tin+"-countedButNotInRegistry",value+""));
            return this;
        }
        public ResultEntryBuilder addSelectableSheep(String value)
        {
            resultEntryList.add( new ResultEntry(tin+"-"+ AnimalType.SHEEP_ANIMAL.getTitle(),value+""));
            return this;
        }
        public ResultEntryBuilder addSelectableGoat(String value)
        {
            resultEntryList.add( new ResultEntry(tin+"-"+ AnimalType.GOAT_ANIMAL.getTitle(),value+""));
            return this;
        }
        public ResultEntryBuilder addKidLamb(String value) {
            resultEntryList.add( new ResultEntry(tin+"-Αμνοερίφια",value+""));
            return this;
        }
        public ResultEntryBuilder addNoTag(String value) {
            resultEntryList.add( new ResultEntry(tin+"-noΤag",value+""));
            return this;
        }


        public void addSelectableRamHeGoat(String value) {
            resultEntryList.add( new ResultEntry(tin+"-"+ "Κριοί-Τράγοι",value+""));
        }
        public List<ResultEntry> build()
        {
            return resultEntryList;
        }

        public void addSelectableHorse(String value) {
            resultEntryList.add( new ResultEntry(tin+"-"+AnimalType.HORSE_ANIMAL.getTitle(),value+""));
        }
        public void addSelectableKidLamb(String value) {
            resultEntryList.add( new ResultEntry(tin+"-"+AnimalType.KIDLAMB_ANIMAL.getTitle(),value+""));
        }
    }

    public static class ResultEntry {
        private final String id;
        private final String number;

        public ResultEntry(String id, String number) {
            this.id = id;
            this.number = number;
        }

        public String getId() {
            return id;
        }

        public String getNumber() {
            return number;
        }
    }
    public static String saveRsg(Set<Rsg> rsgs) throws IOException {
        CsvRootDirectory csvRootDirectory = new CsvRootDirectory();
        File datamarsDir = new File(csvRootDirectory.getDirectory() + File.separator + USB_NAME);
        if (!datamarsDir.exists()) {
            boolean mkDir = datamarsDir.mkdir();
            if (!mkDir) {
                throw new IllegalStateException("Could not create device-differentiating directory");
            }
        }
        return RsgExporter.export( rsgs, datamarsDir.getAbsolutePath(), TEST_FILE_NAME);
    }

    public static Set<Rsg> getRsgsArray() {
        return rsgsArray;
    }
}
