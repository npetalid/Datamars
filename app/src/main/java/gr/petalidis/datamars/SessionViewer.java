package gr.petalidis.datamars;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import gr.petalidis.datamars.rsglibrary.Rsg;
import gr.petalidis.datamars.rsglibrary.RsgExporter;
import gr.petalidis.datamars.rsglibrary.RsgReader;
import gr.petalidis.datamars.rsglibrary.RsgRootDirectory;
import gr.petalidis.datamars.rsglibrary.RsgSession;
import gr.petalidis.datamars.rsglibrary.RsgSessionFiles;
import gr.petalidis.datamars.rsglibrary.RsgSessionScanner;

/**
 * Created by npetalid on 29/10/17.
 */

public class SessionViewer extends AsyncTask<String, String, RsgSessionFiles> {

    WeakReference<Context> context;


    public SessionViewer(Context context) {
        this.context = new WeakReference<Context>(context);
    }


    @Override
    protected RsgSessionFiles doInBackground(String... params) {
        RsgSessionFiles sessions = new RsgSessionFiles();

        String selectedUsb = (String) params[0];


        try {
            sessions = RsgSessionScanner.scanUsbDirectory(selectedUsb);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sessions;
    }


    @Override
    protected void onPostExecute(RsgSessionFiles sessions) {
        super.onPostExecute(sessions);

        Context actualContext = context.get();
        if (actualContext != null && sessions != null) {
            Intent intent = new Intent(actualContext, CalendarActivity.class);
            intent.putExtra("dates", sessions);
            actualContext.startActivity(intent);
        }

    }
}
