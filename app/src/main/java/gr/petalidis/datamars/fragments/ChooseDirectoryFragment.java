package gr.petalidis.datamars.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.SessionViewer;
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
                            SessionViewer sessionViewer = new SessionViewer(getContext());
                            String fullPath = RsgRootDirectory.getCsvPath(usbs.get(which));
                            sessionViewer.execute(fullPath);
                        }
                    });
        }
        return builder.create();

    }
}
