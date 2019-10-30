
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

import android.icu.text.SimpleDateFormat;
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

public class InspectionViewActivity2 extends AppCompatActivity {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Inspection inspection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_view2);
        if (savedInstanceState != null) {
            inspection = (Inspection) savedInstanceState.getSerializable("inspection");
        } else {
            inspection = (Inspection) getIntent().getExtras().getSerializable("inspection");
        }
        final ListView listview = findViewById(R.id.customlist);

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
    public void sortGridEntriesByTag(View view) {
        ListView gridView = findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::getTag));
        Button button = findViewById(R.id.tag);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::getTag).reversed());
            button.setOnClickListener(y->sortGridEntriesByTag(y));
        });
    }

    public void sortGridEntriesByTime(View view) {
        ListView gridView = findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::getTagDate));
        Button button = findViewById(R.id.age);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::getTagDate).reversed());
            button.setOnClickListener(y->sortGridEntriesByTime(y));
        });
    }

    public void sortGridEntriesByAnimalType(View view) {
        ListView gridView = findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::getAnimalType));
        Button button = findViewById(R.id.type);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::getAnimalType).reversed());
            button.setOnClickListener(y->sortGridEntriesByAnimalType(y));
        });
    }

    public void sortGridEntriesByProducer(View view) {
        ListView gridView = findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::getProducer));
        Button button = findViewById(R.id.names);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::getProducer).reversed());
            button.setOnClickListener(y->sortGridEntriesByProducer(y));
        });
    }
    public void sortGridEntriesByIsInRegister(View view) {
        ListView gridView = findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::isInRegister));
        Button button = findViewById(R.id.ischecked);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::isInRegister).reversed());
            button.setOnClickListener(y->sortGridEntriesByIsInRegister(y));
        });
    }

    public void sortGridEntriesByGenre(View view) {
        ListView gridView = findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::getAnimalGenre));
        Button button = findViewById(R.id.race);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::getAnimalGenre).reversed());
            button.setOnClickListener(y->sortGridEntriesByIsInRegister(y));
        });
    }
    public void sortGridEntriesByComments(View view) {
        ListView gridView = findViewById(R.id.customlist);
        InspectionViewAdapter inspectionViewAdapter = (InspectionViewAdapter) gridView.getAdapter();
        inspectionViewAdapter.sort(Comparator.comparing(Entry::getComment));
        Button button = findViewById(R.id.viewCommentsButton);
        button.setOnClickListener(v ->
        { inspectionViewAdapter.sort(Comparator.comparing(Entry::getComment).reversed());
            button.setOnClickListener(y->sortGridEntriesByComments(y));
        });
    }
}
