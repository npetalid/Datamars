package gr.petalidis.datamars.inspections.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.util.EnumSet;

import gr.petalidis.datamars.Moo;
import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.repository.DbHandler;

public class InspectionStepFourActivity extends AppCompatActivity {

    private enum ConventionalEntryTag {
        SHEEP_ENTRY(R.string.sheepAnimal,R.id.sheepLegalConventionalTag,R.id.sheepConventionalOutOfRegistry,R.id.sheepSingleConventionalTag,R.id.sheepIllegalConventionalTag),
        GOAT_ENTRY(R.string.goatAnimal,R.id.goatLegalConventionalTag,R.id.goatConventionalOutOfRegistry,R.id.goatSingleConventionalTag,R.id.goatIllegalConventionalTag),
        HEGOAT_ENTRY(R.string.heGoatAnimal,R.id.heGoatLegalConventionalTag,R.id.heGoatConventionalOutOfRegistry,R.id.heGoatSingleConventionalTag,R.id.heGoatIllegalConventionalTag),
        RAM_ENTRY(R.string.ramAnimal,R.id.ramLegalConventionalTag,R.id.ramConventionalOutOfRegistry,R.id.ramSingleConventionalTag,R.id.ramIllegalConventionalTag);

         final String animal;
         final int conventionalTag;
         final int outOfRegistryTag;
         final int singleTag;
         final int illegalTag;

         ConventionalEntryTag(int animalStringId,int conventionalTag, int outOfRegistryTag, int singleTag, int illegalTag) {
             this.conventionalTag = conventionalTag;
             this.singleTag = singleTag;
             this.illegalTag = illegalTag;
             this.animal = Moo.getAppContext().getResources().getString(animalStringId);
             this.outOfRegistryTag = outOfRegistryTag;
        }

        public String getAnimal()
        {
            return animal;
        }

        public int getConventionalTag() {
            return conventionalTag;
        }

        public int getSingleTag() {
            return singleTag;
        }

        public int getIllegalTag() {
            return illegalTag;
        }
    }
    private Inspection inspection;

    private int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context mContext = this;
        super.onCreate(savedInstanceState);
        // recovering the instance state
        if (savedInstanceState != null) {
            inspection = (Inspection) savedInstanceState.getSerializable("inspection");
        } else {
            inspection = (Inspection) getIntent().getExtras().getSerializable("inspection");
        }

        DbHandler dbHandler = new DbHandler(this.getApplicationContext());
        setContentView(R.layout.activity_inspection_step_four);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EnumSet.allOf(ConventionalEntryTag.class).forEach(entryTag-> {

            TextView conventionalTagValue = findViewById(entryTag.conventionalTag);
            conventionalTagValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        inspection.setLegalConventionalTag(
                                inspection.getProducersWithNoDummy().get(index), entryTag.animal,
                                Integer.parseInt(editable.toString()));
                    }
                }
            });

            TextView singleTagValue = findViewById(entryTag.singleTag);
            singleTagValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        inspection.setSingleConventionalTag(
                                inspection.getProducersWithNoDummy().get(index), entryTag.animal,
                                Integer.parseInt(editable.toString()));
                    }
                }
            });
            TextView outOfRegistryValue = findViewById(entryTag.outOfRegistryTag);
            outOfRegistryValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        inspection.setConventionalOutOfRegistry(
                                inspection.getProducersWithNoDummy().get(index), entryTag.animal,
                                Integer.parseInt(editable.toString()));
                    }
                }
            });
            TextView conventionalIllegalTagValue = findViewById(entryTag.illegalTag);

            conventionalIllegalTagValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        inspection.setIllegalConventionalTag(
                                inspection.getProducersWithNoDummy().get(index), entryTag.animal,
                                Integer.parseInt(editable.toString()));
                    }
                }
            });
        });
       updateButtons();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("inspection", inspection);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToInspectionStepFiveActivity(View view) {

        Intent intent = new Intent(this, InspectionStepFiveActivity.class);

        intent.putExtra("inspection", inspection);

        startActivity(intent);
    }
    public void goToPreviousProducer(View view) {
        if (index>0) {
            index--;
        }
        updateButtons();
    }

    public void goToNextProducer(View view) {
        if (index<inspection.getProducersWithNoDummy().size()-1) {
            index++;
        }

        updateButtons();
    }

    private void updateButtons()
    {
        TextView noEarringTin = findViewById(R.id.noEarringTin);
        Inspectee inspectee = inspection.getProducersWithNoDummy().get(index);

        noEarringTin.setText(inspectee.getTin() + " " +
                inspectee.getName());

        EnumSet.allOf(ConventionalEntryTag.class).forEach(conventionalEntryTag ->
        {
            TextView conventionalTag = findViewById(conventionalEntryTag.conventionalTag);
            conventionalTag.setText(inspection.getLegalConventionalTagFor(inspectee,conventionalEntryTag.animal)+"");
            TextView singleTag = findViewById(conventionalEntryTag.singleTag);
            singleTag.setText(inspection.getSingleConventionalTagFor(inspectee,conventionalEntryTag.animal)+"");
            TextView illegalTag = findViewById(conventionalEntryTag.illegalTag);
            illegalTag.setText(inspection.getIllegalConventionalTagFor(inspectee,conventionalEntryTag.animal)+"");
            TextView outOfRegistryTag = findViewById(conventionalEntryTag.outOfRegistryTag);
            outOfRegistryTag.setText(inspection.getOutOfRegistryTagFor(inspectee,conventionalEntryTag.animal)+"");
        });
        ImageButton previousProducerButton = findViewById(R.id.previousProducer);
        ImageButton nextProducerButton = findViewById(R.id.nextProducer);

        if (inspection.getProducersWithNoDummy().isEmpty() || inspection.getProducersWithNoDummy().size()==1) {
            previousProducerButton.setClickable(false);
            nextProducerButton.setClickable(false);
        } else {
            if (index == 0) {
                previousProducerButton.setClickable(false);
                nextProducerButton.setClickable(true);
            } else if (index == inspection.getProducersWithNoDummy().size() - 1) {
                nextProducerButton.setClickable(false);
                previousProducerButton.setClickable(true);
            } else {
                previousProducerButton.setClickable(true);
                nextProducerButton.setClickable(true);
            }
        }
    }
}
