package gr.petalidis.datamars.inspections.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Report {
    private final Inspectee inspectee;

    private final long total; //Καταμετρηθέντα
    private final long noTag; //Ζώα χωρίς σήμανση
    private final long noTagUnder6; //Ζώα χωρίς σήμανση < 6 μηνών
    private final long noElectronicTag; //Ζώα χωρίς ηλεκτρονική σήμανση
    private final long singleTag; //Ζώα με ένα ενώτιο
    private final long countedButNotInRegistry; //Καταμετρηθέντα ζώα με σήμανση που δεν αναγράφονται στο μητρώο

    private final Map<AnimalType,Long> selectable;
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

    private String surround(String id, String value) {
        return "<span id=\""+inspectee.getTin()+"-"+id+"\">" + value + "</span>";

    }
    private String surround(String id, long value) {
        return surround(id,value+"");

    }
    public long getTotal() {
        return total;
    }

    private String getTotalHtmlString() {
        return surround("total",getTotal());
    }
    public long getNoTag() {
        return noTag;
    }

    private String getNoTagHtmlString() {
        return surround("noΤag",getNoTag());

    }
    public long getNoTagUnder6() {
        return noTagUnder6;
    }
    private String getNoTagUnder6HtmlString() {
        return surround("noTagUnder6",getNoTagUnder6());

    }
    public long getNoElectronicTag() {
        return noElectronicTag;
    }

    private String getNoElectronicTagHtmlString() {
        return surround("noElectronicTag",getNoElectronicTag());
    }

    public long getSingleTag() {
        return singleTag;
    }

    private String getSingleTagHtmlString() {
        return surround("singleTag",getSingleTag());
    }
    public long getCountedButNotInRegistry() {
        return countedButNotInRegistry;
    }

    private String getCountedButNotInRegistryHtmlString()
    {
        return surround("countedButNotInRegistry",getCountedButNotInRegistry());
    }
    public Map<AnimalType, Long> getSelectable() {
        return new HashMap<>(selectable);
    }

    public Map<AnimalType, String> getSelectableHtmlString() {
        return selectable.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey(),
                e -> surround(e.getKey().title,e.getValue())
        ));
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

    public String toHtmlString()
    {
        return "<h4 class=\"header\" id=\"" +inspectee.getTin()+"\">" + inspectee.getName() + " ΑΦΜ " + inspectee.getTin() + "</h4>" +
                "<table><tr><td>" +
                "Καταμετρηθέντα: <i>" + getTotalHtmlString() + "</i></td><td></td></tr><tr><td>" +
                "Χωρίς σήμανση: <i>"  + getNoTagHtmlString() + "</i></td><td>" +
                "&lt; 6 μηνων χωρίς σήμανση: <i>" + getNoTagUnder6HtmlString() + "</i></td></tr><tr><td>" +
                "Χωρίς ηλ. σήμανση: <i>" + getNoElectronicTagHtmlString() + "</i></td><td>" + "Με μονό ενώτιο: <i>" + getSingleTagHtmlString() + "</i></td></tr><tr><td>" +
                "Με σήμανση εκτός μητρώου <i>" +getCountedButNotInRegistryHtmlString()+ "</i></td><td></td></tr>"
                + "</table>"
                +
               // "<h4>Επιλέξιμα</h4>" +
                "<table><tr><td>" +
                selectable.entrySet().stream().filter(entry->entry.getKey()==AnimalType.SHEEP_ANIMAL
                    || entry.getKey()==AnimalType.GOAT_ANIMAL)
                        .map(entry->entry.getKey().title + ": <i>" + surround(entry.getKey().title,entry.getValue())+"</i>")
                        .collect(Collectors.joining("</td><td>"))
                +"</td></tr><tr><td> Κριοί-Τράγοι: <i> " +
                surround("Κριοί-Τράγοι",selectable.entrySet().stream().filter(entry->entry.getKey()==AnimalType.RAM_ANIMAL
                || entry.getKey()==AnimalType.HEGOAT_ANIMAL)
                .map(entry->entry.getValue()).reduce(0L,Long::sum).toString()) + "</i></td>"
                +"<td>Αμνοερίφια: <i>" +
                surround("Αμνοερίφια",selectable.entrySet().stream().filter(entry->entry.getKey()==AnimalType.KIDLAMB_ANIMAL)
                        .map(entry->entry.getValue()).reduce(0L,Long::sum).toString()) + "</i>"
                +"</td></tr><tr><td>" +
                selectable.entrySet().stream().filter(entry->entry.getKey()==AnimalType.HORSE_ANIMAL)
                .map(entry->entry.getKey().title + ": <i>" +
                        surround(entry.getKey().title,entry.getValue())+"</i>").collect(Collectors.joining("</td><td>"))
                +"</td></tr>"
                + "</table>"
                ;
    }
}
