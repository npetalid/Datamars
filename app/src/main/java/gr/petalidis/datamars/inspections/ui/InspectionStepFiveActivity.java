
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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.util.Objects;
import java.util.stream.Collectors;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.activities.StartActivity;
import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.InspectionService;

public class InspectionStepFiveActivity extends AppCompatActivity {

    private Inspection inspection;
    private DbHandler dbHandler;
    private Context mContext;

    private int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mContext = this;
        super.onCreate(savedInstanceState);
        // recovering the instance state
        if (savedInstanceState != null) {
            inspection = (Inspection) savedInstanceState.getSerializable("inspection");
        } else {
            inspection = (Inspection) Objects.requireNonNull(getIntent().getExtras()).getSerializable("inspection");
        }
        dbHandler = new DbHandler(this.getApplicationContext());
        setContentView(R.layout.activity_inspection_step_five);

        TextView value = findViewById(R.id.noTagUnder6MonthValue);
        value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    inspection.setNoTagUnder6Month(inspection.getProducers().get(index),
                            Integer.parseInt(editable.toString()));
                }
            }
        });
        value = findViewById(R.id.noTagOver6MonthValue);
        value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    inspection.setNoTagOver6Month(inspection.getProducers().get(index),
                            Integer.parseInt(editable.toString()));
                }
            }
        });

        updateButtons();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
        AlertDialog.Builder preSaveBuilder = new AlertDialog.Builder(this);

        String totalResult = inspection.getProducersWithNoDummy().stream().map(
                inspectee -> inspection.generateReportFor(inspectee).toHtmlString()
        ).collect(Collectors.joining("<br/>\n"));

        String msg = totalResult +   "<br/>\nΝα γίνει αποθήκευση;";
        WebView webView = new WebView(this.getApplicationContext());
        webView.setTag(R.id.WEBVIEW,"WebView");
        WebSettings webSettings = webView.getSettings();
        webView.setVerticalScrollBarEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(msg, "text/html", "utf-8");
        preSaveBuilder.setTitle("Αποθήκευση ελέγχου").setView(webView)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    InspectionService.save(dbHandler, inspection);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(R.string.success).setMessage(R.string.success);
                    builder.show();
                    Intent intent = new Intent(mContext,StartActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        preSaveBuilder.show();


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
        TextView conventionalTin = findViewById(R.id.conventionalTin);
        Inspectee inspectee = inspection.getProducersWithNoDummy().get(index);

        conventionalTin.setText(inspectee.getTin() + " " +
                inspectee.getName());
        TextView noTagOver6MonthOld = findViewById(R.id.noTagOver6MonthValue);
        noTagOver6MonthOld.setText(inspection.getNoTagOver6MonthOld(inspectee)+"");
        TextView noTagUnder6MonthOld = findViewById(R.id.noTagUnder6MonthValue);
        noTagUnder6MonthOld.setText(inspection.getNoTagUnder6MonthOld(inspectee)+"");

        ImageButton previousProducerButton = findViewById(R.id.previousProducer2);
        ImageButton nextProducerButton = findViewById(R.id.nextProducer2);

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
