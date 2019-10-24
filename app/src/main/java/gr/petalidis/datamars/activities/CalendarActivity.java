/*        Copyright 2017 Nikolaos Petalidis
*
*        Licensed under the Apache License, Version 2.0 (the "License");
*        you may not use this file except in compliance with the License.
*        You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*        Unless required by applicable law or agreed to in writing, software
*        distributed under the License is distributed on an "AS IS" BASIS,
*        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*        See the License for the specific language governing permissions and
*        limitations under the License.
*/

package gr.petalidis.datamars.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.timessquare.CalendarPickerView;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.R;
import gr.petalidis.datamars.rsglibrary.RsgSessionFiles;

public class CalendarActivity extends AppCompatActivity {
    private RsgSessionFiles files = new RsgSessionFiles();
    private static final Logger log = Log4jHelper.getLogger(CalendarActivity.class.getName());
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

        CalendarPickerView calendarView = findViewById(R.id.calendar_view);
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

        calendarView.highlightDates(files.getDates());
        calendarView.scrollToDate(cal2.getTime());
        calendarView.setCellClickInterceptor(date -> {
            Intent intent1 = new Intent(mContext, ViewRsgActivity.class);
            try {
                intent1.putExtra("rsg", files.getFilename(date));
                intent1.putExtra("date", SimpleDateFormat.getDateInstance().format(date));
                startActivity(intent1);
                return true;
            } catch (Exception e) {
                log.error("Unable to parse date: " +date + ":" + e.getLocalizedMessage());
            }
            return false;
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable("dates", files);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        files = (RsgSessionFiles) savedInstanceState.getSerializable("dates");
        if (files == null) {
                files = new RsgSessionFiles();
        }
    }


}
