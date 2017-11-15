package gr.petalidis.datamars.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.RsgCopier;
import gr.petalidis.datamars.SessionViewer;
import gr.petalidis.datamars.rsglibrary.RsgRootDirectory;


public class ChooseDirectoryFragment extends DialogFragment  {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "usbs";
//
//
//    private OnFragmentInteractionListener mListener;
//
//    public ChooseDirectoryFragment() {
//
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param usbs A list of strings (directory names) that correspond to different
//     *            scanners that were attached at one point in time to this device
//     * @return A new instance of fragment ChooseDirectoryFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ChooseDirectoryFragment newInstance(ArrayList<String> usbs) {
//        ChooseDirectoryFragment fragment = new ChooseDirectoryFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(ARG_PARAM1, usbs);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            usbs = (ArrayList<String>)getArguments().getSerializable(ARG_PARAM1);
//        }
//    }
//
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        // Inflate the layout for this fragment
////        return inflater.inflate(R.layout.fragment_choose_directory, container, false);
////    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final ArrayList<String> usbs = (ArrayList<String>)args.getSerializable("usbList");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       // builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
         //   public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
          //  }
        //});
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.setTitle(R.string.pickUsb)
                .setItems(usbs.toArray(new String[usbs.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SessionViewer sessionViewer = new SessionViewer(getContext());
                        String fullPath = RsgRootDirectory.getCsvPath(usbs.get(which));
                        sessionViewer.execute(fullPath);
                    }
                });
        return builder.create();
    }
}
