package gr.petalidis.datamars.inspections.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.utilities.WGS84Converter;
import gr.petalidis.datamars.inspections.validators.TinValidator;
import gr.petalidis.datamars.rsglibrary.Rsg;
import gr.petalidis.datamars.rsglibrary.RsgReader;

public class InspectionStepTwoActivity extends AppCompatActivity {
    private static final int PERMISSION_GPS_REQUEST_CODE = 2;

    private Inspection inspection;
    private String filename = "";

    private Date inspectionDate;
    private Set<Rsg> rsgs = new HashSet<>();
    private String producer1Tag = "";
    private String producer2Tag = "";
    private String producer3Tag = "";
    private String producer4Tag = "";
    Set<String> owners = new HashSet<>();
    private FusedLocationProviderClient mFusedLocationClient;

    private Location gpsLocation = null;
    private class SpinnerListener implements AdapterView.OnItemSelectedListener {
        private String producerTag;

        public SpinnerListener(String producerTag) {
            this.producerTag = producerTag;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
            producerTag = (String) parent.getItemAtPosition(pos);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //do Nothing
        }

        public String getSelection()
        {
            return producerTag;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // recovering the instance state
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (savedInstanceState != null) {
            inspection = (Inspection) savedInstanceState.getSerializable("inspection");
            inspectionDate = (Date) savedInstanceState.getSerializable("inspectionDate");
            filename = savedInstanceState.getString("rsgFilename");
        } else {
            inspectionDate = (Date) getIntent().getExtras().getSerializable("inspectionDate");
            filename = getIntent().getExtras().getString("rsgFilename");
        }
        try {
            rsgs.addAll(RsgReader.readRsgFromTablet(filename));
            owners = rsgs.stream().map(x->x.getOwner()).collect(Collectors.toSet());

            setContentView(R.layout.activity_inspection_step_two);
            setAnimalCount(rsgs.size());
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
            setProducer1Tag();
            setProducer2Tag();
            setProducer3Tag();
            setProducer4Tag();
            EditText tin = findViewById(R.id.producer1TinText);
            tin.addTextChangedListener(new TinWatcher(new TinValidator(), tin));
            tin =  findViewById(R.id.producer2TinText);
            tin.addTextChangedListener(new TinWatcher(new TinValidator(), tin));
            tin = findViewById(R.id.producer3TinText);
            tin.addTextChangedListener(new TinWatcher(new TinValidator(), tin));
            tin =  findViewById(R.id.producer4TinText);
            tin.addTextChangedListener(new TinWatcher(new TinValidator(), tin));

            AlertDialog.Builder gpsAlertDialog = new AlertDialog.Builder(this);
            checkLocationPermission(this);

            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                String msg = "Δε δώσατε δικαιώματα πρόσβασης στο GPS.\n Δεν ήταν δυνατή η ανάγνωση των συντεταγμένων της τοποθεσίας";
                gpsAlertDialog.setTitle("Αδυναμία πρόσβασης στις υπηρεσίες τοποθεσίας").setMessage(msg)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                gpsAlertDialog.show();
            } else {

            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("inspection", inspection);
        outState.putString("rsgFilename", filename);
        outState.putSerializable("inspectionDate", inspectionDate);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    private boolean formHasErrors() {
        boolean hasErrors = false;
        TinValidator validator = new TinValidator();
        EditText tin = (EditText) findViewById(R.id.producer1TinText);
        String tinString = tin.getText().toString();
        EditText producer1Name = (EditText) findViewById(R.id.producer1NameText);
        String producer1NameString = producer1Name.getText().toString();

        EditText tin2 = (EditText) findViewById(R.id.producer2TinText);
        String tinString2 = tin2.getText().toString();


        EditText tin3 = (EditText) findViewById(R.id.producer3TinText);
        String tinString3 = tin3.getText().toString();

        EditText tin4 = (EditText) findViewById(R.id.producer4TinText);
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

    public void goToInspectionStepThreeActivity(View view) {

        if (!formHasErrors()) {

            Spinner producer1TagSpinner = findViewById(R.id.producer1TagValue);
            String producer1Tag = (String)producer1TagSpinner.getSelectedItem();
            Spinner producer2TagSpinner = findViewById(R.id.producer2TagValue);
            String producer2Tag = (String)producer2TagSpinner.getSelectedItem();
            Spinner producer3TagSpinner = findViewById(R.id.producer3TagValue);
            String producer3Tag = (String)producer3TagSpinner.getSelectedItem();
            Spinner producer4TagSpinner = findViewById(R.id.producer4TagValue);
            String producer4Tag = (String)producer4TagSpinner.getSelectedItem();

            Intent intent = new Intent(this, InspectionStepThreeActivity.class);

            inspection = new Inspection(inspectionDate, getProducer1Tin(), getProducer1Name());

            inspection.setProducer2Name(getProducer2Name());
            inspection.setProducer2Tin(getProducer2Tin());

            inspection.setProducer3Name(getProducer3Name());
            inspection.setProducer3Tin(getProducer3Tin());

            inspection.setProducer4Name(getProducer4Name());
            inspection.setProducer4Tin(getProducer4Tin());

            double[] coordinates = {0.0,0.0};
            if (gpsLocation!=null) {
                coordinates = WGS84Converter.toGGRS87(gpsLocation.getLatitude(),gpsLocation.getLongitude());
            }
            inspection.setLatitude(coordinates[0]);
            inspection.setLongitude(coordinates[1]);
            inspection.initEntries(rsgs);

            inspection.getEntries().stream().filter(x->x.getOwner().equals(producer1Tag)).forEach(x->{
                x.setProducerTin(inspection.getProducer1Tin());
                x.setProducer(inspection.getProducer1Name());
            });

            if (!inspection.getProducer2Tin().isEmpty()) {
                inspection.getEntries().stream().filter(x->x.getOwner().equals(producer2Tag)).forEach(x->{
                    x.setProducerTin(inspection.getProducer2Tin());
                    x.setProducer(inspection.getProducer2Name());
                });
            }

            if (!inspection.getProducer3Tin().isEmpty()) {
                inspection.getEntries().stream().filter(x->x.getOwner().equals(producer3Tag)).forEach(x->{
                    x.setProducerTin(inspection.getProducer3Tin());
                    x.setProducer(inspection.getProducer3Name());
                });
            }
            if (!inspection.getProducer4Tin().isEmpty()) {
                inspection.getEntries().stream().filter(x->x.getOwner().equals(producer4Tag)).forEach(x->{
                    x.setProducerTin(inspection.getProducer4Tin());
                    x.setProducer(inspection.getProducer4Name());
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
        EditText name = (EditText) findViewById(R.id.producer1NameText);
        return name.getText().toString().trim();
    }

    private String getProducer1Tin() {
        EditText tin = (EditText) findViewById(R.id.producer1TinText);
        return tin.getText().toString().trim();
    }

    private String getProducer2Name() {
        EditText name = (EditText) findViewById(R.id.producer2NameText);
        return name.getText().toString().trim();
    }

    private String getProducer2Tin() {
        EditText tin = (EditText) findViewById(R.id.producer2TinText);
        return tin.getText().toString().trim();
    }

    private String getProducer3Name() {
        EditText name = (EditText) findViewById(R.id.producer3NameText);
        return name.getText().toString().trim();
    }

    private String getProducer3Tin() {
        EditText tin = (EditText) findViewById(R.id.producer3TinText);
        return tin.getText().toString().trim();
    }

    private String getProducer4Name() {
        EditText name = (EditText) findViewById(R.id.producer4NameText);
        return name.getText().toString().trim();
    }

    private String getProducer4Tin() {
        EditText tin = (EditText) findViewById(R.id.producer4TinText);
        return tin.getText().toString().trim();
    }

    private void setProducer1Name() {
        EditText name = (EditText) findViewById(R.id.producer1NameText);
        if (inspection != null) name.setText(inspection.getProducer1Name());
    }

    private void setProducer1Tin() {
        EditText tin = (EditText) findViewById(R.id.producer1TinText);
        if (inspection != null) tin.setText(inspection.getProducer1Tin());
    }

    private void setProducer2Name() {
        EditText name = (EditText) findViewById(R.id.producer2NameText);
        if (inspection != null) name.setText(inspection.getProducer2Name());
    }

    private void setProducer2Tin() {
        EditText tin = (EditText) findViewById(R.id.producer2TinText);
        if (inspection != null) tin.setText(inspection.getProducer2Tin());
    }

    private void setProducer3Name() {
        EditText name = (EditText) findViewById(R.id.producer3NameText);
        if (inspection != null) name.setText(inspection.getProducer3Name());
    }

    private void setProducer3Tin() {
        EditText tin = (EditText) findViewById(R.id.producer3TinText);
        if (inspection != null) tin.setText(inspection.getProducer3Tin());
    }

    private void setProducer4Name() {
        EditText name = (EditText) findViewById(R.id.producer4NameText);
        if (inspection != null) name.setText(inspection.getProducer4Name());
    }

    private void setProducer1Tag() {
        Spinner producer1TagSpinner = findViewById(R.id.producer1TagValue);
        producer1TagSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, owners.toArray(new String[]{})));
    }
    private void setProducer2Tag() {
        Spinner producer2TagSpinner = findViewById(R.id.producer2TagValue);
        producer2TagSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, owners.toArray(new String[]{})));
    }
    private void setProducer3Tag() {
        Spinner producer3TagSpinner = findViewById(R.id.producer3TagValue);
        producer3TagSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, owners.toArray(new String[]{})));
    }
    private void setProducer4Tag() {
        Spinner producer4TagSpinner = findViewById(R.id.producer4TagValue);
        producer4TagSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, owners.toArray(new String[]{})));
    }
    private void setProducer4Tin() {
        EditText tin = (EditText) findViewById(R.id.producer4TinText);
        if (inspection != null) tin.setText(inspection.getProducer4Tin());
    }

    private void setAnimalCount(int value) {
        TextView animalCountValue = (TextView) findViewById(R.id.animalCountValue);
        animalCountValue.setText(value + "");
    }


    private void setGpsLocation(Location location) {
        gpsLocation = location;
        double[] coordinates = {0.0,0.0};
        if (gpsLocation!=null) {
           coordinates = WGS84Converter.toGGRS87(gpsLocation.getLatitude(),gpsLocation.getLongitude());
        }
        TextView gpsLocation = (TextView) findViewById(R.id.gpsValue);
        gpsLocation.setText(coordinates[0] + "," + coordinates[1] + " με ακρίβεια 68% εντός:" + location.getAccuracy() + " μέτρων");
    }

    public boolean checkLocationPermission(Activity thisActivity) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // TODO: Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("Αίτημα χρήσης υπηρεσιών τοποθεσίας")
                        .setMessage("Η τοποθεσία χρησιμοποιείται για την τοποθέτηση του στάβλου όταν καταχωρείτε έλεγχο")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(thisActivity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_GPS_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_GPS_REQUEST_CODE);
            }
            return false;
        } else {
            AlertDialog.Builder gpsAlertDialog = new AlertDialog.Builder(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                setGpsLocation(location);
                            } else {
                                showGpsFailure();
                            }
                        }
                    });
            return true;
        }
    }

    private void showGpsFailure() {
        AlertDialog.Builder gpsAlertDialog = new AlertDialog.Builder(this);
        String msg = "Δεν ήταν δυνατή η ανάγνωση της τοποθεσίας. Δοκιμάσετε αργότερα";
        gpsAlertDialog.setTitle("Αδυναμία πρόσβασης στις υπηρεσίες τοποθεσίας").setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        gpsAlertDialog.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_GPS_REQUEST_CODE:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    showGpsFailure();
                } else {
                    checkLocationPermission(this);
                }
                break;
        }
    }


}