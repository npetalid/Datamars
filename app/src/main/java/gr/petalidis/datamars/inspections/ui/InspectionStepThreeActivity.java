package gr.petalidis.datamars.inspections.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Comparator;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.activities.StartActivity;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.InspectionService;

public class InspectionStepThreeActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_inspection_step_three);

        final ListView listview = (ListView) findViewById(R.id.editItemsList);

        final InspectionStepThreeAdapter adapter = new InspectionStepThreeAdapter(this,
                android.R.layout.simple_list_item_1, inspection.getProducers(), inspection.getEntries());
        // Add a header to the ListView
        //LayoutInflater inflater = getLayoutInflater();

//        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.listitemsheader, listview, false);
//        listview.addHeaderView(header);
//
//        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.listitemsfooter, listview, false);
//        listview.addFooterView(footer);

        listview.setAdapter(adapter);

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
                "Για τον παραγωγό " + inspection.getProducer1Name() +
                inspection.getValidEntriesCount("\n" + inspection.getProducer1Tin()) + " ζώα\t" +
                inspection.getInRegisterCount(inspection.getProducer1Tin()) + " στο μητρώο\n" +
                inspection.getLambCount(inspection.getProducer1Tin()) + " αρνιά\t" +
                inspection.getSheepCount(inspection.getProducer1Tin()) + " προβατίνες\n" +
                inspection.getKidCount(inspection.getProducer1Tin()) + " ερίφια\t" +
                inspection.getGoatCount(inspection.getProducer1Tin()) + " γίδες\n" +
                inspection.getRamCount(inspection.getProducer1Tin()) + " κριάρια\t" +
                inspection.getHeGoatCount(inspection.getProducer1Tin()) + " τράγοι\n";
                if (!inspection.getProducer2Tin().isEmpty()) {
                   msg = msg + "\nΓια τον παραγωγό " + inspection.getProducer2Name() +
                            inspection.getValidEntriesCount("\n" +inspection.getProducer2Tin()) + " ζώα\t" +
                            inspection.getInRegisterCount(inspection.getProducer2Tin()) + " στο μητρώο\n" +
                            inspection.getLambCount(inspection.getProducer2Tin()) + " αρνιά\t" +
                            inspection.getSheepCount(inspection.getProducer2Tin()) + " προβατίνες\n" +
                            inspection.getKidCount(inspection.getProducer2Tin()) + " ερίφια\t" +
                            inspection.getGoatCount(inspection.getProducer2Tin()) + " γίδες\n" +
                            inspection.getRamCount(inspection.getProducer2Tin()) + " κριάρια\t" +
                            inspection.getHeGoatCount(inspection.getProducer2Tin()) + " τράγοι\n";
                }
         if (!inspection.getProducer3Tin().isEmpty()) {
            msg = msg + "\nΓια τον παραγωγό " + inspection.getProducer3Name() +
                    inspection.getValidEntriesCount("\n" +inspection.getProducer3Tin()) + " ζώα\t" +
                    inspection.getInRegisterCount(inspection.getProducer3Tin()) + " στο μητρώο\n" +
                    inspection.getLambCount(inspection.getProducer3Tin()) + " αρνιά\t" +
                    inspection.getSheepCount(inspection.getProducer3Tin()) + " προβατίνες\n" +
                    inspection.getKidCount(inspection.getProducer3Tin()) + " ερίφια\t" +
                    inspection.getGoatCount(inspection.getProducer3Tin()) + " γίδες\n" +
                    inspection.getRamCount(inspection.getProducer3Tin()) + " κριάρια\t" +
                    inspection.getHeGoatCount(inspection.getProducer3Tin()) + " τράγοι\n";
        }
        if (!inspection.getProducer4Tin().isEmpty()) {
            msg = msg + "\nΓια τον παραγωγό " + inspection.getProducer4Name() +
                    inspection.getValidEntriesCount("\n" +inspection.getProducer4Tin()) + " ζώα\t" +
                    inspection.getInRegisterCount(inspection.getProducer4Tin()) + " στο μητρώο\n" +
                    inspection.getLambCount(inspection.getProducer4Tin()) + " αρνιά\t" +
                    inspection.getSheepCount(inspection.getProducer4Tin()) + " προβατίνες\n" +
                    inspection.getKidCount(inspection.getProducer4Tin()) + " ερίφια\t" +
                    inspection.getGoatCount(inspection.getProducer4Tin()) + " γίδες\n" +
                    inspection.getRamCount(inspection.getProducer4Tin()) + " κριάρια\t" +
                    inspection.getHeGoatCount(inspection.getProducer4Tin()) + " τράγοι\n";
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

    public void sortGridEntriesByTag(View view) {
        ListView gridView = (ListView) findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getTag));
        Button button = (Button)findViewById(R.id.tag);
        button.setOnClickListener(v ->
        { inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getTag).reversed());
            button.setOnClickListener(y->sortGridEntriesByTag(y));
        });
    }

    public void sortGridEntriesByTime(View view) {
        ListView gridView = (ListView) findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getTagDate));
        Button button = (Button)findViewById(R.id.age);
        button.setOnClickListener(v ->
        { inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getTagDate).reversed());
            button.setOnClickListener(y->sortGridEntriesByTime(y));
        });
    }

    public void sortGridEntriesByAnimalType(View view) {
        ListView gridView = (ListView) findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getAnimalType));
        Button button = (Button)findViewById(R.id.type);
        button.setOnClickListener(v ->
        { inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getAnimalType).reversed());
            button.setOnClickListener(y->sortGridEntriesByAnimalType(y));
        });
    }

    public void sortGridEntriesByProducer(View view) {
        ListView gridView = (ListView) findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getProducer));
        Button button = (Button)findViewById(R.id.names);
        button.setOnClickListener(v ->
        { inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getProducer).reversed());
            button.setOnClickListener(y->sortGridEntriesByProducer(y));
        });
    }
    public void sortGridEntriesByIsInRegister(View view) {
        ListView gridView = (ListView) findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::isInRegister));
        Button button = (Button)findViewById(R.id.ischecked);
        button.setOnClickListener(v ->
        { inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::isInRegister).reversed());
            button.setOnClickListener(y->sortGridEntriesByIsInRegister(y));
        });
    }
    public void sortGridEntriesByComments(View view) {
        ListView gridView = (ListView) findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::getComment));
        Button button = (Button)findViewById(R.id.viewCommentsButton);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::getComment).reversed());
            button.setOnClickListener(y->sortGridEntriesByComments(y));
        });
    }
}
