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
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import gr.petalidis.datamars.activities.StartActivity;
import gr.petalidis.datamars.rsglibrary.CsvRootDirectory;
import gr.petalidis.datamars.testUtils.DataEntry;
import gr.petalidis.datamars.testUtils.DataSet;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
 * Α = (Προβατίνες) με ηλεκτρονικά ενώτια στο μητρώο και κανένα σχόλιο
 * Β = (Προβατίνες) με ηλεκτρονικό ενώτιο καταχωρημένο ως ΘΑΝ
 * Γ = (Προβατίνες) με ηλεκτρονικό ενώτιο καταχωρημένο ως ΣΦΓ
 * Δ = (Προβατίνες) με ηλεκτρονικό ενώτιο καταχωρημένο ως ΠΩΛ
 * Ε = (Προβατίνες) με ηλεκτρονικό ενώτιο καταχωρημένο ως ΔΠΛ
 * ΣΤ = (Προβατίνες) με ηλεκτρονικό ενώτιο καταχωρημένο ως ΛΘΣ
 * Ζ = (Προβατίνες) με ηλεκτρονικό ενώτιο καταχωρημένο ως ΜΟΝ
 * Η =  ΙΠΠΟΕΙΔΕΣ
 * Θ = (Προβατίνες) συμβατικό ενώτιο εντός ιστορικής περιόδου
 * Ι = (Προβατίνες) με συμβατικό εκτός μητρώου
 * ΙΑ = (Προβατίνες) με μονό συμβατικό ενώτιο εντός ιστορικής περιόδου
 * ΙΒ = (Προβατίνες) με συμβατικό ενώτιο εκτός ιστορικής περιόδου
 * ΙΓ = ΖΩΑ χωρίς ενώτιο
 * ΙΔ = ΖΩΑ κατω των 6 μηνών
 *
 * Καταμετρηθέντα = Α+Β+Γ+Δ+Ε+Ζ+Θ+Ι+ΙΑ+ΙΒ+ΙΓ+ΙΔ (Όχι τα ιπποειδή)
 * Ζώα χωρίς σήμανση = Ε+ΙΓ
 * Ζώα χωρίς σήμανση < 6 μηνών = ΙΔ
 * Ζώα χωρίς ηλ. σήμανση = ΙΒ+ΙΓ
 * Ζώα με ένα μονό ενώτιο = ΙΑ+Ζ
 * Ζώα με σήμανση που δεν αναγράφονται στο μητρώο  = Β+Γ+Δ+Ι
 * ΕΠΙΛΕΞΙΜΕΣ ΠΡΟΒΑΤΙΝΕΣ = Α+Ζ+Θ+ΙΑ (Ζ και ΙΑ μόνο αν Ζ+ΙΑ/(Α+Ζ+Θ+ΙΑ)<=0.2
 */
@RunWith(Parameterized.class)
//@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterInspectionTest {

    @Rule
    public ActivityTestRule<StartActivity> activityRule =
            new ActivityTestRule<>(StartActivity.class);
    private String tmpFilePath = "";

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws Exception {

        List<Object[]> arrayList = new ArrayList<>();
        // arrayList.add(DataSet.readData("Dataset1.csv").toArray());
        // arrayList.add(DataSet.readData("Dataset2.csv").toArray());
        arrayList.add(DataSet.readData("Dataset3.csv").toArray());

        return arrayList;
    }

    @Parameterized.Parameter // first data value (0) is default
    public /* NOT private */ List<DataEntry.Producer> producers;

    @Parameterized.Parameter(1)
    public /* NOT private */ List<DataEntry.TagEntry> tagEntries;

    @Parameterized.Parameter(2)
    public /* NOT private */ List<DataEntry.ConventionalEntry> conventionalEntries;

    @Parameterized.Parameter(3)
    public /* NOT private */ List<DataEntry.ConventionalEntry> noTags;

    @Parameterized.Parameter(4)
    public /* NOT private */ List<DataEntry.ResultEntry> results;

    @Before
    public void setUp() throws Exception {

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

        boolean hasMoreThanOneProducers = producers.size() > 1;

        producers.stream().sorted(Comparator.comparing(DataEntry.Producer::getIndex)).forEach(x -> {
            onView(withId(x.getNameId())).perform(typeText(x.getName()));
            onView(withId(x.getTinId())).perform(typeText(x.getTin()));

            //if (hasMoreThanOneProducers) {
            onView(withId(x.getTagId())).perform(click());
            if (!x.getTag().isEmpty()) {
                onData(hasToString(x.getTag())).perform(click());
            }
            //}
        });

        onView(withId(R.id.gotoStep3Screen)).perform(click());

        tagEntries.forEach(x -> {

            if (x.isInRegister() == false) {
                //TODO: Do things when false
            }
            if (!x.getAnimal().equalsIgnoreCase("Προβατίνα")) {
                onData(hasEntryTag(x.getTag())).inAdapterView(withId(R.id.editItemsList))
                        .onChildView(withId(R.id.spinner)).perform(click());
                onData(hasToString(x.getAnimal())).perform(click());
            }
            if (!x.getComment().isEmpty()) {
                onData(hasEntryTag(x.getTag())).inAdapterView(withId(R.id.editItemsList))
                        .onChildView(withId(R.id.viewComments)).perform(click());
                onData(hasToString(x.getComment())).perform(click());
            }

        });

        onView(withId(R.id.addFindingsButton)).perform(click());
        producers.stream().sorted(Comparator.comparingInt(DataEntry.Producer::getIndex))
                .forEach(x -> {
                    onView(withId(R.id.noEarringTin)).check(matches(withText(containsString(x.getTin()))));
                    conventionalEntries.stream().filter(y -> y.getTin().equalsIgnoreCase(x.getTin()))
                            .forEach(y ->
                                    onView(withId(y.getFieldId())).perform(typeText(y.getNumber())));

                    onView(withId(R.id.nextProducer)).perform(click());
                });
        onView(withId(R.id.gotoStep5Screen)).perform(click());

        producers.stream().sorted(Comparator.comparingInt(DataEntry.Producer::getIndex))
                .forEach(x -> {
                    onView(withId(R.id.conventionalTin)).check(matches(withText(containsString(x.getTin()))));
                    noTags.stream().filter(y -> y.getTin().equalsIgnoreCase(x.getTin())).forEach(y ->
                            onView(withId(y.getFieldId())).perform(typeText(y.getNumber()))
                    );
                    onView(withId(R.id.nextProducer2)).perform(click());
                });
        onView(withId(R.id.saveInspectionButon)).perform(click());

//        producers.stream().sorted(Comparator.comparing(DataEntry.Producer::getIndex)).forEach(
//                producer ->
//
                    results//.stream().filter(x -> x.getId().contains(producer.getTin()))
                            .forEach(x ->
                                    onWebView().withElement(findElement(Locator.ID, x.getId()))
                                            .check(webMatches(getText(), containsString(x.getNumber())))
                            );
               // })

//        );
        //onData(allOf(is(instanceOf(Map.class))));

    }

    @After
    public void tearDown() {
        if (!"".equals(tmpFilePath)) {
            File file = new File(tmpFilePath);
            file.delete();
        }
        CsvRootDirectory csvRootDirectory = new CsvRootDirectory();
        File datamarsDir = new File(csvRootDirectory.getDirectory() + File.separator + DataEntry.USB_NAME);
        if (datamarsDir.exists()) {
            datamarsDir.delete();
        }

    }
}
