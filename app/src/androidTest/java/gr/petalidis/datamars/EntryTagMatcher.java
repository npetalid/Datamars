package gr.petalidis.datamars;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import gr.petalidis.datamars.inspections.domain.Entry;

public class EntryTagMatcher extends TypeSafeMatcher<Entry> {
    private final String tagExpected;
    private EntryTagMatcher(String tagExpected) {
        this.tagExpected = tagExpected;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(" Tad dit not contain a value of " + tagExpected);
    }

    @Override
    protected boolean matchesSafely(Entry item) {
        return item.getTag().equals(tagExpected);
    }

    public static EntryTagMatcher hasEntryTag(String tagExpected) {
        return new EntryTagMatcher(tagExpected);
    }
}
