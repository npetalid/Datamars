package gr.petalidis.datamars;

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static gr.petalidis.datamars.R.id.status_text_view;
import static gr.petalidis.datamars.R.id.textView;

/**
 * Created by npetalid on 29/10/17.
 */

public class DirectoryScanner extends AsyncTask<Void,Void,List<String>> {

    WeakReference<TextView> textView;

    public DirectoryScanner(TextView textView) {
        this.textView = new WeakReference<TextView>(textView);
    }

    private List<String> startShowing()
    {

        List<String> sessionLocations = recurseIntoFilePath(new ArrayList<String>(),"/storage");

    //     sessionLocations = recurseIntoFilePath(sessionLocations,"/");

      //  sessionLocations = recurseIntoFilePath(sessionLocations,"/mnt");

        //String m_str = Environment.getExternalStorageDirectory()
      //          .getAbsolutePath();

      //  if (isExternalStorageReadable()) {
      //      sessionLocations = recurseIntoFilePath(sessionLocations,m_str);

     //   }

        return sessionLocations;
        // textView.setText(m_str);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public List<String> recurseIntoFilePath(List<String> results, String path)
    {

        //TextView textView = (TextView) getView().findViewById(R.id.status_text_view);

        File f = new File(path.toString());
        File [] files = f.listFiles();
        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory() && inFile.canRead() ) {
                    if (inFile.getName().endsWith("Session")) {
                         results.add(inFile.getAbsolutePath());
                        return results;
                    }
                     recurseIntoFilePath(results, inFile.getAbsolutePath());
                }
            }

        }
        return results;
    }
    @Override
    protected List<String> doInBackground(Void... params) {
        return startShowing();
    }

    @Override
    protected void onPostExecute(List<String> s) {
        super.onPostExecute(s);
        TextView actualView = textView.get();
        if (actualView!=null) {
            for (String result:s) {
                actualView.append(result + "\n");
            }
            actualView.append("\n---End---, V1");
        }
    }
}
