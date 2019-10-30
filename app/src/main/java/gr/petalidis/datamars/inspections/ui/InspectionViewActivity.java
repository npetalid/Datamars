
/*
 * Copyright 2017-2019 Nikolaos Petalidis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package gr.petalidis.datamars.inspections.ui;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

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
        TextView inspectionDateTextView = findViewById(R.id.viewInspectionDateValue);
        if (inspection != null)
            inspectionDateTextView.setText(dateFormat.format(inspection.getDate()));
    }
    private void setInspectionCoordinatesTextView() {
        TextView coordinatesTextView = findViewById(R.id.viewCoordinatesValue);
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
                TextView inspecteeView  = findViewById(R.id.viewProducerTin);
                inspecteeView.setText(inspectee.getTin() + " " +
                        inspectee.getName());

                Report report = inspection.generateReportFor(inspectee);
                TextView totalValue = findViewById(R.id.total);
                totalValue.setText(report.getTotal() + "");

                TextView noTag = findViewById(R.id.noTag);
                noTag.setText(report.getNoTag() + "");

                TextView noTagUnder6 = findViewById(R.id.noTagUnder6);
                noTagUnder6.setText(report.getNoTagUnder6() + "");

                TextView noElectronicTag = findViewById(R.id.noElectronicTag);
                noElectronicTag.setText(report.getNoElectronicTag() + "");

                TextView singleTag = findViewById(R.id.singleTag);
                singleTag.setText(report.getSingleTag() + "");

                TextView countedButNotInRegistry = findViewById(R.id.countedButNotInRegistry);
                countedButNotInRegistry.setText(report.getCountedButNotInRegistry() + "");

                Map<AnimalType, Long> selectable = report.getSelectable();
                TextView sheepTotalValue = findViewById(R.id.sheepTotalValue);
                sheepTotalValue.setText(selectable.get(AnimalType.SHEEP_ANIMAL) + "");
                TextView goatTotalValue = findViewById(R.id.goatTotalValue);
                goatTotalValue.setText(selectable.get(AnimalType.GOAT_ANIMAL) + "");

                TextView ramTotalValue = findViewById(R.id.ramTotalValue);
                ramTotalValue.setText(selectable.get(AnimalType.RAM_ANIMAL) + selectable.get(AnimalType.HEGOAT_ANIMAL) + "");

                TextView lambsTotalValue = findViewById(R.id.lambsTotalValue);
                lambsTotalValue.setText(selectable.get(AnimalType.KIDLAMB_ANIMAL)+"");

                TextView horseTotalValue = findViewById(R.id.horseTotalValue);
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
