package gr.petalidis.datamars.inspections.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.service.RsgService;
import gr.petalidis.datamars.inspections.service.StubRsgService;
import gr.petalidis.datamars.rsglibrary.Rsg;
import gr.petalidis.datamars.rsglibrary.RsgReader;
import gr.petalidis.datamars.rsglibrary.RsgSessionFiles;

public class InspectionStepTwoActivity extends AppCompatActivity {
    Inspection inspection;
     String filename = "";

    Date inspectionDate;

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
        setContentView(R.layout.activity_inspection_step_two);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("inspection", inspection);
        outState.putString("rsgFilename",filename);
        outState.putSerializable("inspectionDate", inspectionDate);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    public void goToInspectionStepThreeActivity(View view) {
        Intent intent = new Intent(this, InspectionStepThreeActivity.class);
        Set<Rsg> rsgs = new HashSet<>();
        try {
            rsgs.addAll(RsgReader.readRsgFromTablet(filename));
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
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
}