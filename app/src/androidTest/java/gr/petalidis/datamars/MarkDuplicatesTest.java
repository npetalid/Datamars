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

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import gr.petalidis.datamars.activities.StartActivity;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.rsglibrary.CsvRootDirectory;
import gr.petalidis.datamars.testUtils.MarkDuplicatesHelper;
import gr.petalidis.datamars.testUtils.RegisterInspectionHelper;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static gr.petalidis.datamars.EntryDateMatcher.hasEntryDate;
import static gr.petalidis.datamars.EntryTagMatcher.hasEntryTag;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.AllOf.allOf;

//@RunWith(Parameterized.class)
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MarkDuplicatesTest {

    @Rule
    public ActivityTestRule<StartActivity> activityRule =
            new ActivityTestRule<>(StartActivity.class);
    private String tmpFilePath = "";

    @Before
    public void setUp() throws Exception {
        Moo.setTestProperties(InstrumentationRegistry.getInstrumentation().getContext());
        DbHandler dbHandler = new DbHandler(Moo.getAppContext());
        dbHandler.dropDatabase(Moo.getAppContext());
        MarkDuplicatesHelper.saveRsg();
    }

    @Test
    public void testCorrectCalculationOfResults() {

        onView(withId(R.id.inspectionsButton))
                .perform(click());

        onView(withText("testDevice")).perform(click());

        onView(withId(R.id.createInspectionButton)).perform(click());

        String dateString = "MonthCellDescriptor{"
                + "date=Thu Feb 22 00:00:00 GMT+02:00 2018"
                + ", value=22"
                + ", isCurrentMonth=true"
                + ", isSelected=false"
                + ", isToday=false"
                + ", isSelectable=true"
                + ", isHighlighted=true"
                + ", rangeState=NONE"
                + '}';
        onView(withTagValue(hasToString(dateString)))
                .perform(scrollTo(), click());

        onView(withId(R.id.gotoStep2Screen)).perform(click());

        onView(withId(R.id.producer1NameText)).perform(typeText(MarkDuplicatesHelper.inspectee.getName()));
        onView(withId(R.id.producer1TinText)).perform(typeText(MarkDuplicatesHelper.inspectee.getTin()));

        onView(withId(R.id.gotoStep3Screen)).perform(click());

        MarkDuplicatesHelper.results().forEach(x -> {

            onData(allOf(hasEntryTag(x.getTag()), hasEntryDate(x.getDate()))).inAdapterView(withId(R.id.editItemsList))
                    .onChildView(withId(R.id.viewComments)).check(matches(withSpinnerText(x.getComment())));
            onData(allOf(hasEntryTag(x.getTag()), hasEntryDate(x.getDate()))).inAdapterView(withId(R.id.editItemsList))
                    .onChildView(withId(R.id.checkBox)).check(matches(x.isInRegister() ? isChecked() : isNotChecked()));
        });
    }

    @After
    public void tearDown() {
        if (!"".equals(tmpFilePath)) {
            File file = new File(tmpFilePath);
            file.delete();
        }
        CsvRootDirectory csvRootDirectory = new CsvRootDirectory();
        File datamarsDir = new File(csvRootDirectory.getDirectory() + File.separator + RegisterInspectionHelper.USB_NAME);
        if (datamarsDir.exists()) {
            datamarsDir.delete();
        }
    }
}
