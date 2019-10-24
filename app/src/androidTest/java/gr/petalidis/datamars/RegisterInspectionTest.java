/*
     Copyright 2017 Nikolaos Petalidis
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
 */


package gr.petalidis.datamars;

import androidx.test.espresso.web.webdriver.Locator;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import gr.petalidis.datamars.activities.StartActivity;
import gr.petalidis.datamars.inspections.domain.AnimalType;
import gr.petalidis.datamars.rsglibrary.CsvRootDirectory;
import gr.petalidis.datamars.rsglibrary.Rsg;
import gr.petalidis.datamars.rsglibrary.RsgExporter;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static androidx.test.espresso.web.sugar.Web.onWebView;
import static androidx.test.espresso.web.webdriver.DriverAtoms.findElement;
import static androidx.test.espresso.web.webdriver.DriverAtoms.getText;
import static gr.petalidis.datamars.EntryTagMatcher.hasEntryTag;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterInspectionTest {
    private final static String USB_NAME = "testDevice";
    private final static String TEST_FILE_NAME = "2018-02-22.csv";
    @Rule
    public ActivityTestRule<StartActivity> activityRule =
            new ActivityTestRule<>(StartActivity.class);
    private String tmpFilePath = "";

    @Before
    public void setUp() throws Exception {
        CsvRootDirectory csvRootDirectory = new CsvRootDirectory();
        File datamarsDir = new File(csvRootDirectory.getDirectory() + File.separator + USB_NAME);
        if (!datamarsDir.exists()) {
            boolean mkDir = datamarsDir.mkdir();
            if (!mkDir) {
                throw new IllegalStateException("Could not create device-differentiating directory");
            }
        }

        Rsg[] rsgsArray = {
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
                new Rsg("01234560300062037570081", "22022018", "093129")};
        Set<Rsg> rsgs = new HashSet<>(Arrays.asList(rsgsArray));
        tmpFilePath = RsgExporter.export(rsgs, datamarsDir.getAbsolutePath(), TEST_FILE_NAME);
    }

    @Test
    public void useAppContext() {

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

        onView(withId(R.id.producer1NameText)).perform(typeText("Pappas"));
        onView(withId(R.id.producer1TinText)).perform(typeText("127137474"));

        onView(withId(R.id.gotoStep3Screen)).perform(click());

        onData(hasEntryTag("062037570071")).inAdapterView(withId(R.id.editItemsList)).onChildView(withId(R.id.viewComments)).perform(click());
        onData(hasToString("ΘΑΝ")).perform(click());

        onData(hasEntryTag("062037570055")).inAdapterView(withId(R.id.editItemsList)).onChildView(withId(R.id.viewComments)).perform(click());
        onData(hasToString("ΣΦΓ")).perform(click());

        onData(hasEntryTag("062037570054")).inAdapterView(withId(R.id.editItemsList)).onChildView(withId(R.id.viewComments)).perform(click());
        onData(hasToString("ΠΩΛ")).perform(click());

        onData(hasEntryTag("062037570012")).inAdapterView(withId(R.id.editItemsList)).onChildView(withId(R.id.viewComments)).perform(click());
        onData(hasToString("ΔΠΛ")).perform(click());

        onData(hasEntryTag("062037570021")).inAdapterView(withId(R.id.editItemsList)).onChildView(withId(R.id.viewComments)).perform(click());
        onData(hasToString("ΛΘΣ")).perform(click());

        onData(hasEntryTag("062036290063")).inAdapterView(withId(R.id.editItemsList)).onChildView(withId(R.id.viewComments)).perform(click());
        onData(hasToString("ΜΟΝ")).perform(click());


        onView(withId(R.id.addFindingsButton)).perform(click());
        onView(withId(R.id.sheepLegalConventionalTag)).perform(typeText("1"));
        onView(withId(R.id.sheepConventionalOutOfRegistry)).perform(typeText("1"));
        onView(withId(R.id.sheepSingleConventionalTag)).perform(typeText("1"));
        onView(withId(R.id.sheepIllegalConventionalTag)).perform(typeText("1"));
        onView(withId(R.id.gotoStep5Screen)).perform(click());
        onView(withId(R.id.noTagOver6MonthValue)).perform(typeText("5"));
        onView(withId(R.id.noTagUnder6MonthValue)).perform(typeText("5"));
        onView(withId(R.id.saveInspectionButon)).perform(click());

        onWebView().withElement(findElement(Locator.ID, "127137474-total"))
                .check(webMatches(getText(), containsString("29")));

        onWebView().withElement(findElement(Locator.ID, "127137474-noTagUnder6"))
                .check(webMatches(getText(), containsString("5")));
        onWebView().withElement(findElement(Locator.ID, "127137474-noElectronicTag"))
                .check(webMatches(getText(), containsString("6")));
        onWebView().withElement(findElement(Locator.ID, "127137474-singleTag"))
                .check(webMatches(getText(), containsString("2")));
        onWebView().withElement(findElement(Locator.ID, "127137474-countedButNotInRegistry"))
                .check(webMatches(getText(), containsString("4")));
        onWebView().withElement(findElement(Locator.ID, "127137474-" + AnimalType.SHEEP_ANIMAL.getTitle()))
                .check(webMatches(getText(), containsString("13")));
        onWebView().withElement(findElement(Locator.ID, "127137474-Αμνοερίφια"))
                .check(webMatches(getText(), containsString("5")));
        onWebView().withElement(findElement(Locator.ID, "127137474-noΤag"))
                .check(webMatches(getText(), containsString("6")));


        //onData(allOf(is(instanceOf(Map.class))));

    }

    @After
    public void tearDown() {
        if (!"".equals(tmpFilePath)) {
            File file = new File(tmpFilePath);
            file.delete();
        }
        CsvRootDirectory csvRootDirectory = new CsvRootDirectory();
        File datamarsDir = new File(csvRootDirectory.getDirectory() + File.separator + USB_NAME);
        if (datamarsDir.exists()) {
            datamarsDir.delete();
        }

    }
}
