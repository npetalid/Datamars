package gr.petalidis.datamars.inspections.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.dto.InspectionDateProducer;

public class CreateInspectionAdapter extends ArrayAdapter<InspectionDateProducer> {
    private final Context context;
    private final List<InspectionDateProducer> objects;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");


    public CreateInspectionAdapter(Context context, int resource, List<InspectionDateProducer> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listinspections, parent, false);
        final InspectionDateProducer item = objects.get(position);

        TextView inspectionDateText = (TextView) rowView.findViewById(R.id.inspectionDate);
        TextView inspectionProducer = (TextView) rowView.findViewById(R.id.inspectionProducer);

        inspectionProducer.setText(item.getMainProducer());
        inspectionDateText.setText(item.getInspectionDate());

        return rowView;
    }


}
