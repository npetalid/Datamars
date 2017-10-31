package gr.petalidis.datamars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import gr.petalidis.datamars.rsglibrary.Rsg;

/**
 * Created by npetalid on 31/10/17.
 */

public class RsgAdapter extends ArrayAdapter<Rsg> {
    private final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    public RsgAdapter(Context context, ArrayList<Rsg> rsgs) {
        super(context, 0, rsgs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Rsg rsg = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rsg_item, parent, false);
        }
        // Lookup view for data population
        TextView country = (TextView) convertView.findViewById(R.id.countryId);
        TextView identification = (TextView) convertView.findViewById(R.id.identificationId);
        TextView mydate = (TextView) convertView.findViewById(R.id.dateId);

        // Populate the data into the template view using the data object
        country.setText(rsg.getCountryCode());
        identification.setText(rsg.getIdentificationCode());
        mydate.setText(format.format(rsg.getDate()));
        // Return the completed view to render on screen
        return convertView;
    }
}