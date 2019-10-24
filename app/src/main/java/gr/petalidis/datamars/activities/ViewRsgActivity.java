/*        Copyright 2017 Nikolaos Petalidis
*
*        Licensed under the Apache License, Version 2.0 (the "License");
*        you may not use this file except in compliance with the License.
*        You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*        Unless required by applicable law or agreed to in writing, software
*        distributed under the License is distributed on an "AS IS" BASIS,
*        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*        See the License for the specific language governing permissions and
*        limitations under the License.
*/


package gr.petalidis.datamars.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.log4j.Logger;

import java.util.ArrayList;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.R;
import gr.petalidis.datamars.rsglibrary.Rsg;
import gr.petalidis.datamars.rsglibrary.RsgReader;


public class ViewRsgActivity extends AppCompatActivity {

    private static final Logger log = Log4jHelper.getLogger(ViewRsgActivity.class.getName());
    private TextView mTextMessage;
    private String filename = "";
    private String date = "";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rsg);

        if (savedInstanceState != null) {
            filename = savedInstanceState.getString("filename");
            if (filename == null) {
                filename = "";
            }
            if (date == null) {
                date = "";
            }
        } else {
            Intent intent = getIntent();
            if (intent != null) {
                filename = intent.getStringExtra("rsg");
                date = intent.getStringExtra("date");
            }
        }
        try {
            TextView dateField = findViewById(R.id.rsgDate);
            dateField.setText(date);
            ArrayList<Rsg> rsgs = RsgReader.readRsgFromTablet(filename);
            RsgAdapter adapter = new RsgAdapter(this, rsgs);
            ListView gridView = findViewById(R.id.rsglistId);
            gridView.setAdapter(adapter);
        } catch (Exception e) {
            log.error( "Unable to readRsgFromTablet: " + e.getLocalizedMessage());
        }


    }

    public void sortGridEntriesByCountry(View view) {
        ListView gridView = findViewById(R.id.rsglistId);
        RsgAdapter rsgAdapter = (RsgAdapter) gridView.getAdapter();
        rsgAdapter.sort((first, second) -> first.getCountryCode().compareTo(second.getCountryCode()));

        Button button = findViewById(R.id.countryButton);
        button.setOnClickListener(this::sortGridEntriesByCountryReverse);

    }

    private void sortGridEntriesByCountryReverse(View view) {
        ListView gridView = findViewById(R.id.rsglistId);
        RsgAdapter rsgAdapter = (RsgAdapter) gridView.getAdapter();
        rsgAdapter.sort((first, second) -> second.getCountryCode().compareTo(first.getCountryCode()));

        Button button = findViewById(R.id.countryButton);
        button.setOnClickListener(this::sortGridEntriesByCountry);
    }
    public void sortGridEntriesById(View view) {
        ListView gridView = findViewById(R.id.rsglistId);
        RsgAdapter rsgAdapter = (RsgAdapter) gridView.getAdapter();
        rsgAdapter.sort((first, second) -> first.getIdentificationCode().compareTo(second.getIdentificationCode()));
        Button button = findViewById(R.id.idButton);
        button.setOnClickListener(this::sortGridEntriesByIdReverse);


    }

    private void sortGridEntriesByIdReverse(View view) {
        ListView gridView = findViewById(R.id.rsglistId);
        RsgAdapter rsgAdapter = (RsgAdapter) gridView.getAdapter();
        rsgAdapter.sort((first, second) -> second.getIdentificationCode().compareTo(first.getIdentificationCode()));
        Button button = findViewById(R.id.idButton);
        button.setOnClickListener(this::sortGridEntriesById);
    }

    public void sortGridEntriesByTime(View view) {
        ListView gridView = findViewById(R.id.rsglistId);
        RsgAdapter rsgAdapter = (RsgAdapter) gridView.getAdapter();
        rsgAdapter.sort((first, second) -> first.getDate().compareTo(second.getDate()));
        Button button = findViewById(R.id.timeButton);
        button.setOnClickListener(this::sortGridEntriesByTimeReverse);

    }

    private void sortGridEntriesByTimeReverse(View view) {
        ListView gridView = findViewById(R.id.rsglistId);
        RsgAdapter rsgAdapter = (RsgAdapter) gridView.getAdapter();
        rsgAdapter.sort((first, second) -> second.getDate().compareTo(first.getDate()));
        Button button = findViewById(R.id.timeButton);
        button.setOnClickListener(this::sortGridEntriesByTime);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("filename", filename);
        savedInstanceState.putString("date",date);
    }

    @Override
    public void onRestoreInstanceState(@NonNull  Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
            filename = savedInstanceState.getString("filename");
            if (filename == null) {
                filename = "";
            }
            date = savedInstanceState.getString("date");
    }

}
