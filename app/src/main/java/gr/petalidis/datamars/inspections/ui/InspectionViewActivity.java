package gr.petalidis.datamars.inspections.ui;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Comparator;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Animals;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.inspections.domain.Inspection;

public class InspectionViewActivity extends AppCompatActivity {

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Inspection inspection;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            inspection = (Inspection) savedInstanceState.getSerializable("inspection");
        } else {
            inspection = (Inspection) getIntent().getExtras().getSerializable("inspection");
        }
        setContentView(R.layout.activity_inspection_view);

        setInspectionDateTextView();
        setInspectionCoordinatesTextView();
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

    private void setTotalValue(String tin) {
        TextView stats = (TextView) findViewById(R.id.statsTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.getProducerTin().equals(tin)).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    private void setInRegisterValue(String tin) {
        TextView stats = (TextView) findViewById(R.id.statsInRegisterValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true && x.getProducerTin().equals(tin)).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    private void lambValue(String tin) {
        TextView countValue = (TextView) findViewById(R.id.lambsTotalValue);
        TextView isInRegisterValue = (TextView) findViewById(R.id.lambsInRegisterValue);

        if (inspection != null) {
            countValue.setText(inspection.getCount(tin,Animals.LAMB_ANIMAL)+"");
            isInRegisterValue.setText(inspection.getInRegisterCount(tin,Animals.LAMB_ANIMAL)+"");
        }
    }
    private void sheepValue(String tin) {
        TextView countValue = (TextView) findViewById(R.id.sheepTotalValue);
        TextView isInRegisterValue = (TextView) findViewById(R.id.sheepIsInRegisterValue);
        if (inspection != null) {
            countValue.setText(inspection.getCount(tin,Animals.SHEEP_ANIMAL)+"");
            isInRegisterValue.setText(inspection.getInRegisterCount(tin,Animals.SHEEP_ANIMAL)+"");
        }
    }
    private void kidValue(String tin) {
        TextView countValue = (TextView) findViewById(R.id.kidTotalValue);
        TextView isInRegisterValue = (TextView) findViewById(R.id.kidIsInRegisterValue);
        if (inspection != null) {
            countValue.setText(inspection.getCount(tin,Animals.KID_ANIMAL)+"");
            isInRegisterValue.setText(inspection.getInRegisterCount(tin,Animals.KID_ANIMAL)+"");
        }
    }
    private void goatValue(String tin) {
        TextView countValue = (TextView) findViewById(R.id.goatTotalValue);
        TextView isInRegisterValue = (TextView) findViewById(R.id.goatIsInRegisterValue);
        if (inspection != null) {
            countValue.setText(inspection.getCount(tin,Animals.GOAT_ANIMAL)+"");
            isInRegisterValue.setText(inspection.getInRegisterCount(tin,Animals.GOAT_ANIMAL)+"");
        }
    }
    private void ramValue(String tin) {
        TextView countValue = (TextView) findViewById(R.id.ramTotalValue);
        TextView isInRegisterValue = (TextView) findViewById(R.id.ramIsInRegisterValue);
        if (inspection != null) {
            countValue.setText(inspection.getCount(tin,Animals.RAM_ANIMAL)+"");
            isInRegisterValue.setText(inspection.getInRegisterCount(tin,Animals.RAM_ANIMAL)+"");
        }
    }
    private void horseValue(String tin) {
        TextView countValue = (TextView) findViewById(R.id.horseTotalValue);
        TextView isInRegisterValue = (TextView) findViewById(R.id.horseIsInRegisterValue);
        if (inspection != null) {
            countValue.setText(inspection.getCount(tin,Animals.HORSE_ANIMAL)+"");
            isInRegisterValue.setText(inspection.getInRegisterCount(tin,Animals.HORSE_ANIMAL)+"");
        }
    }

    private void heGoatValue(String tin) {
        TextView countValue = (TextView) findViewById(R.id.heGoatTotalValue);
        TextView isInRegisterValue = (TextView) findViewById(R.id.heGoatIsInRegister);
        if (inspection != null) {
            countValue.setText(inspection.getCount(tin,Animals.HEGOAT_ANIMAL)+"");
            isInRegisterValue.setText(inspection.getInRegisterCount(tin,Animals.HEGOAT_ANIMAL)+"");
        }
    }

    private void singleTagValue(String tin) {
        TextView countValue = (TextView) findViewById(R.id.singleTagValue);
        if (inspection != null) {
            countValue.setText(inspection.getUniqueTag(tin)+"");
        }
    }
    public void goToViewActivityStep2(View view) {
        Intent intent = new Intent(this, InspectionViewActivity2.class);
        intent.putExtra("inspection",inspection);
        startActivity(intent);
    }

    public void viewPhotos(View view) {
        Intent intent = new Intent(this, InspectionViewPhotoActivity.class);
        intent.putExtra("inspection",inspection);
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
        TextView noEarringTin = findViewById(R.id.viewProducerTin);
        Inspectee inspectee = inspection.getProducersWithNoDummy().get(index);

        noEarringTin.setText(inspectee.getTin() + " " +
                inspectee.getName());
        setTotalValue(inspectee.getTin());
        setInRegisterValue(inspectee.getTin());
        lambValue(inspectee.getTin());
        sheepValue(inspectee.getTin());
        kidValue(inspectee.getTin());
        goatValue(inspectee.getTin());
        ramValue(inspectee.getTin());
        heGoatValue(inspectee.getTin());
        horseValue(inspectee.getTin());
        singleTagValue(inspectee.getTin());

        ImageButton previousProducerButton = findViewById(R.id.previousProducer3);
        ImageButton nextProducerButton = findViewById(R.id.nextProducer3);

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
