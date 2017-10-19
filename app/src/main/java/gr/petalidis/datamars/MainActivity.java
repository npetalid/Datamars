package gr.petalidis.datamars;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.squareup.timessquare.CalendarPickerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.MONTH;

public class MainActivity extends AppCompatActivity {

    private ViewFlipper mViewFlipper;
    private Context mContext;
    private float lastX;
    private static final int PERMISSION_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

      //  performFileSearch();
        mContext = this;
        mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);


        CalendarPickerView calendarView = (CalendarPickerView) findViewById(R.id.calendar_view);
        Calendar rightNow = Calendar.getInstance();
        Calendar monthBefore = Calendar.getInstance();
        monthBefore.add(MONTH, -1);
        Calendar monthAfter = Calendar.getInstance();
        monthAfter.add(MONTH, 1);
        rightNow.add(Calendar.DAY_OF_YEAR, -1);

        calendarView.init(monthBefore.getTime(), monthAfter.getTime());

        List<Date> selectedDates = new ArrayList<>();
        selectedDates.add(rightNow.getTime());
        rightNow.add(Calendar.DAY_OF_YEAR, -3);
        selectedDates.add(rightNow.getTime());

        calendarView.highlightDates(selectedDates);

    }

    private void showExternalStorage()
    {

        TextView textView = (TextView) findViewById(R.id.textView);



        String m_str = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        textView.setText(m_str);

        if (isExternalStorageReadable()) {
            textView.append("Is readable\n");
        }


        File f = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        File [] files = f.listFiles();
        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    textView.append(inFile.getAbsolutePath()+"\n");
                }
            }

        }


        recurseIntoFilePath("/");


    }

    public void recurseIntoFilePath(String path)
    {
        TextView textView = (TextView) findViewById(R.id.textView);

        File f = new File(path);
        File [] files = f.listFiles();
        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory() && inFile.canRead()) {
                    textView.append(inFile.getAbsolutePath()+"\n");
                    recurseIntoFilePath(inFile.getAbsolutePath());
                }
            }

        }
    }
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
       // intent.setType("image/*");

        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        TextView textView = (TextView) findViewById(R.id.textView);

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
    // Method to handle touch event like left to right swap and right to left swap
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            // when user first touches the screen to swap
            case MotionEvent.ACTION_DOWN: {
                lastX = touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_UP: {
                float currentX = touchevent.getX();

                // if left to right swipe on screen
                if (lastX < currentX) {
                    // If no more View/Child to flip
                    if (mViewFlipper.getDisplayedChild() == 0)
                        break;

                    // set the required Animation type to ViewFlipper
                    // The Next screen will come in form Left and current Screen will go OUT from Right
                    mViewFlipper.setInAnimation(this, R.anim.in_from_left);
                    mViewFlipper.setOutAnimation(this, R.anim.out_to_right);
                    // Show the next Screen
                    mViewFlipper.showNext();
                }

                // if right to left swipe on screen
                if (lastX > currentX) {
                    if (mViewFlipper.getDisplayedChild() == 1)
                        break;
                    // set the required Animation type to ViewFlipper
                    // The Next screen will come in form Right and current Screen will go OUT from Left
                    mViewFlipper.setInAnimation(this, R.anim.in_from_right);
                    mViewFlipper.setOutAnimation(this, R.anim.out_to_left);
                    // Show The Previous Screen
                    mViewFlipper.showPrevious();
                }
                break;
            }
        }
        return false;
    }


    private void showFailure()
    {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("Failed to get permissions");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    showExternalStorage();
                } else {
                    showFailure();

                }
                break;
        }
    }

}
