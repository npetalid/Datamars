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

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.web.webdriver.Locator;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import gr.petalidis.datamars.activities.StartActivity;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.utilities.EspressoIdlingResource;
import gr.petalidis.datamars.rsglibrary.CsvRootDirectory;
import gr.petalidis.datamars.testUtils.RegisterInspectionDataSet;
import gr.petalidis.datamars.testUtils.RegisterInspectionHelper;

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
 * <p>
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

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<Object[]> arrayList = new ArrayList<>();
        arrayList.add(new String[]{"Dataset1.csv"}); //Basic check of calculations
        arrayList.add(new String[]{"Dataset2.csv"}); //Check different animals
        arrayList.add(new String[]{"Dataset3.csv"});  //Check two producers
        arrayList.add(new String[]{"Dataset4.csv"});  //Check calculation when single tags > 20%
        arrayList.add(new String[]{"Dataset5.csv"});  //Check calculation when only single tags
        arrayList.add(new String[]{"Dataset6.csv"});  //Check calculation when single tags = 20%

        return arrayList;
    }

    @Parameterized.Parameter // first data value (0) is default
    public /* NOT private */ String dataset;

    private RegisterInspectionHelper registerInspectionHelper;
    @Before
    public void setUp() throws IOException, ParseException {
        Moo.setTestProperties(InstrumentationRegistry.getInstrumentation().getContext());
        DbHandler dbHandler = new DbHandler(Moo.getAppContext());
        dbHandler.dropDatabase(Moo.getAppContext());
        registerInspectionHelper = RegisterInspectionDataSet.readData(dataset);
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

        IdlingRegistry.getInstance().register(EspressoIdlingResource.getCountingIdlingResource());


        registerInspectionHelper.getProducers().stream().sorted(Comparator.comparing(RegisterInspectionHelper.Producer::getIndex)).forEach(x -> {
            onView(withId(x.getNameId())).perform(typeText(x.getName()));
            onView(withId(x.getTinId())).perform(typeText(x.getTin()));

            onView(withId(x.getTagId())).perform(click());
            if (!x.getTag().isEmpty()) {
                onData(hasToString(x.getTag())).perform(click());
            }
        });
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getCountingIdlingResource());

        onView(withId(R.id.gotoStep3Screen)).perform(click());

        registerInspectionHelper.getTagEntries().forEach(x -> {

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
        registerInspectionHelper.getProducers().stream().sorted(Comparator.comparingInt(RegisterInspectionHelper.Producer::getIndex))
                .forEach(x -> {
                    onView(withId(R.id.noEarringTin)).check(matches(withText(containsString(x.getTin()))));
                    registerInspectionHelper.getConventionalEntries().stream().filter(y -> y.getTin().equalsIgnoreCase(x.getTin()))
                            .forEach(y ->
                                    onView(withId(y.getFieldId())).perform(typeText(y.getNumber())));

                    onView(withId(R.id.nextProducer)).perform(click());
                });
        onView(withId(R.id.gotoStep5Screen)).perform(click());

        registerInspectionHelper.getProducers().stream().sorted(Comparator.comparingInt(RegisterInspectionHelper.Producer::getIndex))
                .forEach(x -> {
                    onView(withId(R.id.conventionalTin)).check(matches(withText(containsString(x.getTin()))));
                    registerInspectionHelper.getNoTagEntries().stream().filter(y -> y.getTin().equalsIgnoreCase(x.getTin())).forEach(y ->
                            onView(withId(y.getFieldId())).perform(typeText(y.getNumber()))
                    );
                    onView(withId(R.id.nextProducer2)).perform(click());
                });
        onView(withId(R.id.saveInspectionButon)).perform(click());


        registerInspectionHelper.getResultEntries()
                .forEach(x ->
                        onWebView().withElement(findElement(Locator.ID, x.getId()))
                                .check(webMatches(getText(), containsString(x.getNumber())))
                );


        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.inspectionsButton))
                .perform(click());

        onView(withText("testDevice")).perform(click());

        onData(hasToString(  registerInspectionHelper.getProducers().stream().filter(x->x.getIndex()==0).findAny().map(RegisterInspectionHelper.Producer::getName).orElse(""))).perform(click());
        registerInspectionHelper.getProducers().stream().sorted(Comparator.comparingInt(RegisterInspectionHelper.Producer::getIndex))
                .forEach(x -> {
                    onView(withId(R.id.viewProducerTin)).check(matches(withText(containsString(x.getTin()))));

                    onView(withId(R.id.total)).check(matches(withText(containsString(RegisterInspectionHelper.ResultEntryGetter
                            .getTotalFor(registerInspectionHelper.getResultEntries(), x.getTin())))));

                    onView(withId(R.id.noTag)).check(matches(withText(containsString(RegisterInspectionHelper.ResultEntryGetter
                            .getNoTagFor(registerInspectionHelper.getResultEntries(), x.getTin())))));

                    onView(withId(R.id.noTagUnder6)).check(matches(withText(containsString(RegisterInspectionHelper.ResultEntryGetter
                            .getNoTagUnder6For(registerInspectionHelper.getResultEntries(), x.getTin())))));

                    onView(withId(R.id.noElectronicTag)).check(matches(withText(containsString(RegisterInspectionHelper.ResultEntryGetter
                            .getNoElectronicTagFor(registerInspectionHelper.getResultEntries(), x.getTin())))));

                    onView(withId(R.id.singleTag)).check(matches(withText(containsString(RegisterInspectionHelper.ResultEntryGetter
                            .getSingleTagFor(registerInspectionHelper.getResultEntries(), x.getTin())))));
                    onView(withId(R.id.countedButNotInRegistry)).check(matches(withText(containsString(RegisterInspectionHelper.ResultEntryGetter
                            .getCountedButNotInRegistryFor(registerInspectionHelper.getResultEntries(), x.getTin())))));

                    onView(withId(R.id.sheepTotalValue)).check(matches(withText(containsString(RegisterInspectionHelper.ResultEntryGetter
                            .getSheepFor(registerInspectionHelper.getResultEntries(), x.getTin())))));

                    onView(withId(R.id.goatTotalValue)).check(matches(withText(containsString(RegisterInspectionHelper.ResultEntryGetter
                            .getGoatFor(registerInspectionHelper.getResultEntries(), x.getTin())))));

                    onView(withId(R.id.ramTotalValue)).check(matches(withText(containsString(RegisterInspectionHelper.ResultEntryGetter
                            .getSelectableRamHeGoatFor(registerInspectionHelper.getResultEntries(), x.getTin())))));
                    onView(withId(R.id.lambsTotalValue)).check(matches(withText(containsString(RegisterInspectionHelper.ResultEntryGetter
                            .getSelectableKidLambFor(registerInspectionHelper.getResultEntries(), x.getTin())))));
                    onView(withId(R.id.horseTotalValue)).check(matches(withText(containsString(RegisterInspectionHelper.ResultEntryGetter
                            .getSelectableHorseFor(registerInspectionHelper.getResultEntries(), x.getTin())))));

                    onView(withId(R.id.nextProducer3)).perform(click());
                });

    }

    @After
    public void tearDown() throws Exception {
        if (!"".equals(registerInspectionHelper.getTestFilePath())) {
            File file = new File(registerInspectionHelper.getTestFilePath());
            if (!file.delete()) {
                throw new Exception("Unable to delete file " + registerInspectionHelper.getTestFilePath());
            }
        }
        CsvRootDirectory csvRootDirectory = new CsvRootDirectory();
        File datamarsDir = new File(csvRootDirectory.getDirectory() + File.separator + RegisterInspectionHelper.USB_NAME);
        if (datamarsDir.exists()) {
            if (!datamarsDir.delete()) {
                throw new Exception("Unable to delete directory " + datamarsDir.getAbsoluteFile());
            }
        }
    }
}
