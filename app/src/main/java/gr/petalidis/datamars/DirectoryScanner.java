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

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

class DirectoryScanner extends AsyncTask<Void,Void,List<String>> {

    private final WeakReference<TextView> textView;

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
        File f = new File(path);
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
