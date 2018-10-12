package gr.petalidis.datamars.inspections.ui;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Comparator;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.domain.Inspection;

public class InspectionViewActivity extends AppCompatActivity {

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Inspection inspection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            inspection = (Inspection) savedInstanceState.getSerializable("inspection");
        } else {
            inspection = (Inspection) getIntent().getExtras().getSerializable("inspection");
        }
        setContentView(R.layout.activity_inspection_view);

        final ListView listview = (ListView) findViewById(R.id.customlist);

        final InspectionViewAdapter adapter = new InspectionViewAdapter(this,
                android.R.layout.simple_list_item_1, inspection.getEntries());
        // Add a header to the ListView
//        LayoutInflater inflater = getLayoutInflater();
//
//        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.listitemsheader, listview, false);
//        listview.addHeaderView(header);
//
//        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.listitemsfooter, listview, false);
//        listview.addFooterView(footer);

        listview.setAdapter(adapter);


        setInspectionDateTextView();
        setInspectionCoordinatesTextView();
        setProducer1TextView();
        setProducer2TextView();
        setProducer3TextView();
        setProducer4TextView();
        setTotalValue();
        setInRegisterValue();
        lambValue();
        sheepValue();
        kidValue();
        goatValue();
        ramValue();
        heGoatValue();
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

    private void setInspectionDateTextView() {
        TextView inspectionDateTextView = (TextView) findViewById(R.id.viewInspectionDateValue);
        if (inspection != null)
            inspectionDateTextView.setText(dateFormat.format(inspection.getDate()));
    }
    private void setInspectionCoordinatesTextView() {
        TextView coordinatesTextView = (TextView) findViewById(R.id.viewCoordinatesValue);
        if (inspection != null)
            coordinatesTextView.setText(inspection.getLatitude() +", " + inspection.getLongitude());
    }
    private void setProducer1TextView() {
        TextView producerTextView = (TextView) findViewById(R.id.viewInspectionProducer1Value);
        if (inspection != null)
            producerTextView.setText(inspection.getProducer1Tin() + "-" + inspection.getProducer1Name());
    }

    private void setProducer2TextView() {
        TextView producerTextView = (TextView) findViewById(R.id.viewInspectionProducer2Value);
        if (inspection != null)
            producerTextView.setText(inspection.getProducer2Tin() + "-" + inspection.getProducer2Name());
    }

    private void setProducer3TextView() {
        TextView producerTextView = (TextView) findViewById(R.id.viewInspectionProducer3Value);
        if (inspection != null)
            producerTextView.setText(inspection.getProducer3Tin() + "-" + inspection.getProducer3Name());
    }

    private void setProducer4TextView() {
        TextView producerTextView = (TextView) findViewById(R.id.viewInspectionProducer4Value);
        if (inspection != null)
            producerTextView.setText(inspection.getProducer4Tin() + "-" + inspection.getProducer4Name());
    }

    private void setTotalValue() {
        TextView stats = (TextView) findViewById(R.id.statsTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy()).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    private void setInRegisterValue() {
        TextView stats = (TextView) findViewById(R.id.statsInRegisterValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    private void lambValue() {
        TextView stats = (TextView) findViewById(R.id.lambsTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.lambAnimal))).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void sheepValue() {
        TextView stats = (TextView) findViewById(R.id.sheepTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.sheepAnimal))).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void kidValue() {
        TextView stats = (TextView) findViewById(R.id.kidsTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.kidAnimal))).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void goatValue() {
        TextView stats = (TextView) findViewById(R.id.goatTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.goatAnimal))).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void ramValue() {
        TextView stats = (TextView) findViewById(R.id.ramTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.ramAnimal))).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void heGoatValue() {
        TextView stats = (TextView) findViewById(R.id.heGoatTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.heGoatAnimal))).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    public void sortGridEntriesByTag(View view) {
        ListView gridView = (ListView) findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::getTag));
        Button button = (Button)findViewById(R.id.tag);
        button.setOnClickListener(v ->
            { inspectionViewAdapter.sort(Comparator.comparing(Entry::getTag).reversed());
                button.setOnClickListener(y->sortGridEntriesByTag(y));
            });
    }

    public void sortGridEntriesByTime(View view) {
        ListView gridView = (ListView) findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::getTagDate));
        Button button = (Button)findViewById(R.id.age);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::getTagDate).reversed());
            button.setOnClickListener(y->sortGridEntriesByTime(y));
        });
    }

    public void sortGridEntriesByAnimalType(View view) {
        ListView gridView = (ListView) findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::getAnimalType));
        Button button = (Button)findViewById(R.id.type);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::getAnimalType).reversed());
            button.setOnClickListener(y->sortGridEntriesByAnimalType(y));
        });
    }

    public void sortGridEntriesByProducer(View view) {
        ListView gridView = (ListView) findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::getProducer));
        Button button = (Button)findViewById(R.id.names);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::getProducer).reversed());
            button.setOnClickListener(y->sortGridEntriesByProducer(y));
        });
    }
    public void sortGridEntriesByIsInRegister(View view) {
        ListView gridView = (ListView) findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::isInRegister));
        Button button = (Button)findViewById(R.id.ischecked);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::isInRegister).reversed());
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
