
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

package gr.petalidis.datamars.inspections.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.utilities.TagFormatter;

class InspectionViewAdapter extends ArrayAdapter<Entry> {
    private final Context context;
    private final List<Entry> objects;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");


    public InspectionViewAdapter(Context context, int resource, List<Entry> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.listitems, parent, false);
        }
        final Entry item = objects.get(position);

        TextView textViewTag = rowView.findViewById(R.id.viewTag);
        TextView textViewTime = rowView.findViewById(R.id.viewTagTime);
        TextView textViewOwner = rowView.findViewById(R.id.viewTagOwner);
        TextView textViewType = rowView.findViewById(R.id.viewTagType);
        TextView textViewComment = rowView.findViewById(R.id.viewComments);
        TextView textViewGenre = rowView.findViewById(R.id.viewTagBio);
        CheckBox textViewIsInRegister = rowView.findViewById(R.id.viewIsInRegister);

        textViewTag.setText(TagFormatter.format(item.getTag()));
        textViewTime.setText(simpleDateFormat.format(item.getTagDate()));
        textViewOwner.setText(item.getProducer());
        textViewType.setText(item.getAnimalType());
        textViewIsInRegister.setChecked(item.isInRegister());
        textViewIsInRegister.setEnabled(false);
        textViewComment.setText(item.getComment().getTitle());
        textViewGenre.setText(item.getAnimalGenre());
        return rowView;
    }


}
