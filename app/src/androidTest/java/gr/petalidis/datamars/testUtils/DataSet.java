package gr.petalidis.datamars.testUtils;

import androidx.test.platform.app.InstrumentationRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.rsglibrary.Rsg;

public class DataSet {

    private static Map<String, Integer> animalToConventional = new HashMap<>();

    static {
        animalToConventional.put("Προβατίνα-ΝΑΙ-<2010", R.id.sheepLegalConventionalTag);
        animalToConventional.put("Προβατίνα-ΟΧΙ-", R.id.sheepConventionalOutOfRegistry);
        animalToConventional.put("Προβατίνα-ΝΑΙ-ΜΟΝ", R.id.sheepSingleConventionalTag);
        animalToConventional.put("Προβατίνα-ΝΑΙ->2010", R.id.sheepIllegalConventionalTag);

        animalToConventional.put("Γίδα-ΝΑΙ-<2010", R.id.goatLegalConventionalTag);
        animalToConventional.put("Γίδα-ΟΧΙ-", R.id.goatConventionalOutOfRegistry);
        animalToConventional.put("Γίδα-ΝΑΙ-ΜΟΝ", R.id.goatSingleConventionalTag);
        animalToConventional.put("Γίδα-ΝΑΙ->2010", R.id.goatIllegalConventionalTag);

        animalToConventional.put("Κριάρι-ΝΑΙ-<2010", R.id.ramLegalConventionalTag);
        animalToConventional.put("Κριάρι-ΟΧΙ-", R.id.ramConventionalOutOfRegistry);
        animalToConventional.put("Κριάρι-ΝΑΙ-ΜΟΝ", R.id.ramSingleConventionalTag);
        animalToConventional.put("Κριάρι-ΝΑΙ->2010", R.id.ramIllegalConventionalTag);

        animalToConventional.put("Τράγος-ΝΑΙ-<2010", R.id.heGoatLegalConventionalTag);
        animalToConventional.put("Τράγος-ΟΧΙ-", R.id.heGoatConventionalOutOfRegistry);
        animalToConventional.put("Τράγος-ΝΑΙ-ΜΟΝ", R.id.heGoatSingleConventionalTag);
        animalToConventional.put("Γράγος-ΝΑΙ->2010", R.id.heGoatIllegalConventionalTag);

    }

    public static DataEntry readData(String name) throws IOException, ParseException {
        List<DataEntry.ConventionalEntry> conventionalEntries = new ArrayList<>();
        List<DataEntry.ConventionalEntry> noTagEntries = new ArrayList<>();
        List<DataEntry.TagEntry> tagEntries = new ArrayList<>();
        List<DataEntry.Producer> producerSet = new ArrayList<>();
        Map<String, String> tinToRsgOwner = new HashMap<>();
        Map<String, String> tinToName = new HashMap<>();
        Map<String, DataEntry.ResultEntryBuilder> tinToResultEntryBuilder = new HashMap<>();
        Set<Rsg> rsgSet = new HashSet<>();
        int index = 0;

        try (InputStream in = InstrumentationRegistry.getInstrumentation().getContext().getAssets().open(name);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            boolean headersHaveBeenRead = false;
            while ((line = br.readLine()) != null) {
                if (headersHaveBeenRead == false) {
                    headersHaveBeenRead = true;
                } else {
                    String[] columns = line.split(",");
                    String tin = columns[1];
                    String producer = columns[2];
                    tinToName.put(tin, producer);

                    if (columns[0].equalsIgnoreCase("ΣΥΜΒΑΤΙΚΑ")) {
                        String animal = columns[3];
                        String inRegister = columns[4];
                        String comments = columns.length>5?columns[5]:"";
                        String quantity = columns.length>6?columns[6]:"0";

                        conventionalEntries.add(new DataEntry.ConventionalEntry(
                                animalToConventional.get(animal + "-" + inRegister + "-" + comments)
                                , quantity, tin));
                    } else if (columns[0].equalsIgnoreCase("ΚΑΘΟΛΟΥ")) {
                        String quantity = columns[6];

                        noTagEntries.add(new DataEntry.ConventionalEntry(
                                R.id.noTagOver6MonthValue
                                , quantity, tin));
                    } else if (columns[0].equalsIgnoreCase("ΚΑΘΟΛΟΥ < 6 ΜΗΝΩΝ")) {
                        String quantity = columns[6];

                        noTagEntries.add(new DataEntry.ConventionalEntry(
                                R.id.noTagUnder6MonthValue
                                , quantity, tin));
                    } else if (columns[0].equalsIgnoreCase("ΑΠΟΤΕΛΕΣΜΑ")) {
                        String quantity = columns[6];

                        if (tinToResultEntryBuilder.get(tin) == null) {
                            tinToResultEntryBuilder.put(tin, new DataEntry.ResultEntryBuilder(tin));
                        }
                        if (columns[3].equals("Καταμετρηθέντα")) {
                            tinToResultEntryBuilder.get(tin).addTotal(quantity);
                        } else if (columns[3].equalsIgnoreCase("Χωρίς σήμανση")) {
                            tinToResultEntryBuilder.get(tin).addNoTag(quantity);
                        } else if (columns[3].equalsIgnoreCase("< 6 μηνών χωρίς σήμανση")) {
                            tinToResultEntryBuilder.get(tin).addNoTagUnder6(quantity);
                        } else if (columns[3].equalsIgnoreCase("Χωρίς ηλ. Σήμανση")) {
                            tinToResultEntryBuilder.get(tin).addNoElectronicTag(quantity);
                        } else if (columns[3].equalsIgnoreCase("Με σήμανση που δεν αναγράφονται στο μητρώο")) {
                            tinToResultEntryBuilder.get(tin).addCountedButNotInRegistry(quantity);
                        } else if (columns[3].equalsIgnoreCase("Μονό ενώτιο")) {
                            tinToResultEntryBuilder.get(tin).addSingleTag(quantity);
                        } else if (columns[3].equalsIgnoreCase("Προβατίνα")) {
                            tinToResultEntryBuilder.get(tin).addSelectableSheep(quantity);
                        } else if (columns[3].equalsIgnoreCase("Κριοί-Τράγοι")) {
                            tinToResultEntryBuilder.get(tin).addSelectableRamHeGoat(quantity);
                        } else if (columns[3].equalsIgnoreCase("Γίδα")) {
                            tinToResultEntryBuilder.get(tin).addSelectableGoat(quantity);
                        } else if (columns[3].equalsIgnoreCase("Ιπποειδή")) {
                            tinToResultEntryBuilder.get(tin).addSelectableHorse(quantity);
                        } else if (columns[3].equalsIgnoreCase(("Αμνοερίφια"))) {
                            tinToResultEntryBuilder.get(tin).addSelectableKidLamb(quantity);
                        }
                    } else {
                        String tag = "012345603000" + columns[0];
                        String date = "22022018";
                        String time = "09" + (index < 10 ? "0" + index : index) + "00";
                        boolean inRegister = columns[4].equalsIgnoreCase("ΝΑΙ") || columns[4].equalsIgnoreCase("NAI");
                        Rsg rsg = new Rsg(tag, date, time);
                        tagEntries.add(new DataEntry.TagEntry(rsg.getIdentificationCode(), columns[3], tin, inRegister, columns.length>5?columns[5]:""));
                        tinToRsgOwner.put(tin, rsg.getOwner());
                        rsgSet.add(rsg);
                    }
                }
            }
            List<DataEntry.ResultEntry> resultEntries = tinToResultEntryBuilder.values()
                    .stream().map(x -> x.build()).flatMap(Collection::stream).collect(Collectors.toList());
            DataEntry.ProducerBuilder producerBuilder = DataEntry.ProducerBuilder.getInstance();
            tinToName.entrySet().forEach(x ->
                    producerBuilder.addProducer(x.getValue(), x.getKey(), tinToRsgOwner.get(x.getKey())));
            producerSet.addAll(producerBuilder.build());
            DataEntry.saveRsg(rsgSet);

            return new DataEntry(producerSet, tagEntries, conventionalEntries, noTagEntries, resultEntries);
        }

    }
}
