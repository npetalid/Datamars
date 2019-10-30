
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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.util.Comparator;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.domain.Inspection;

public class InspectionStepThreeActivity extends AppCompatActivity {

    private Inspection inspection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // recovering the instance state
        if (savedInstanceState != null) {
            inspection = (Inspection) savedInstanceState.getSerializable("inspection");
        } else {
            inspection = (Inspection) getIntent().getExtras().getSerializable("inspection");
        }
        setContentView(R.layout.activity_inspection_step_three);

        final Button checkButton = findViewById(R.id.ischecked);

        checkButton.setOnLongClickListener(this::reverseSelection);

        final ListView listview = findViewById(R.id.editItemsList);

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

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

    public void goToStepFourActivity(View view) {
        Intent intent = new Intent(this, InspectionStepFourActivity.class);

        intent.putExtra("inspection", inspection);

        startActivity(intent);

    }

    public void sortGridEntriesByTag(View view) {
        ListView gridView = findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getTag));
        Button button = findViewById(R.id.tag);
        button.setOnClickListener(v ->
        { inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getTag).reversed());
            button.setOnClickListener(this::sortGridEntriesByTag);
        });
    }

    public void sortGridEntriesByTime(View view) {
        ListView gridView = findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getTagDate));
        Button button = findViewById(R.id.age);
        button.setOnClickListener(v ->
        { inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getTagDate).reversed());
            button.setOnClickListener(this::sortGridEntriesByTime);
        });
    }

    public void sortGridEntriesByAnimalType(View view) {
        ListView gridView = findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getAnimalType));
        Button button = findViewById(R.id.type);
        button.setOnClickListener(v ->
        { inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getAnimalType).reversed());
            button.setOnClickListener(this::sortGridEntriesByAnimalType);
        });
    }

    public void sortGridEntriesByProducer(View view) {
        ListView gridView = findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getProducer));
        Button button = findViewById(R.id.names);
        button.setOnClickListener(v ->
        { inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getProducer).reversed());
            button.setOnClickListener(this::sortGridEntriesByProducer);
        });
    }
    public void sortGridEntriesByIsInRegister(View view) {
        ListView gridView = findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::isInRegister));
        Button button = findViewById(R.id.ischecked);
        button.setOnClickListener(v ->
        { inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::isInRegister).reversed());
            button.setOnClickListener(this::sortGridEntriesByIsInRegister);
        });
    }

    public void sortGridEntriesByGenre(View view) {
        ListView gridView = findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getAnimalGenre));
        Button button = findViewById(R.id.race);
        button.setOnClickListener(v ->
        { inspectionStepThreeAdapter.sort(Comparator.comparing(Entry::getAnimalGenre).reversed());
            button.setOnClickListener(this::sortGridEntriesByGenre);
        });
    }

    private boolean reverseSelection(View view) {
        ListView gridView = findViewById(R.id.editItemsList);
        InspectionStepThreeAdapter inspectionStepThreeAdapter = (InspectionStepThreeAdapter) gridView.getAdapter();
        inspectionStepThreeAdapter.revertRegister();
        inspectionStepThreeAdapter.notifyDataSetChanged();
        gridView.setAdapter(inspectionStepThreeAdapter);
        return true;
    }
    public void sortGridEntriesByComments(View view) {
        ListView gridView = findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::getComment));
        Button button = findViewById(R.id.viewCommentsButton);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::getComment).reversed());
            button.setOnClickListener(this::sortGridEntriesByComments);
        });
    }
}
