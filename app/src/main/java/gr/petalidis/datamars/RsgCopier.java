package gr.petalidis.datamars;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
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

public class RsgCopier extends AsyncTask<Object, String, Integer> {

    private WeakReference<TextView> textView;

    private WeakReference<Context> context;

    private RsgSessionFiles sessions = new RsgSessionFiles();

      /* Checks if external storage is available to at least read */

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));
    }

    public RsgCopier(Context context, TextView textView) {
        this.textView = new WeakReference<>(textView);
        this.context = new WeakReference<>(context);

    }


    @Override
    protected Integer doInBackground(Object... params) {
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
        } catch (IllegalStateException  | IOException | ParseException e) {
            e.printStackTrace();
        }


        return numberOfFilesWritter;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        TextView actualView = textView.get();
        if (actualView != null) {
            actualView.setText(values[0]);
        }

    }


    @Override
    protected void onPostExecute(Integer numberOfFilesWritten) {
        super.onPostExecute(numberOfFilesWritten);

        TextView actualView = textView.get();
        if (actualView != null) {
            if (numberOfFilesWritten == 0) {
                actualView.setText("No datamars or no sessions found\n");
            } else {
                actualView.setText("Synced " + numberOfFilesWritten + " files \n");
            }
        }


    }
}
