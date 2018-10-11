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

public class InspectionViewAdapter extends ArrayAdapter<Entry> {
    private final Context context;
    private final List<Entry> objects;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");


    public InspectionViewAdapter(Context context, int resource, List<Entry> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listitems, parent, false);
        final Entry item = objects.get(position);

        TextView textViewTag = (TextView) rowView.findViewById(R.id.viewTag);
        TextView textViewTime = (TextView) rowView.findViewById(R.id.viewTagTime);
        TextView textViewOwner = (TextView) rowView.findViewById(R.id.viewTagOwner);
        TextView textViewType = (TextView) rowView.findViewById(R.id.viewTagType);
        CheckBox textViewIsInRegister = (CheckBox) rowView.findViewById(R.id.viewIsInRegister);

        textViewTag.setText(item.getTag());
        textViewTime.setText(simpleDateFormat.format(item.getTagDate()));
        textViewOwner.setText(item.getProducer());
        textViewType.setText(item.getAnimalType());
        textViewIsInRegister.setChecked(item.isInRegister());
        textViewIsInRegister.setEnabled(false);

        return rowView;
    }


}
