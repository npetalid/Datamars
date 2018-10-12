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


package gr.petalidis.datamars.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.rsglibrary.Rsg;

public class RsgAdapter extends ArrayAdapter<Rsg> {
    private final SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

    public RsgAdapter(Context context, ArrayList<Rsg> rsgs) {
        super(context, 0, rsgs);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Rsg rsg = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rsg_item, parent, false);
        }
        // Lookup view for data population
        TextView country = (TextView) convertView.findViewById(R.id.countryId);
        TextView identification = (TextView) convertView.findViewById(R.id.identificationId);
        TextView myDate = (TextView) convertView.findViewById(R.id.dateId);

        if (rsg!=null) {
            country.setText(rsg.getCountryCode());
            identification.setText(rsg.getIdentificationCode());
            myDate.setText(format.format(rsg.getDate()));
        }
        return convertView;
    }
}