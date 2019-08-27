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
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.activities.StartActivity;
import gr.petalidis.datamars.inspections.domain.Animals;
import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.InspectionService;

public class InspectionStepFiveActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_inspection_step_five);

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
                    if (!editable.toString().isEmpty()) {
                        inspection.setConventionalTotalFor(inspection.getProducers().get(index),
                                animal.toString(),
                                Integer.parseInt(editable.toString()));
                    }
                }
            });
        });

        updateButtons();

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


        String msg = "Βρέθηκαν: \n";

        String totalResult = inspection.getProducersWithNoDummy().stream().map(
                inspectee ->
                        "Για τον παραγωγό " + inspectee.getName() + " \n" +
                                inspection.getCount(inspectee.getTin()) + " καταμετρηθέντα ζώα και " +
                                inspection.getInRegisterCount(inspectee.getTin()) + " αναγραφόμενα στο μητρώο\n" +
                                inspection.getUniqueTag(inspectee.getTin()) + " ζώα > 6 μηνών με μόνο ένα μέσο σήμανσης\n" +
                                inspection.getCount(inspectee.getTin(), Animals.LAMB_ANIMAL) + " καταμετρηθέντα αρνιά και " +
                                inspection.getInRegisterCount(inspectee.getTin(), Animals.LAMB_ANIMAL) + " καταγεγραμμένα στο μητρώο\n" +
                                inspection.getCount(inspectee.getTin(), Animals.SHEEP_ANIMAL) + " καταμετρηθέντα πρόβατα και " +
                                inspection.getInRegisterCount(inspectee.getTin(), Animals.SHEEP_ANIMAL) + " καταγεγραμμένα στο μητρώο\n" +
                                inspection.getCount(inspectee.getTin(), Animals.KID_ANIMAL) + " καταμετρηθέντα ερίφια και " +
                                inspection.getInRegisterCount(inspectee.getTin(), Animals.KID_ANIMAL) + " καταγεγραμμένα στο μητρώο\n" +
                                inspection.getCount(inspectee.getTin(), Animals.GOAT_ANIMAL) + " καταμετρηθέντα γίδια και " +
                                inspection.getInRegisterCount(inspectee.getTin(), Animals.GOAT_ANIMAL) + " καταγεγραμμένα στο μητρώο\n" +
                                inspection.getCount(inspectee.getTin(), Animals.RAM_ANIMAL) + " καταμετρηθέντα κριάρια και" +
                                inspection.getInRegisterCount(inspectee.getTin(), Animals.RAM_ANIMAL) + " καταγεγραμμένα στο μητρώο\n" +
                                inspection.getCount(inspectee.getTin(), Animals.HEGOAT_ANIMAL) + " καταμετρηθέντες τράγοι και " +
                                inspection.getInRegisterCount(inspectee.getTin(), Animals.HEGOAT_ANIMAL) + " καταγεγραμμένοι στο μητρώο\n" +
                                inspection.getCount(inspectee.getTin(), Animals.HORSE_ANIMAL) + " καταμετρηθέντα ιπποειδή και " +
                                inspection.getInRegisterCount(inspectee.getTin(), Animals.HORSE_ANIMAL) + " καταγεγραμμένα στο μητρώο\n"
        ).collect(Collectors.joining("\n"));

        msg = msg + totalResult +   "Να γίνει αποθήκευση;";
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
        TextView conventionalTin = findViewById(R.id.conventionalTin);
        Inspectee inspectee = inspection.getProducersWithNoDummy().get(index);

        conventionalTin.setText(inspectee.getTin() + " " +
                inspectee.getName());

        valuesToLabels.keySet().forEach(key->
        {
            TextView label = findViewById(valuesToLabels.get(key));
            CharSequence animal = label.getText();

            TextView value = findViewById(key);
            value.setText(inspection.getConventionalTotalFor(inspectee,animal.toString())+"");
        });
        ImageButton previousProducerButton = findViewById(R.id.previousProducer2);
        ImageButton nextProducerButton = findViewById(R.id.nextProducer2);

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
