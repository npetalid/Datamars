package gr.petalidis.datamars;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.rsglibrary.RsgSessionFiles;

import static java.util.Calendar.MONTH;

public class CalendarActivity extends AppCompatActivity {
    private Context mContext;
    private RsgSessionFiles files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent intent = getIntent();
        files = (RsgSessionFiles)intent.getSerializableExtra("dates");

        mContext = this;

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
        calendarView.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                Intent intent = new Intent(mContext, ViewRsgActivity.class);
                try {
                    intent.putExtra("rsg", files.getFilename(date));
                    startActivity(intent);
                    return true;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }
}
