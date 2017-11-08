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

public class RsgCopier extends AsyncTask<Object, String, String> {

    WeakReference<TextView> textView;
    WeakReference<Button> button;

    WeakReference<Context> context;

    RsgSessionFiles sessions = new RsgSessionFiles();

      /* Checks if external storage is available to at least read */

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public RsgCopier(Context context, TextView textView, Button button) {
        this.textView = new WeakReference<TextView>(textView);
        this.button = new WeakReference<Button>(button);
        this.context = new WeakReference<Context>(context);

    }


    @Override
    protected String doInBackground(Object... params) {
        int numberOfSessionsLocated = 0;
        int numberOfFilesWritter = 0;

        try {

            RsgRootDirectory rsgRootDirectory = new RsgRootDirectory();

            publishProgress("Datamars located");


            RsgSessionFiles rsgSessionFiles = RsgSessionScanner.scanDirectory(rsgRootDirectory.rsgRoot());
            numberOfSessionsLocated += rsgSessionFiles.getSessions().size();
            publishProgress(numberOfSessionsLocated + " sessions located");
            for (RsgSession rsgSession : rsgSessionFiles.getSessions()) {
                List<Rsg> rsgs = RsgReader.readRsgFromScanner(rsgSession.getFilepath());
                String filename = RsgExporter.export(rsgs, rsgRootDirectory.getCsvDirectory(), rsgSession.getCsvName());
                if (!filename.equals("")) {
                    publishProgress("Wrote file " + filename);
                    numberOfFilesWritter++;
                }
            }

            sessions = RsgSessionScanner.scanUsbDirectory(rsgRootDirectory.getCsvDirectory());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return "Wrote " + numberOfFilesWritter + " files";
    }

    @Override
    protected void onProgressUpdate(String... values) {
        TextView actualView = textView.get();
        if (actualView != null) {
            actualView.setText(values[0]);
        }

    }


    @Override
    protected void onPostExecute(String s) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        super.onPostExecute(s);
        TextView actualView = textView.get();
        if (actualView != null) {
            actualView.setText(s + "\n");

        }

        Button actualButton = button.get();
        if (actualButton != null) {
            actualButton.setText("Ο Συγχρονισμός ολοκληρώθηκε. Ξαναδοκιμάστε");
            actualButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context actualContext = context.get();
                    if (actualContext != null && sessions != null) {
                         Intent intent = new Intent(actualContext, CalendarActivity.class);
                            intent.putExtra("dates", sessions);
                            actualContext.startActivity(intent);
                    }
                }
            });
        }

    }
}
