package gr.petalidis.datamars;

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by npetalid on 29/10/17.
 */

public class DirectoryScanner extends AsyncTask<Void,Void,List<String>> {

    private WeakReference<TextView> textView;

    public DirectoryScanner(TextView textView) {
        this.textView = new WeakReference<>(textView);
    }

    private List<String> startShowing()
    {
        return recurseIntoFilePath(new ArrayList<String>(),"/storage");
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    private List<String> recurseIntoFilePath(List<String> results, String path)
    {
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
