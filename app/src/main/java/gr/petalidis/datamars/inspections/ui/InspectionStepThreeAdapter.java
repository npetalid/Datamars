package gr.petalidis.datamars.inspections.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.domain.Inspectee;

public class InspectionStepThreeAdapter extends ArrayAdapter<Entry> {
    private final Context context;
    private final List<Entry> objects;
    private final List<Inspectee> producerNames;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
    private final ArrayAdapter<Inspectee> spinnerNamesAdapter;
    private final ArrayAdapter<CharSequence> adapter;
    private class SpinnerListener implements AdapterView.OnItemSelectedListener {
        final private Entry item;

        public SpinnerListener(Entry item) {
            this.item = item;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
            String selectedItem = (String) parent.getItemAtPosition(pos);
            item.setAnimalType(selectedItem);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //do Nothing
        }
    }

    private class SpinnerNamesListener implements AdapterView.OnItemSelectedListener {
        final private Entry item;

        public SpinnerNamesListener(Entry item) {
            this.item = item;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
            Inspectee selectedItem = (Inspectee) parent.getItemAtPosition(pos);
            item.setProducer(selectedItem.getName());
            item.setProducerTin(selectedItem.getTin());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //do Nothing
        }
    }

    public InspectionStepThreeAdapter(Context context, int resource, List<Inspectee> producerNames, List<Entry> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.producerNames = producerNames;
        spinnerNamesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, producerNames.toArray(new Inspectee[]{}));
        spinnerNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter = ArrayAdapter.createFromResource(context,
                R.array.animals_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.edititems, parent, false);
        final Entry item = objects.get(position);

        TextView textView = (TextView) rowView.findViewById(R.id.textView);
        TextView tagDateView = (TextView) rowView.findViewById(R.id.editText2);
        final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);

        Spinner spinner = (Spinner) rowView.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerListener(item));
        spinner.setSelection(adapter.getPosition(item.getAnimalType()));

        Spinner spinnerNames = (Spinner) rowView.findViewById(R.id.spinnerNames);
        spinnerNames.setAdapter(spinnerNamesAdapter);
        spinnerNames.setOnItemSelectedListener(new SpinnerNamesListener(item));
        spinnerNames.setSelection(spinnerNamesAdapter.getPosition(new Inspectee(item.getProducerTin(),item.getProducer())));
//
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    item.setTagDate(new BigDecimal(v.getText().toString()));
//                    return true;
//                }
//                return false;
//            }
//        });
        textView.setText(item.getTag());
        tagDateView.setText(simpleDateFormat.format(item.getTagDate()));
        checkBox.setChecked(item.isInRegister());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setInRegister(!item.isInRegister());
                checkBox.setChecked(item.isInRegister());
            }
        });
        return rowView;
    }


}
