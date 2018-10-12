package gr.petalidis.datamars.inspections.utilities;

public class TagFormatter {

    private final static String TAG ="000000000000";
    public static String format(String tag)
    {
        if (tag.length()!=TAG.length()) {
            return tag;
        } else {
            String prefecture = tag.substring(0,4);
            String owner = tag.substring(4,8);
            String animal = tag.substring(8);
            return prefecture+"-"+owner+"-"+animal;
        }
    }
}
