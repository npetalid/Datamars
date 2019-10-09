package gr.petalidis.datamars.inspections.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.log4j.Logger;

import java.util.Map;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.R;
import gr.petalidis.datamars.SessionViewer;
import gr.petalidis.datamars.inspections.domain.AnimalType;
import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.domain.Report;

public class InspectionViewActivity extends AppCompatActivity {

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Inspection inspection;
    private int index = 0;
    private static final Logger log = Log4jHelper.getLogger(SessionViewer.class.getName());

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
        if (inspection!=null) {
            Inspectee inspectee = inspection.getProducersWithNoDummy().get(index);

            if (inspectee!=null) {
                Report report = inspection.generateReportFor(inspectee);
                TextView totalValue = (TextView) findViewById(R.id.total);
                totalValue.setText(report.getTotal() + "");

                TextView noTag = (TextView) findViewById(R.id.noTag);
                noTag.setText(report.getNoTag() + "");

                TextView noTagUnder6 = (TextView) findViewById(R.id.noTagUnder6);
                noTagUnder6.setText(report.getNoTagUnder6() + "");

                TextView noElectronicTag = (TextView) findViewById(R.id.noElectronicTag);
                noElectronicTag.setText(report.getNoElectronicTag() + "");

                TextView singleTag = (TextView) findViewById(R.id.singleTag);
                singleTag.setText(report.getSingleTag() + "");

                TextView countedButNotInRegistry = (TextView) findViewById(R.id.countedButNotInRegistry);
                countedButNotInRegistry.setText(report.getCountedButNotInRegistry() + "");

                Map<AnimalType, Long> selectable = report.getSelectable();
                TextView sheepTotalValue = (TextView) findViewById(R.id.sheepTotalValue);
                sheepTotalValue.setText(selectable.get(AnimalType.SHEEP_ANIMAL) + "");
                TextView goatTotalValue = (TextView) findViewById(R.id.goatTotalValue);
                goatTotalValue.setText(selectable.get(AnimalType.GOAT_ANIMAL) + "");

                TextView ramTotalValue = (TextView) findViewById(R.id.ramTotalValue);
                ramTotalValue.setText(selectable.get(AnimalType.RAM_ANIMAL) + "");

                TextView heGoatTotalValue = (TextView) findViewById(R.id.heGoatTotalValue);
                heGoatTotalValue.setText(selectable.get(AnimalType.HEGOAT_ANIMAL) + "");

                TextView kidTotalValue = (TextView) findViewById(R.id.kidTotalValue);
                kidTotalValue.setText(selectable.get(AnimalType.KID_ANIMAL) + "");

                TextView lambsTotalValue = (TextView) findViewById(R.id.lambsTotalValue);
                lambsTotalValue.setText(selectable.get(AnimalType.LAMB_ANIMAL) + "");

                TextView horseTotalValue = (TextView) findViewById(R.id.horseTotalValue);
                horseTotalValue.setText(selectable.get(AnimalType.HORSE_ANIMAL) + "");


                ImageButton previousProducerButton = findViewById(R.id.previousProducer3);
                ImageButton nextProducerButton = findViewById(R.id.nextProducer3);

                if (inspection.getProducersWithNoDummy().isEmpty() || inspection.getProducersWithNoDummy().size() == 1) {
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
            else {
                log.error("InspectionViewActivity: Tried to get inspectee with index " + index +
                        " for inspection " + inspection.getId() + " but was null");

            }
        } else {
            log.error("InspectionViewActivity: Tried to get inspection but was null");
        }
    }
}
