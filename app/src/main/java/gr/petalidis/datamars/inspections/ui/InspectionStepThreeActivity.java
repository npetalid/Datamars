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
import android.widget.Spinner;

import java.util.Comparator;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.activities.StartActivity;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.InspectionService;
import gr.petalidis.datamars.inspections.utilities.WGS84Converter;

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

        final Button checkButton = findViewById(R.id.ischecked);

        checkButton.setOnLongClickListener(this::reverseSelection);

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

    public void goToInspectionStepFourActivity(View view) {

            Intent intent = new Intent(this, InspectionStepFourActivity.class);

            intent.putExtra("inspection", inspection);

            startActivity(intent);
        }
    public void goToStepFourActivity(View view) {
        Intent intent = new Intent(this, InspectionStepFourActivity.class);

        intent.putExtra("inspection", inspection);

        startActivity(intent);

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

    public boolean reverseSelection(View view) {
        ListView gridView = (ListView) findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.revertRegister();
        inspectionStepThreeAdapter.notifyDataSetChanged();
        gridView.setAdapter(inspectionStepThreeAdapter);
        return true;
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
