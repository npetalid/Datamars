package gr.petalidis.datamars.inspections.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.timessquare.CalendarPickerView;

import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.R;
import gr.petalidis.datamars.rsglibrary.RsgSessionFiles;


public class InspectionStepOneActivity extends AppCompatActivity {

    private RsgSessionFiles files = new RsgSessionFiles();

    private static final Logger log = Log4jHelper.getLogger(InspectionStepOneActivity.class.getName());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context mContext = this;

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            files = (RsgSessionFiles) savedInstanceState.getSerializable("dates");
            if (files == null) {
                files = new RsgSessionFiles();
            }
        }

        setContentView(R.layout.activity_inspection_step_one);

        Intent intent = getIntent();
        if (intent != null) {
            files = (RsgSessionFiles) intent.getSerializableExtra("files");
            if (files == null) {
                files = new RsgSessionFiles();
            }
        }

        CalendarPickerView calendarView = findViewById(R.id.inspectionDateText);

        Calendar cal = Calendar.getInstance();

        Date firstDate = new Date();
        Date lastDate = new Date();

        if (!files.getDates().isEmpty()) {
            firstDate = files.getDates().get(0);
            lastDate = files.getDates().get(files.getDates().size() - 1);
        }
        cal.setTime(firstDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(lastDate);
        cal2.add(Calendar.DATE, 1);
        calendarView.init(cal.getTime(), cal2.getTime());

        calendarView.scrollToDate(cal2.getTime());
        calendarView.highlightDates(files.getDates());

        calendarView.setCellClickInterceptor(date -> {
            Intent intent1 = new Intent(mContext, InspectionStepPhotoActivity.class);
            try {
                intent1.putExtra("rsgFilename", files.getFilename(date));
                intent1.putExtra("inspectionDate", date);
                startActivity(intent1);
                return true;
            } catch (Exception e) {
                log.error("Received not valid name and/or date: " + e.getLocalizedMessage());
            }
            return false;
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable("files", files);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            files = (RsgSessionFiles) savedInstanceState.getSerializable("files");
            if (files == null) {
                files = new RsgSessionFiles();
            }
        }
    }
}