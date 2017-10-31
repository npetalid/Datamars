package gr.petalidis.datamars;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.rsglibrary.RsgSessionFiles;

import static java.util.Calendar.MONTH;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent intent = getIntent();
        RsgSessionFiles files = (RsgSessionFiles)intent.getSerializableExtra("dates");


        CalendarPickerView calendarView = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date firstDate = files.getDates().get(0);
        Date lastDate = files.getDates().get(files.getDates().size()-1);

        Calendar cal = Calendar.getInstance();

        cal.setTime(firstDate);
        cal.add(Calendar.MONTH,-1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(lastDate);
        cal2.add(Calendar.MONTH,1);
        calendarView.init(cal.getTime(),cal2.getTime());

        calendarView.highlightDates(files.getDates());

    }
}
