/*
 * Copyright 2017-2019 Nikolaos Petalidis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package gr.petalidis.datamars.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.R;
import gr.petalidis.datamars.SessionViewer;
import gr.petalidis.datamars.inspections.domain.UsbAlias;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.UsbAliasService;
import gr.petalidis.datamars.rsglibrary.CsvRootDirectory;


public class ChooseDirectoryFragment extends DialogFragment {
    private static final Logger log = Log4jHelper.getLogger(ChooseDirectoryFragment.class.getName());
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final ArrayList<String> usbs = (ArrayList<String>) args.getSerializable("usbList");
        final String nextClassName = args.getString("nextClassName");
        DbHandler dbHandler = new DbHandler(getContext().getApplicationContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        List<UsbAlias> andUpdateAllUsbs = UsbAliasService.findAndUpdateAllUsbs(dbHandler, usbs);
        if (!andUpdateAllUsbs.isEmpty()) {

            builder.setTitle(R.string.pickUsb);
            UsbListView usbListView = new UsbListView(getContext(),andUpdateAllUsbs);
            usbListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int which, long l) {
                    try {
                        SessionViewer sessionViewer = new SessionViewer(getContext(), nextClassName);
                        CsvRootDirectory csvRootDirectory = new CsvRootDirectory();
                        String fullPath = csvRootDirectory.getDirectory() + File.separator + andUpdateAllUsbs.get(which).getUsb();
                        sessionViewer.execute(fullPath);
                    } catch (IllegalStateException e) {
                        log.error("Unable to read csv Root Directory: {}", e.getLocalizedMessage());
                    }
                }
            });
            usbListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int which, long l) {
                    try {
                        EditListItemDialog editListItemDialog = new EditListItemDialog(getContext(), andUpdateAllUsbs.get(which),usbListView, dbHandler);
                        editListItemDialog.show();
                        return true;
                    } catch (IllegalStateException e) {
                        log.error("Unable to read csv Root Directory: {}", e.getLocalizedMessage());
                        return false;
                    }
                }
            });

            builder.setView(usbListView);
        }
        return builder.create();

    }
}
