/*
     Copyright 2017 Nikolaos Petalidis
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
 */


package gr.petalidis.datamars;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.log4j.Logger;

import java.lang.ref.WeakReference;
import java.text.ParseException;

import gr.petalidis.datamars.activities.CalendarActivity;
import gr.petalidis.datamars.rsglibrary.RsgSessionFiles;
import gr.petalidis.datamars.rsglibrary.RsgSessionScanner;

public class SessionViewer extends AsyncTask<String, String, RsgSessionFiles> {
    private static final Logger log = Log4jHelper.getLogger(SessionViewer.class.getName());

    private WeakReference<Context> context;
    private WeakReference<String> nextActivityName;

    public SessionViewer(Context context, String nextActivityName) {
        this.context = new WeakReference<>(context);
        this.nextActivityName = new WeakReference<>(nextActivityName);
    }


    @Override
    protected RsgSessionFiles doInBackground(String... params) {
        RsgSessionFiles sessions = new RsgSessionFiles();

        String selectedUsb = params[0];

        try {
            sessions = RsgSessionScanner.scanUsbDirectory(selectedUsb);
        } catch (ParseException e) {
            log.error("Unable to scan Usb Directory: " + e.getLocalizedMessage());
        }

        return sessions;
    }


    @Override
    protected void onPostExecute(RsgSessionFiles sessions) {
        super.onPostExecute(sessions);

        Context actualContext = context.get();
        Class nextActivity = null;
        try {
              nextActivity = Class.forName(nextActivityName.get());
        } catch (ClassNotFoundException e) {
            nextActivity = CalendarActivity.class;
        }
        if (actualContext != null && sessions != null) {
            Intent intent = new Intent(actualContext, nextActivity);
            intent.putExtra("dates", sessions);
            actualContext.startActivity(intent);
        }

    }
}
