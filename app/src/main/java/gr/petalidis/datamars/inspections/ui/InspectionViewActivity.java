package gr.petalidis.datamars.inspections.ui;

import android.content.Intent;
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

        setTotalValue2();
        setInRegisterValue2();
        lambValue2();
        sheepValue2();
        kidValue2();
        goatValue2();
        ramValue2();
        heGoatValue2();

        setTotalValue3();
        setInRegisterValue3();
        lambValue3();
        sheepValue3();
        kidValue3();
        goatValue3();
        ramValue3();
        heGoatValue3();

        setTotalValue4();
        setInRegisterValue4();
        lambValue4();
        sheepValue4();
        kidValue4();
        goatValue4();
        ramValue4();
        heGoatValue4();
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
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.getProducerTin().equals(inspection.getProducer1Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    private void setInRegisterValue() {
        TextView stats = (TextView) findViewById(R.id.statsInRegisterValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true && x.getProducerTin().equals(inspection.getProducer1Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    private void lambValue() {
        TextView stats = (TextView) findViewById(R.id.lambsTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.lambAnimal)) && x.getProducerTin().equals(inspection.getProducer1Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void sheepValue() {
        TextView stats = (TextView) findViewById(R.id.sheepTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.sheepAnimal)) && x.getProducerTin().equals(inspection.getProducer1Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void kidValue() {
        TextView stats = (TextView) findViewById(R.id.kidsTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.kidAnimal)) && x.getProducerTin().equals(inspection.getProducer1Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void goatValue() {
        TextView stats = (TextView) findViewById(R.id.goatTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.goatAnimal)) && x.getProducerTin().equals(inspection.getProducer1Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void ramValue() {
        TextView stats = (TextView) findViewById(R.id.ramTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.ramAnimal)) && x.getProducerTin().equals(inspection.getProducer1Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void heGoatValue() {
        TextView stats = (TextView) findViewById(R.id.heGoatTotalValue);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.heGoatAnimal)) && x.getProducerTin().equals(inspection.getProducer1Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }


    private void setTotalValue2() {
        TextView stats = (TextView) findViewById(R.id.statsTotalValue2);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.getProducerTin().equals(inspection.getProducer2Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    private void setInRegisterValue2() {
        TextView stats = (TextView) findViewById(R.id.statsInRegisterValue2);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true && x.getProducerTin().equals(inspection.getProducer2Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    private void lambValue2() {
        TextView stats = (TextView) findViewById(R.id.lambsTotalValue2);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.lambAnimal)) && x.getProducerTin().equals(inspection.getProducer2Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void sheepValue2() {
        TextView stats = (TextView) findViewById(R.id.sheepTotalValue2);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.sheepAnimal)) && x.getProducerTin().equals(inspection.getProducer2Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void kidValue2() {
        TextView stats = (TextView) findViewById(R.id.kidsTotalValue2);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.kidAnimal)) && x.getProducerTin().equals(inspection.getProducer2Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void goatValue2() {
        TextView stats = (TextView) findViewById(R.id.goatTotalValue2);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.goatAnimal)) && x.getProducerTin().equals(inspection.getProducer2Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void ramValue2() {
        TextView stats = (TextView) findViewById(R.id.ramTotalValue2);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.ramAnimal)) && x.getProducerTin().equals(inspection.getProducer2Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void heGoatValue2() {
        TextView stats = (TextView) findViewById(R.id.heGoatTotalValue2);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.heGoatAnimal)) && x.getProducerTin().equals(inspection.getProducer2Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }


    private void setTotalValue3() {
        TextView stats = (TextView) findViewById(R.id.statsTotalValue3);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.getProducerTin().equals(inspection.getProducer3Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    private void setInRegisterValue3() {
        TextView stats = (TextView) findViewById(R.id.statsInRegisterValue3);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true && x.getProducerTin().equals(inspection.getProducer3Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    private void lambValue3() {
        TextView stats = (TextView) findViewById(R.id.lambsTotalValue3);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.lambAnimal)) && x.getProducerTin().equals(inspection.getProducer3Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void sheepValue3() {
        TextView stats = (TextView) findViewById(R.id.sheepTotalValue3);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.sheepAnimal)) && x.getProducerTin().equals(inspection.getProducer3Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void kidValue3() {
        TextView stats = (TextView) findViewById(R.id.kidsTotalValue3);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.kidAnimal)) && x.getProducerTin().equals(inspection.getProducer3Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void goatValue3() {
        TextView stats = (TextView) findViewById(R.id.goatTotalValue3);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.goatAnimal)) && x.getProducerTin().equals(inspection.getProducer3Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void ramValue3() {
        TextView stats = (TextView) findViewById(R.id.ramTotalValue3);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.ramAnimal)) && x.getProducerTin().equals(inspection.getProducer3Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void heGoatValue3() {
        TextView stats = (TextView) findViewById(R.id.heGoatTotalValue3);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.heGoatAnimal)) && x.getProducerTin().equals(inspection.getProducer3Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }


    private void setTotalValue4() {
        TextView stats = (TextView) findViewById(R.id.statsTotalValue4);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.getProducerTin().equals(inspection.getProducer4Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    private void setInRegisterValue4() {
        TextView stats = (TextView) findViewById(R.id.statsInRegisterValue4);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true && x.getProducerTin().equals(inspection.getProducer4Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }

    private void lambValue4() {
        TextView stats = (TextView) findViewById(R.id.lambsTotalValue4);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.lambAnimal)) && x.getProducerTin().equals(inspection.getProducer4Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void sheepValue4() {
        TextView stats = (TextView) findViewById(R.id.sheepTotalValue4);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.sheepAnimal)) && x.getProducerTin().equals(inspection.getProducer4Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void kidValue4() {
        TextView stats = (TextView) findViewById(R.id.kidsTotalValue4);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.kidAnimal)) && x.getProducerTin().equals(inspection.getProducer4Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void goatValue4() {
        TextView stats = (TextView) findViewById(R.id.goatTotalValue4);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.goatAnimal)) && x.getProducerTin().equals(inspection.getProducer4Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void ramValue4() {
        TextView stats = (TextView) findViewById(R.id.ramTotalValue4);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.ramAnimal)) && x.getProducerTin().equals(inspection.getProducer4Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
    }
    private void heGoatValue4() {
        TextView stats = (TextView) findViewById(R.id.heGoatTotalValue4);
        long validEntries = inspection.getEntries().stream().filter(x->!x.isDummy() && x.isInRegister()==true
                && x.getAnimalType().trim().equals(getResources().getString(R.string.heGoatAnimal)) && x.getProducerTin().equals(inspection.getProducer4Tin())).count();
        if (inspection != null)
            stats.setText(validEntries+"");
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
}
