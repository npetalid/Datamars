
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

package gr.petalidis.datamars;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;

import gr.petalidis.datamars.inspections.domain.Entry;

public class EntryDateMatcher extends TypeSafeMatcher<Entry> {
    private final Date dateExpected;
    private EntryDateMatcher(Date dateExpected) {
        this.dateExpected = dateExpected;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Date did not contain a value of " + dateExpected);
    }

    @Override
    protected boolean matchesSafely(Entry item) {
        return item.getTagDate().equals(dateExpected);
    }

    public static EntryDateMatcher hasEntryDate(Date dateExpected) {
        return new EntryDateMatcher(dateExpected);
    }
}
