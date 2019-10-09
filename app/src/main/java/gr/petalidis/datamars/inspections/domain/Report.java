package gr.petalidis.datamars.inspections.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Report {
    private Inspectee inspectee;

    private long total; //Καταμετρηθέντα
    private long noTag; //Ζώα χωρίς σήμανση
    private long noTagUnder6; //Ζώα χωρίς σήμανση < 6 μηνών
    private long noElectronicTag; //Ζώα χωρίς ηλεκτρονική σήμανση
    private long singleTag; //Ζώα με ένα ενώτιο
    private long countedButNotInRegistry; //Καταμετρηθέντα ζώα με σήμανση που δεν αναγράφονται στο μητρώο

    private Map<AnimalType,Long> selectable;
    public Report(Inspectee inspectee, long total, long noTag, long noTagUnder6, long noElectronicTag, long singleTag, long countedButNotInRegistry, Map<AnimalType,Long> selectable) {
        this.inspectee = inspectee;
        this.total = total;
        this.noTag = noTag;
        this.noTagUnder6 = noTagUnder6;
        this.noElectronicTag = noElectronicTag;
        this.singleTag = singleTag;
        this.countedButNotInRegistry = countedButNotInRegistry;
        this.selectable = selectable;
    }

    public long getTotal() {
        return total;
    }

    public long getNoTag() {
        return noTag;
    }

    public long getNoTagUnder6() {
        return noTagUnder6;
    }

    public long getNoElectronicTag() {
        return noElectronicTag;
    }

    public long getSingleTag() {
        return singleTag;
    }

    public long getCountedButNotInRegistry() {
        return countedButNotInRegistry;
    }

    public Map<AnimalType, Long> getSelectable() {
        return new HashMap<>(selectable);
    }

    @Override
    public String toString()
    {
        return "Παραγωγός: " + inspectee.getName() + "-" + inspectee.getTin() + "\n" +
                "Καταμετρηθέντα ζώα: " + getTotal() + "\n" +
                "Χωρίς σήμανση: " + getNoTag() + "\n" +
                "<6 μηνων χωρίς σήμανση: " + getNoTagUnder6() + "\n"+
                "Χωρίς ηλ. σήμανση: " + getNoElectronicTag() + "\n" +
                "Με ένα ενώτιο: " + getSingleTag() + "\n" +
                "Με σήμανση που δεν αναγράφονται στο μητρώο" + "\n" +
                "Επιλέξιμα: " + "\n" +
                selectable.entrySet().stream().map(entry->entry.getKey().title + ":" + entry.getValue()).collect(Collectors.joining("\n"))
                ;
    }
}
