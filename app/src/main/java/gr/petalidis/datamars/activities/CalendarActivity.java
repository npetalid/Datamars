package gr.petalidis.datamars.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.rsglibrary.RsgSessionFiles;

public class CalendarActivity extends AppCompatActivity {
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

        setContentView(R.layout.activity_calendar);

        Intent intent = getIntent();
        if (intent != null) {
            files = (RsgSessionFiles) intent.getSerializableExtra("dates");
            if (files == null) {
                files = new RsgSessionFiles();
            }
        }

        CalendarPickerView calendarView = (CalendarPickerView) findViewById(R.id.calendar_view);
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
        cal2.add(Calendar.MONTH, 1);
        calendarView.init(cal.getTime(), cal2.getTime());

        calendarView.highlightDates(files.getDates());

        calendarView.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                Intent intent = new Intent(mContext, ViewRsgActivity.class);
                try {
                    intent.putExtra("rsg", files.getFilename(date));
                    intent.putExtra("date", SimpleDateFormat.getDateInstance().format(date));
                    startActivity(intent);
                    return true;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable("dates", files);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            files = (RsgSessionFiles) savedInstanceState.getSerializable("dates");
            if (files == null) {
                    files = new RsgSessionFiles();
            }
        }
    }


}
