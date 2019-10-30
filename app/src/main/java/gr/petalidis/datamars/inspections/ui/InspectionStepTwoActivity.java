
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

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.Moo;
import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.dto.ThumbnailDto;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.repository.EntryRepository;
import gr.petalidis.datamars.inspections.utilities.WGS84Converter;
import gr.petalidis.datamars.inspections.validators.TinValidator;
import gr.petalidis.datamars.rsglibrary.Rsg;
import gr.petalidis.datamars.rsglibrary.RsgReader;

public class InspectionStepTwoActivity extends AppCompatActivity {
    private static final int PERMISSION_GPS_REQUEST_CODE = 2;

    private Inspection inspection;
    private String filename = "";
    private ArrayList<ThumbnailDto> thumbnails;

    private Date inspectionDate;
    private Set<Rsg> rsgs = new HashSet<>();

    private Set<String> owners = new HashSet<>();

    private Location gpsLocation = null;

    private Activity mContext;

    private boolean requestLocationUpdates = true;

    private LocationCallback mLocationCallback;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = this;


        if (savedInstanceState != null) {
            inspection = (Inspection) savedInstanceState.getSerializable("inspection");
            inspectionDate = (Date) savedInstanceState.getSerializable("inspectionDate");
            filename = savedInstanceState.getString("rsgFilename");
            thumbnails = (ArrayList<ThumbnailDto>) savedInstanceState.getSerializable("thumbnails");
            requestLocationUpdates = savedInstanceState.getBoolean("requestLocationUpdates");
        } else {
            inspectionDate = (Date) Objects.requireNonNull(getIntent().getExtras()).getSerializable("inspectionDate");
            filename = getIntent().getExtras().getString("rsgFilename");
            thumbnails = (ArrayList<ThumbnailDto>) getIntent().getExtras().getSerializable("thumbnails");
        }

        setContentView(R.layout.activity_inspection_step_two);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    setGpsLocation(location);
                }
            }
        };
        // recovering the instance state
        checkLocationPermission();


        InspectionStepTwoAsyncTask inspectionStepTwoActivityAsyncTask = new InspectionStepTwoAsyncTask(this);

        inspectionStepTwoActivityAsyncTask.execute(filename,new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(inspectionDate));

        if (inspection != null) {
            setProducer1Name();
            setProducer2Name();
            setProducer3Name();
            setProducer4Name();
            setProducer1Tin();
            setProducer2Tin();
            setProducer3Tin();
            setProducer4Tin();
        }

        EditText tin = findViewById(R.id.producer1TinText);
        tin.addTextChangedListener(new TinWatcher(new TinValidator(), tin));
        tin = findViewById(R.id.producer2TinText);
        tin.addTextChangedListener(new TinWatcher(new TinValidator(), tin));
        tin = findViewById(R.id.producer3TinText);
        tin.addTextChangedListener(new TinWatcher(new TinValidator(), tin));
        tin = findViewById(R.id.producer4TinText);
        tin.addTextChangedListener(new TinWatcher(new TinValidator(), tin));


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("inspection", inspection);
        outState.putString("rsgFilename", filename);
        outState.putSerializable("inspectionDate", inspectionDate);
        outState.putSerializable("thumbnails",thumbnails);
        outState.putBoolean("requestLocationUpdates",requestLocationUpdates);
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    private Set<String> getOwners() {
        return owners;
    }

    private void setOwners(Set<String> owners) {
        this.owners = owners;
    }

    private void setRsgs(Set<Rsg> rsgs) {
        this.rsgs = rsgs;
    }
    private boolean formHasErrors() {
        boolean hasErrors = false;
        TinValidator validator = new TinValidator();
        EditText tin = findViewById(R.id.producer1TinText);
        String tinString = tin.getText().toString();
        EditText producer1Name = findViewById(R.id.producer1NameText);
        String producer1NameString = producer1Name.getText().toString();

        EditText tin2 = findViewById(R.id.producer2TinText);
        String tinString2 = tin2.getText().toString();


        EditText tin3 = findViewById(R.id.producer3TinText);
        String tinString3 = tin3.getText().toString();

        EditText tin4 = findViewById(R.id.producer4TinText);
        String tinString4 = tin4.getText().toString();

        if (!validator.isValid(tinString)) {
            hasErrors = true;
            tin.setError("Το ΑΦΜ δεν είναι σωστό");
        }
        if (producer1NameString.length() < 2) {
            hasErrors = true;
            producer1Name.setError("Υποχρεωτικό πεδίο");
        }

        if (tinString2.length() > 0 && !validator.isValid(tinString2)) {
            hasErrors = true;
            tin2.setError("Το ΑΦΜ δεν είναι σωστό");
        }

        if (tinString3.length() > 0 && !validator.isValid(tinString2)) {
            hasErrors = true;
            tin3.setError("Το ΑΦΜ δεν είναι σωστό");
        }

        if (tinString4.length() > 0 && !validator.isValid(tinString2)) {
            hasErrors = true;
            tin4.setError("Το ΑΦΜ δεν είναι σωστό");
        }

        return hasErrors;

    }

    public void gpsToggle(View view) {
        Button button = findViewById(R.id.gpsButton);

        if (requestLocationUpdates) {
            stopLocationUpdates();
            requestLocationUpdates=false;
            button.setText("Έναρξη ενημέρωσης συν/γμένων");
        } else {
            requestLocationUpdates =true;
            checkLocationPermission();
            button.setText("Παύση ενημέρωσης συν/γμένων");
        }
    }
    public void goToInspectionStepThreeActivity(View view) {

        if (!formHasErrors() && !rsgs.isEmpty()) {

            Spinner producer1TagSpinner = findViewById(R.id.producer1TagValue);
            String producer1Tag = (String) producer1TagSpinner.getSelectedItem();
            Spinner producer2TagSpinner = findViewById(R.id.producer2TagValue);
            String producer2Tag = (String) producer2TagSpinner.getSelectedItem();
            Spinner producer3TagSpinner = findViewById(R.id.producer3TagValue);
            String producer3Tag = (String) producer3TagSpinner.getSelectedItem();
            Spinner producer4TagSpinner = findViewById(R.id.producer4TagValue);
            String producer4Tag = (String) producer4TagSpinner.getSelectedItem();

            Spinner animalTypeSpinner = findViewById(R.id.animalType);
            String animalType = (String) animalTypeSpinner.getSelectedItem();

            Intent intent = new Intent(this, InspectionStepThreeActivity.class);

            inspection = new Inspection(inspectionDate, getProducer1Tin(), getProducer1Name());

            inspection.setProducer2Name(getProducer2Name());
            inspection.setProducer2Tin(getProducer2Tin());

            inspection.setProducer3Name(getProducer3Name());
            inspection.setProducer3Tin(getProducer3Tin());

            inspection.setProducer4Name(getProducer4Name());
            inspection.setProducer4Tin(getProducer4Tin());

            double[] coordinates = {0.0, 0.0};
            if (gpsLocation != null) {
                coordinates = WGS84Converter.toGGRS87(gpsLocation.getLatitude(), gpsLocation.getLongitude());
            }
            inspection.setLatitude(coordinates[0]);
            inspection.setLongitude(coordinates[1]);
            inspection.initEntries(rsgs);
            inspection.initScannedDocuments(thumbnails);
            inspection.getEntries()
                    //.stream()
                    //.filter(x -> x.getOwner().equals(producer1Tag))
                    .forEach(x -> {
                x.setProducerTin(inspection.getProducer1Tin());
                x.setProducer(inspection.getProducer1Name());
                x.setAnimalType(animalType);
            });

            if (!inspection.getProducer2Tin().isEmpty()) {
                inspection.getEntries().stream().filter(x -> x.getOwner().equals(producer2Tag)).forEach(x -> {
                    x.setProducerTin(inspection.getProducer2Tin());
                    x.setProducer(inspection.getProducer2Name());
                    x.setAnimalType(animalType);
                });
            }

            if (!inspection.getProducer3Tin().isEmpty()) {
                inspection.getEntries().stream().filter(x -> x.getOwner().equals(producer3Tag)).forEach(x -> {
                    x.setProducerTin(inspection.getProducer3Tin());
                    x.setProducer(inspection.getProducer3Name());
                    x.setAnimalType(animalType);
                });
            }
            if (!inspection.getProducer4Tin().isEmpty()) {
                inspection.getEntries().stream().filter(x -> x.getOwner().equals(producer4Tag)).forEach(x -> {
                    x.setProducerTin(inspection.getProducer4Tin());
                    x.setProducer(inspection.getProducer4Name());
                    x.setAnimalType(animalType);
                });
            }
            intent.putExtra("inspection", inspection);

            startActivity(intent);
        }


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

    private String getProducer1Name() {
        EditText name = findViewById(R.id.producer1NameText);
        return name.getText().toString().trim();
    }

    private String getProducer1Tin() {
        EditText tin = findViewById(R.id.producer1TinText);
        return tin.getText().toString().trim();
    }

    private String getProducer2Name() {
        EditText name = findViewById(R.id.producer2NameText);
        return name.getText().toString().trim();
    }

    private String getProducer2Tin() {
        EditText tin = findViewById(R.id.producer2TinText);
        return tin.getText().toString().trim();
    }

    private String getProducer3Name() {
        EditText name = findViewById(R.id.producer3NameText);
        return name.getText().toString().trim();
    }

    private String getProducer3Tin() {
        EditText tin = findViewById(R.id.producer3TinText);
        return tin.getText().toString().trim();
    }

    private String getProducer4Name() {
        EditText name = findViewById(R.id.producer4NameText);
        return name.getText().toString().trim();
    }

    private String getProducer4Tin() {
        EditText tin = findViewById(R.id.producer4TinText);
        return tin.getText().toString().trim();
    }

    private void setProducer1Name() {
        EditText name = findViewById(R.id.producer1NameText);
        if (inspection != null) name.setText(inspection.getProducer1Name());
    }

    private void setProducer1Tin() {
        EditText tin = findViewById(R.id.producer1TinText);
        if (inspection != null) tin.setText(inspection.getProducer1Tin());
    }

    private void setProducer2Name() {
        EditText name = findViewById(R.id.producer2NameText);
        if (inspection != null) name.setText(inspection.getProducer2Name());
    }

    private void setProducer2Tin() {
        EditText tin = findViewById(R.id.producer2TinText);
        if (inspection != null) tin.setText(inspection.getProducer2Tin());
    }

    private void setProducer3Name() {
        EditText name = findViewById(R.id.producer3NameText);
        if (inspection != null) name.setText(inspection.getProducer3Name());
    }

    private void setProducer3Tin() {
        EditText tin = findViewById(R.id.producer3TinText);
        if (inspection != null) tin.setText(inspection.getProducer3Tin());
    }

    private void setProducer4Name() {
        EditText name = findViewById(R.id.producer4NameText);
        if (inspection != null) name.setText(inspection.getProducer4Name());
    }

    private void setProducer4Tin() {
        EditText tin = findViewById(R.id.producer4TinText);
        if (inspection != null) tin.setText(inspection.getProducer4Tin());
    }


    private void setGpsLocation(Location location) {

        if (location!=null &&
                (gpsLocation==null || location.distanceTo(gpsLocation)>1 || location.getAccuracy()<gpsLocation.getAccuracy())) {
            gpsLocation = location;

            double[] coordinates;

            coordinates = WGS84Converter.toGGRS87(gpsLocation.getLatitude(), gpsLocation.getLongitude());

            TextView gpsLocation = findViewById(R.id.gpsValue);
            float accuracy = location.getAccuracy();
            gpsLocation.setText(String.format(Locale.forLanguageTag("el"), "%.2f", coordinates[0])
                    + ", " + String.format(Locale.forLanguageTag("el"), "%.2f", coordinates[1]) + "\n (ακρίβεια 68% εντός:"
                    + String.format(Locale.forLanguageTag("el"), "%.2f", accuracy) + "μ)");
        }
    }

    private void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // TODO: Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Αίτημα χρήσης υπηρεσιών τοποθεσίας")
                        .setMessage("Η τοποθεσία χρησιμοποιείται για την τοποθέτηση του στάβλου όταν καταχωρείτε έλεγχο")
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(mContext,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_GPS_REQUEST_CODE);
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_GPS_REQUEST_CODE);
            }
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, this::setGpsLocation);
            if (requestLocationUpdates) {
                LocationRequest currentLocationRequest = new LocationRequest();
                currentLocationRequest.setInterval(500)
                        .setFastestInterval(0)
                        .setMaxWaitTime(0)
                        .setSmallestDisplacement(0)
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                if (mFusedLocationClient!=null) {
                    mFusedLocationClient.requestLocationUpdates(currentLocationRequest, mLocationCallback, null);
                }
            } else {
                stopLocationUpdates();
            }
        }
    }

    private void showGpsFailure() {
        AlertDialog.Builder gpsAlertDialog = new AlertDialog.Builder(this);
        String msg = "Δεν ήταν δυνατή η ανάγνωση της τοποθεσίας. Δοκιμάσετε αργότερα";
        gpsAlertDialog.setTitle("Αδυναμία πρόσβασης στις υπηρεσίες τοποθεσίας").setMessage(msg)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                });
        gpsAlertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_GPS_REQUEST_CODE:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    showGpsFailure();
                } else {
                    checkLocationPermission();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if (mFusedLocationClient!=null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocationPermission();
    }
    private static class InspectionStepTwoAsyncTask extends AsyncTask<String, String, Set<Rsg>> {

        final WeakReference<InspectionStepTwoActivity> parent;

        InspectionStepTwoAsyncTask(InspectionStepTwoActivity parent) {
            this.parent = new WeakReference<>(parent);
        }

        private final Logger log = Log4jHelper.getLogger(InspectionStepTwoAsyncTask.class.getName());
        protected Set<Rsg> doInBackground(String... strings) {

            int count = strings.length;
            Date inspectionDate;
            if (count != 2) {
                return new HashSet<>();
            }
            try {

                String filename = strings[0];
                String dateString = strings[1];
                inspectionDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(dateString);

                DbHandler dbHandler = new DbHandler(Moo.getAppContext());

                List<Rsg> alreadyCheckedRsgs = EntryRepository.getAlreadyCheckedRsgsFor(dbHandler, inspectionDate);

                return RsgReader.readRsgFromTablet(filename)
                        .stream()
                        .filter(rsg -> alreadyCheckedRsgs.stream().noneMatch(seenRsg -> seenRsg.equals(rsg)))
                        .collect(Collectors.toSet());

            } catch (IOException | ParseException e) {
                log.error("Received exception: " + e.getLocalizedMessage());
                return new HashSet<>();

            }
        }


        protected void onPostExecute(Set<Rsg> rsgs) {
            if (rsgs.isEmpty()) {
                Notifier.notify(Moo.getAppContext(), "Δεν υπάρχουν σκαναρισμένα ενώτια για τον έλεγχο αυτό!", Notifier.NOTIFICATION_MESSAGE_TYPE.INFO_MESSAGE);
            }
            InspectionStepTwoActivity activity = parent.get();
            if (activity==null || activity.isFinishing()) {
                return;
            }
            activity.setOwners(rsgs.stream().map(Rsg::getOwner).collect(Collectors.toSet()));
            TextView animalCountValue = activity.findViewById(R.id.animalCountValue);
            animalCountValue.setText(rsgs.size() + "");
            activity.setRsgs(rsgs);
            Spinner producer1TagSpinner = activity.findViewById(R.id.producer1TagValue);
            producer1TagSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, activity.getOwners().toArray(new String[]{})));

            Spinner producer2TagSpinner = activity.findViewById(R.id.producer2TagValue);
            producer2TagSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, activity.getOwners().toArray(new String[]{})));

            Spinner producer3TagSpinner = activity.findViewById(R.id.producer3TagValue);
            producer3TagSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, activity.getOwners().toArray(new String[]{})));

            Spinner producer4TagSpinner = activity.findViewById(R.id.producer4TagValue);
            producer4TagSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, activity.getOwners().toArray(new String[]{})));

        }

    }
}