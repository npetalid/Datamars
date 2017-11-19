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

package gr.petalidis.datamars.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.SessionViewer;
import gr.petalidis.datamars.rsglibrary.CsvRootDirectory;
import gr.petalidis.datamars.rsglibrary.RsgRootDirectory;


public class ChooseDirectoryFragment extends DialogFragment {
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final ArrayList<String> usbs = (ArrayList<String>) args.getSerializable("usbList");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        if (usbs != null && !usbs.isEmpty()) {

            builder.setTitle(R.string.pickUsb)
                    .setItems(usbs.toArray(new String[usbs.size()]), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                SessionViewer sessionViewer = new SessionViewer(getContext());
                                CsvRootDirectory csvRootDirectory = new CsvRootDirectory();
                                String fullPath = csvRootDirectory.getDirectory() + File.separator + usbs.get(which);
                                sessionViewer.execute(fullPath);
                            } catch (IllegalStateException e) {
                                //Do nothing
                            }
                        }
                    });
        }
        return builder.create();

    }
}
