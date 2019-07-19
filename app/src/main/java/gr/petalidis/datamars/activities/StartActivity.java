package gr.petalidis.datamars.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.fragments.AppStatusFragment;
import gr.petalidis.datamars.fragments.ChooseDirectoryFragment;
import gr.petalidis.datamars.inspections.ui.CreateInspectionActivity;
import gr.petalidis.datamars.rsglibrary.CsvRootDirectory;
import gr.petalidis.datamars.rsglibrary.RsgRootDirectory;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


public class StartActivity extends AppCompatActivity implements AppStatusFragment.OnFragmentInteractionListener,  ChooseDirectoryFragment.OnFragmentInteractionListener {
    private static final String TAG = StartActivity.class.getName();
    private static final int PERMISSION_READWRITE_REQUEST_CODE =1;

    private static final String FRAGMENT_TAG = "gr.petalidis.datamars.status_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READWRITE_REQUEST_CODE);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showDialog(View view)
    {
        FragmentManager fragmentManager = getFragmentManager();
        String currentUsbName = "";
        try {
            RsgRootDirectory rsgRootDirectory = new RsgRootDirectory();
            currentUsbName = rsgRootDirectory.getUsbName();
        } catch (IllegalStateException e) {
            Log.w(TAG, "Unable to read RsgRootDirectory: " + e.getLocalizedMessage());
        }

        CsvRootDirectory csvRootDirectory = new CsvRootDirectory();


        ChooseDirectoryFragment fragment = new ChooseDirectoryFragment();

        Bundle args = new Bundle();
        args.putSerializable("usbList",csvRootDirectory.getProbableUsbs(currentUsbName));
        args.putString("nextClassName",CalendarActivity.class.getName());

        fragment.setArguments(args);
        fragment.show(fragmentManager,"Some tag");

    }

    public void showExternalStorage(View view)
    {
        FragmentManager fragmentManager = getFragmentManager();


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (fragment!=null) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();

        fragment = new AppStatusFragment();
        fragmentTransaction.add(R.id.linear_layout_fragment, fragment, FRAGMENT_TAG);

        fragmentTransaction.commit();
    }

    public void goToInspections(View view)
    {
        FragmentManager fragmentManager = getFragmentManager();
        String currentUsbName = "";
        try {
            RsgRootDirectory rsgRootDirectory = new RsgRootDirectory();
            currentUsbName = rsgRootDirectory.getUsbName();
        } catch (IllegalStateException e) {
            Log.w(TAG, "Unable to read RsgRootDirectory: " + e.getLocalizedMessage());
        }

        CsvRootDirectory csvRootDirectory = new CsvRootDirectory();


        ChooseDirectoryFragment fragment = new ChooseDirectoryFragment();

        Bundle args = new Bundle();
        args.putSerializable("usbList",csvRootDirectory.getProbableUsbs(currentUsbName));
        args.putString("nextClassName",CreateInspectionActivity.class.getName());

        fragment.setArguments(args);
        fragment.show(fragmentManager,"Some tag");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        TextView textView = (TextView) findViewById(R.id.status_text_view);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if (resultData != null) {
                Uri uri = resultData.getData();
                if (uri!=null && uri.getPath()!=null) {
                    textView.append(uri.getPath() + "\n");
                }

            }
        }
    }


    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    private void showFailure(String msg)
    {
        TextView textView = (TextView) findViewById(R.id.status_text_view);
        textView.setText(msg);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull  int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_READWRITE_REQUEST_CODE:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    showFailure(getResources().getString(R.string.failedReadWritePermissionsText));
                }
                break;
        }
    }

}
