package gr.petalidis.datamars.inspections.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;

import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.activities.ViewRsgActivity;
import gr.petalidis.datamars.rsglibrary.RsgSessionFiles;


public class InspectionStepOneActivity extends AppCompatActivity {

    private RsgSessionFiles files = new RsgSessionFiles();

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

        CalendarPickerView calendarView = (CalendarPickerView) findViewById(R.id.inspectionDateText);
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

        calendarView.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                Intent intent = new Intent(mContext, InspectionStepTwoActivity.class);
                try {
                    intent.putExtra("rsgFilename", files.getFilename(date));
                    intent.putExtra("inspectionDate", date);
                    startActivity(intent);
                    return true;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }
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