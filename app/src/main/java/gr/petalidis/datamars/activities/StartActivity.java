package gr.petalidis.datamars.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.fragments.AppStatusFragment;
import gr.petalidis.datamars.fragments.ChooseDirectoryFragment;
import gr.petalidis.datamars.rsglibrary.RsgRootDirectory;

public class StartActivity extends AppCompatActivity implements AppStatusFragment.OnFragmentInteractionListener,  ChooseDirectoryFragment.OnFragmentInteractionListener {
    private static final int PERMISSION_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showDialog(View view)
    {
        FragmentManager fragmentManager = getFragmentManager();
        RsgRootDirectory rsgRootDirectory = new RsgRootDirectory();



        ChooseDirectoryFragment fragment = new ChooseDirectoryFragment();
        Bundle args = new Bundle();
        args.putSerializable("usbList",rsgRootDirectory.getProbableUsbs());
        fragment.setArguments(args);
        fragment.show(fragmentManager,"Some tag");

    }

    public void showExternalStorage(View view)
    {
        FragmentManager fragmentManager = getFragmentManager();


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(R.id.statusFragmentId);
        if (fragment!=null) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();

        fragment = new AppStatusFragment();
        fragmentTransaction.add(R.id.linear_layout_fragment, fragment);

        fragmentTransaction.commit();
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
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                textView.append(uri.getPath()+"\n");

            }
        }
    }


    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void showFailure()
    {
        TextView textView = (TextView) findViewById(R.id.status_text_view);
        textView.setText("Failed to get permissions");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    showFailure();

                }
                break;
        }
    }
}
