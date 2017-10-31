package gr.petalidis.datamars;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.timessquare.CalendarPickerView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import gr.petalidis.datamars.rsglibrary.DateScanner;
import gr.petalidis.datamars.rsglibrary.Rsg;
import gr.petalidis.datamars.rsglibrary.RsgExporter;
import gr.petalidis.datamars.rsglibrary.RsgReader;
import gr.petalidis.datamars.rsglibrary.RsgRootDirectory;
import gr.petalidis.datamars.rsglibrary.RsgSessionFiles;

import static java.util.Calendar.MONTH;

/**
 * Created by npetalid on 29/10/17.
 */

public class RsgCopier extends AsyncTask<Object, String, String> {

    WeakReference<TextView> textView;
    WeakReference<Button> button;

    WeakReference<Context> context;

    HashMap<String, String> sessions = new HashMap<>();
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

    public RsgCopier(Context context, TextView textView, Button button ) {
        this.textView = new WeakReference<TextView>(textView);
        this.button = new WeakReference<Button>(button);
        this.context = new WeakReference<Context>(context);

    }


    @Override
    protected String doInBackground(Object... params) {
        RsgRootDirectory rootDirectories = new RsgRootDirectory();
        String m_str = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            return "Could not write to external storage";
        }

        if (rootDirectories.rsgRoots().isEmpty() || rootDirectories.rsgRoots().size()>1) {
            return "Could not find a connected Datamars device";
        }

        publishProgress("Datamars located");

        int numberOfSessionsLocated = 0;
        int numberOfFilesWritter = 0;
        for (String rootDirectory : rootDirectories.rsgRoots()) {
            sessions = DateScanner.scanDirectory(rootDirectory);
            numberOfSessionsLocated += sessions.size();
            publishProgress(numberOfSessionsLocated + " sessions located");
            for (String date: sessions.keySet()) {
                    try {
                        List<Rsg> rsgs = RsgReader.readRsg(sessions.get(date));
                        String filename = RsgExporter.export(rsgs, m_str);
                        if (!filename.equals("")) {
                            publishProgress("Wrote file "+ filename);
                            numberOfFilesWritter ++;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
            }
        }

        return "Wrote " + numberOfFilesWritter+ " files under " + m_str;
    }
    @Override
    protected void onProgressUpdate(String... values) {
        TextView actualView = textView.get();
        if (actualView!=null) {
            actualView.setText(values[0]);
        }

    }



    @Override
    protected void onPostExecute(String s) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        super.onPostExecute(s);
        TextView actualView = textView.get();
        if (actualView!=null) {
                actualView.setText(s + "\n");

        }

        Button actualButton = button.get();
        if (actualButton!=null) {
            actualButton.setText("Click to see results");
            actualButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context actualContext = context.get();
                    if (actualContext!=null && sessions!=null) {

                        try {
                            RsgSessionFiles files = new RsgSessionFiles(sessions);

                            Intent intent = new Intent(actualContext, CalendarActivity.class);
                            intent.putExtra("dates", files);
                            actualContext.startActivity(intent);
                        } catch (ParseException e) {
                            //
                        }
                    }
                }
            });
        }

    }
}
