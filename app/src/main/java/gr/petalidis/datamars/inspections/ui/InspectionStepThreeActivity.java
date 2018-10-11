package gr.petalidis.datamars.inspections.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.SessionViewer;
import gr.petalidis.datamars.activities.StartActivity;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.InspectionService;
import gr.petalidis.datamars.rsglibrary.CsvRootDirectory;

public class InspectionStepThreeActivity extends AppCompatActivity {

    private Inspection inspection;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        InspectionService.save(dbHandler, inspection);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.success).setMessage(R.string.success);
        builder.show();
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);
    }

}
