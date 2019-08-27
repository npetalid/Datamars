package gr.petalidis.datamars.inspections.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.repository.DbHandler;

public class InspectionStepFourActivity extends AppCompatActivity {

    private Inspection inspection;
    private DbHandler dbHandler;
    private Context mContext;
    private Map<Integer,Integer> valuesToLabels = new HashMap<>();

    private int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mContext = this;
        super.onCreate(savedInstanceState);
        // recovering the instance state
        if (savedInstanceState != null) {
            inspection = (Inspection) savedInstanceState.getSerializable("inspection");
        } else {
            inspection = (Inspection) getIntent().getExtras().getSerializable("inspection");
        }

        dbHandler = new DbHandler(this.getApplicationContext());
        setContentView(R.layout.activity_inspection_step_four);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        valuesToLabels.put(R.id.noEarringGoatValue,R.id.noEarringGoatLabel);
        valuesToLabels.put(R.id.noEarringHegoatValue,R.id.noEarringHegoatLabel);
        valuesToLabels.put(R.id.noEarringHorseValue,R.id.noEarringHorseLabel);
        valuesToLabels.put(R.id.noEarringSheepValue,R.id.noEarringSheepLabel);
        valuesToLabels.put(R.id.noEarringRamValue,R.id.noEarringRamLabel);
        valuesToLabels.put(R.id.noEarringLambValue,R.id.noEarringLambLabel);
        valuesToLabels.put(R.id.noEarringKidValue,R.id.noEarringKidLabel);


        valuesToLabels.keySet().forEach(key->
        {
            TextView label = findViewById(valuesToLabels.get(key));
            CharSequence animal = label.getText();

            TextView value = findViewById(key);
            value.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        inspection.setConventionalInRegisterFor(
                                inspection.getProducersWithNoDummy().get(index), animal.toString(),
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

        valuesToLabels.keySet().forEach(key->
        {
            TextView label = findViewById(valuesToLabels.get(key));
            CharSequence animal = label.getText();

            TextView value = findViewById(key);
            value.setText(inspection.getConventionalInRegisterFor(inspectee,animal.toString())+"");
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
