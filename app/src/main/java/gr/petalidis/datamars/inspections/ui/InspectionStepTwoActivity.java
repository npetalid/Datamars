package gr.petalidis.datamars.inspections.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.validators.TinValidator;
import gr.petalidis.datamars.rsglibrary.Rsg;
import gr.petalidis.datamars.rsglibrary.RsgReader;

public class InspectionStepTwoActivity extends AppCompatActivity {
    private Inspection inspection;
    private String filename = "";

    private Date inspectionDate;
    private Set<Rsg> rsgs = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // recovering the instance state
        if (savedInstanceState != null) {
            inspection = (Inspection) savedInstanceState.getSerializable("inspection");
            inspectionDate = (Date) savedInstanceState.getSerializable("inspectionDate");
            filename = savedInstanceState.getString("rsgFilename");
        } else {
            inspectionDate = (Date) getIntent().getExtras().getSerializable("inspectionDate");
            filename = getIntent().getExtras().getString("rsgFilename");
        }
        try {
            rsgs.addAll(RsgReader.readRsgFromTablet(filename));
            setContentView(R.layout.activity_inspection_step_two);
            setAnimalCount(rsgs.size());
            if (inspection != null) {
                setProducer1Name();
                setProducer2Name();
                setProducer3Name();
                setProducer4Name();
                setProducer1Tin();
                setProducer2Tin();
                setProducer3Tin();
                setProducer4Tin();
            }
            EditText tin = (EditText) findViewById(R.id.producer1TinText);
            tin.addTextChangedListener(new TinWatcher(new TinValidator(),tin));
            tin = (EditText) findViewById(R.id.producer2TinText);
            tin.addTextChangedListener(new TinWatcher(new TinValidator(),tin));
            tin = (EditText) findViewById(R.id.producer3TinText);
            tin.addTextChangedListener(new TinWatcher(new TinValidator(),tin));
            tin = (EditText) findViewById(R.id.producer4TinText);
            tin.addTextChangedListener(new TinWatcher(new TinValidator(),tin));

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("inspection", inspection);
        outState.putString("rsgFilename", filename);
        outState.putSerializable("inspectionDate", inspectionDate);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    private boolean formHasErrors()
    {
        boolean hasErrors = false;
        TinValidator validator = new TinValidator();
        EditText tin = (EditText) findViewById(R.id.producer1TinText);
        String tinString = tin.getText().toString();
        EditText producer1Name = (EditText) findViewById(R.id.producer1NameText);
        String producer1NameString = producer1Name.getText().toString();

        EditText tin2 = (EditText) findViewById(R.id.producer2TinText);
        String tinString2 = tin2.getText().toString();
        EditText tin3 = (EditText) findViewById(R.id.producer3TinText);
        String tinString3 = tin3.getText().toString();
        EditText tin4 = (EditText) findViewById(R.id.producer4TinText);
        String tinString4 = tin4.getText().toString();

        if (!validator.isValid(tinString)) {
            hasErrors = true;
            tin.setError("Το ΑΦΜ δεν είναι σωστό");
        }
        if (producer1NameString.length()<2) {
            hasErrors = true;
            producer1Name.setError("Υποχρεωτικό πεδίο");
        }

        if (tinString2.length()>0 && !validator.isValid(tinString2)) {
            hasErrors = true;
            tin2.setError("Το ΑΦΜ δεν είναι σωστό");
        }

        if (tinString3.length()>0 && !validator.isValid(tinString2)) {
            hasErrors = true;
            tin3.setError("Το ΑΦΜ δεν είναι σωστό");
        }

        if (tinString4.length()>0 && !validator.isValid(tinString2)) {
            hasErrors = true;
            tin4.setError("Το ΑΦΜ δεν είναι σωστό");
        }

        return hasErrors;

    }
    public void goToInspectionStepThreeActivity(View view) {

        if (!formHasErrors()) {
            Intent intent = new Intent(this, InspectionStepThreeActivity.class);

            inspection = new Inspection(inspectionDate, getProducer1Tin(), getProducer1Name());

            inspection.setProducer2Name(getProducer2Name());
            inspection.setProducer2Tin(getProducer2Tin());

            inspection.setProducer3Name(getProducer3Name());
            inspection.setProducer3Tin(getProducer3Tin());

            inspection.setProducer4Name(getProducer4Name());
            inspection.setProducer4Tin(getProducer4Tin());
            inspection.initEntries(rsgs);

            intent.putExtra("inspection", inspection);

            startActivity(intent);
        }


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

    private String getProducer1Name() {
        EditText name = (EditText) findViewById(R.id.producer1NameText);
        return name.getText().toString().trim();
    }

    private String getProducer1Tin() {
        EditText tin = (EditText) findViewById(R.id.producer1TinText);
        return tin.getText().toString().trim();
    }

    private String getProducer2Name() {
        EditText name = (EditText) findViewById(R.id.producer2NameText);
        return name.getText().toString().trim();
    }

    private String getProducer2Tin() {
        EditText tin = (EditText) findViewById(R.id.producer2TinText);
        return tin.getText().toString().trim();
    }

    private String getProducer3Name() {
        EditText name = (EditText) findViewById(R.id.producer3NameText);
        return name.getText().toString().trim();
    }

    private String getProducer3Tin() {
        EditText tin = (EditText) findViewById(R.id.producer3TinText);
        return tin.getText().toString().trim();
    }

    private String getProducer4Name() {
        EditText name = (EditText) findViewById(R.id.producer4NameText);
        return name.getText().toString().trim();
    }

    private String getProducer4Tin() {
        EditText tin = (EditText) findViewById(R.id.producer4TinText);
        return tin.getText().toString().trim();
    }

    private void setProducer1Name() {
        EditText name = (EditText) findViewById(R.id.producer1NameText);
        if (inspection != null) name.setText(inspection.getProducer1Name());
    }

    private void setProducer1Tin() {
        EditText tin = (EditText) findViewById(R.id.producer1TinText);
        if (inspection != null) tin.setText(inspection.getProducer1Tin());
    }

    private void setProducer2Name() {
        EditText name = (EditText) findViewById(R.id.producer2NameText);
        if (inspection != null) name.setText(inspection.getProducer2Name());
    }

    private void setProducer2Tin() {
        EditText tin = (EditText) findViewById(R.id.producer2TinText);
        if (inspection != null) tin.setText(inspection.getProducer2Tin());
    }

    private void setProducer3Name() {
        EditText name = (EditText) findViewById(R.id.producer3NameText);
        if (inspection != null) name.setText(inspection.getProducer3Name());
    }

    private void setProducer3Tin() {
        EditText tin = (EditText) findViewById(R.id.producer3TinText);
        if (inspection != null) tin.setText(inspection.getProducer3Tin());
    }

    private void setProducer4Name() {
        EditText name = (EditText) findViewById(R.id.producer4NameText);
        if (inspection != null) name.setText(inspection.getProducer4Name());
    }

    private void setProducer4Tin() {
        EditText tin = (EditText) findViewById(R.id.producer4TinText);
        if (inspection != null) tin.setText(inspection.getProducer4Tin());
    }

    private void setAnimalCount(int value) {
        TextView animalCountValue = (TextView) findViewById(R.id.animalCountValue);
        animalCountValue.setText(value + "");
    }
}