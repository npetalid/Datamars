package gr.petalidis.datamars.inspections.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.activities.StartActivity;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.InspectionService;

public class InspectionStepFiveActivity extends AppCompatActivity {

    private Inspection inspection;
    private DbHandler dbHandler;
    private Context mContext;
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
        setContentView(R.layout.activity_inspection_step_five);

        Map<Integer,Integer> valuesToLabels = new HashMap<>();
        valuesToLabels.put(R.id.conventionalEarringGoatValue,R.id.conventionalEarringGoatLabel);
        valuesToLabels.put(R.id.conventionalEarringHegoatValue,R.id.conventionalEarringHegoatLabel);
        valuesToLabels.put(R.id.conventionalEarringHorseValue,R.id.conventionalEarringHegoatLabel);
        valuesToLabels.put(R.id.conventionalEarringSheepValue,R.id.conventionalEarringSheepLabel);
        valuesToLabels.put(R.id.conventionalEarringRamValue,R.id.conventionalEarringRamLabel);
        valuesToLabels.put(R.id.conventionalEarringLambValue,R.id.conventionalEarringLambLabel);
        valuesToLabels.put(R.id.conventionalEarringKidValue,R.id.conventionalEarringKidLabel);


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
                    inspection.setConventionalTagFor(animal.toString(),Integer.parseInt(editable.toString()));
                }
            });
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    public void save(View view) {
        AlertDialog.Builder preSaveBuilder = new AlertDialog.Builder(this);

        String msg = "Βρέθηκαν: \n" +
                "Για τον παραγωγό " + inspection.getProducer1Name() + " \n" +
                inspection.getValidEntriesCount(inspection.getProducer1Tin()) + " ζώα\t" +
                inspection.getInRegisterCount(inspection.getProducer1Tin()) + " στο μητρώο\n" +
                inspection.getLambCount(inspection.getProducer1Tin()) + " αρνιά\t" +
                inspection.getSheepCount(inspection.getProducer1Tin()) + " προβατίνες\n" +
                inspection.getKidCount(inspection.getProducer1Tin()) + " ερίφια\t" +
                inspection.getGoatCount(inspection.getProducer1Tin()) + " γίδες\n" +
                inspection.getRamCount(inspection.getProducer1Tin()) + " κριάρια\t" +
                inspection.getHeGoatCount(inspection.getProducer1Tin()) + " τράγοι\n"+
                inspection.getHorseCount(inspection.getProducer1Tin()) + " άλογα\n";
        if (!inspection.getProducer2Tin().isEmpty()) {
                   msg = msg + "\nΓια τον παραγωγό " + inspection.getProducer2Name() + "\n" +
                            inspection.getValidEntriesCount(inspection.getProducer2Tin()) + " ζώα\t" +
                            inspection.getInRegisterCount(inspection.getProducer2Tin()) + " στο μητρώο\n" +
                            inspection.getLambCount(inspection.getProducer2Tin()) + " αρνιά\t" +
                            inspection.getSheepCount(inspection.getProducer2Tin()) + " προβατίνες\n" +
                            inspection.getKidCount(inspection.getProducer2Tin()) + " ερίφια\t" +
                            inspection.getGoatCount(inspection.getProducer2Tin()) + " γίδες\n" +
                            inspection.getRamCount(inspection.getProducer2Tin()) + " κριάρια\t" +
                            inspection.getHeGoatCount(inspection.getProducer2Tin()) + " τράγοι\n"+
                            inspection.getHorseCount(inspection.getProducer2Tin()) + " άλογα\n";
                }
         if (!inspection.getProducer3Tin().isEmpty()) {
            msg = msg + "\nΓια τον παραγωγό " + inspection.getProducer3Name()  + "\n" +
                    inspection.getValidEntriesCount(inspection.getProducer3Tin()) + " ζώα\t" +
                    inspection.getInRegisterCount(inspection.getProducer3Tin()) + " στο μητρώο\n" +
                    inspection.getLambCount(inspection.getProducer3Tin()) + " αρνιά\t" +
                    inspection.getSheepCount(inspection.getProducer3Tin()) + " προβατίνες\n" +
                    inspection.getKidCount(inspection.getProducer3Tin()) + " ερίφια\t" +
                    inspection.getGoatCount(inspection.getProducer3Tin()) + " γίδες\n" +
                    inspection.getRamCount(inspection.getProducer3Tin()) + " κριάρια\t" +
                    inspection.getHeGoatCount(inspection.getProducer3Tin()) + " τράγοι\n" +
                    inspection.getHorseCount(inspection.getProducer3Tin()) + " άλογα\n";
        }
        if (!inspection.getProducer4Tin().isEmpty()) {
            msg = msg + "\nΓια τον παραγωγό " + inspection.getProducer4Name()  + "\n" +
                    inspection.getValidEntriesCount(inspection.getProducer4Tin()) + " ζώα\t" +
                    inspection.getInRegisterCount(inspection.getProducer4Tin()) + " στο μητρώο\n" +
                    inspection.getLambCount(inspection.getProducer4Tin()) + " αρνιά\t" +
                    inspection.getSheepCount(inspection.getProducer4Tin()) + " προβατίνες\n" +
                    inspection.getKidCount(inspection.getProducer4Tin()) + " ερίφια\t" +
                    inspection.getGoatCount(inspection.getProducer4Tin()) + " γίδες\n" +
                    inspection.getRamCount(inspection.getProducer4Tin()) + " κριάρια\t" +
                    inspection.getHeGoatCount(inspection.getProducer4Tin()) + " τράγοι\n"+
                    inspection.getHorseCount(inspection.getProducer4Tin()) + " άλογα\n";
        }
             msg = msg+   "Να γίνει αποθήκευση;";
        preSaveBuilder.setTitle("Αποθήκευση ελέγχου").setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        InspectionService.save(dbHandler, inspection);
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(R.string.success).setMessage(R.string.success);
                        builder.show();
                        Intent intent = new Intent(mContext,StartActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        preSaveBuilder.show();


    }
}
