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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.CommentType;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.inspections.utilities.TagFormatter;

class InspectionStepThreeAdapter extends ArrayAdapter<Entry> {
    private final Context context;
    private final List<Entry> objects;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
    private final ArrayAdapter<Inspectee> spinnerNamesAdapter;
    private final ArrayAdapter<CharSequence> adapterComments;
    private final ArrayAdapter<CharSequence> adapterAnimals;

    private final Map<String,ArrayAdapter<CharSequence>> animalsToGenres;

    public void revertRegister() {
        objects.forEach(x->x.setInRegister(!x.isInRegister()));
    }


    private class SpinnerListener implements AdapterView.OnItemSelectedListener {
        final private Entry item;
        private final Spinner genreSpinner;

        SpinnerListener(Entry item, Spinner genreSpinner) {
            this.item = item;
            this.genreSpinner = genreSpinner;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
            String selectedItem = (String) parent.getItemAtPosition(pos);
            item.setAnimalType(selectedItem);
            genreSpinner.setAdapter(animalsToGenres.get(item.getAnimalType()));
            genreSpinner.setOnItemSelectedListener(new SpinnerGenreListener(item));
            genreSpinner.setSelection(animalsToGenres.get(item.getAnimalType()).getPosition(item.getAnimalGenre()));
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //do Nothing
        }
    }

    private class SpinnerGenreListener implements AdapterView.OnItemSelectedListener {
        final private Entry item;

        SpinnerGenreListener(Entry item) {
            this.item = item;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
            String selectedItem = (String) parent.getItemAtPosition(pos);
            item.setAnimalGenre(selectedItem);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //do Nothing
        }
    }
    private class SpinnerNamesListener implements AdapterView.OnItemSelectedListener {
        final private Entry item;

        SpinnerNamesListener(Entry item) {
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

    private class SpinnerCommentsListener implements AdapterView.OnItemSelectedListener {
        final private Entry item;
        final private CheckBox checkBox;

        SpinnerCommentsListener(Entry item, CheckBox checkBox) {
            this.item = item;
            this.checkBox = checkBox;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
            String selectedItem = (String) parent.getItemAtPosition(pos);
            item.setComment(CommentType.fromString(selectedItem));
            if (CommentType.SINGLE!=item.getComment()
                && CommentType.EMPTY!=item.getComment()) {
                item.setInRegister(false);
                checkBox.setChecked(item.isInRegister());
            }
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
        spinnerNamesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, producerNames.toArray(new Inspectee[]{}));
        spinnerNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterAnimals = ArrayAdapter.createFromResource(context,
                R.array.animals_array, android.R.layout.simple_spinner_item);
        adapterAnimals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        animalsToGenres = new HashMap<>();

        ArrayAdapter<CharSequence> adapterSheepGenre = ArrayAdapter.createFromResource(context,
                R.array.sheep_genre, android.R.layout.simple_spinner_dropdown_item);
        adapterSheepGenre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterGoatGenre = ArrayAdapter.createFromResource(context,
                R.array.goat_genre, android.R.layout.simple_spinner_dropdown_item);
        adapterGoatGenre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterHorseGenre = ArrayAdapter.createFromResource(context,
                R.array.horse_genre, android.R.layout.simple_spinner_dropdown_item);
        adapterHorseGenre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        animalsToGenres.put(context.getResources().getString(R.string.goatAnimal), adapterGoatGenre);
        animalsToGenres.put(context.getResources().getString(R.string.kidAnimal), adapterGoatGenre);
        animalsToGenres.put(context.getResources().getString(R.string.heGoatAnimal), adapterGoatGenre);

        animalsToGenres.put(context.getResources().getString(R.string.sheepAnimal), adapterSheepGenre);
        animalsToGenres.put(context.getResources().getString(R.string.lambAnimal), adapterSheepGenre);
        animalsToGenres.put(context.getResources().getString(R.string.ramAnimal), adapterSheepGenre);

        animalsToGenres.put(context.getResources().getString(R.string.horseAnimal), adapterHorseGenre);

        adapterComments = ArrayAdapter.createFromResource(context, R.array.comments_array,android.R.layout.simple_spinner_item);
        adapterComments.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.edititems, parent, false);
      }
        final Entry item = objects.get(position);

        TextView textView = rowView.findViewById(R.id.textView);
        TextView tagDateView = rowView.findViewById(R.id.editText2);
        final CheckBox checkBox = rowView.findViewById(R.id.checkBox);

        Spinner spinner = rowView.findViewById(R.id.spinner);
        Spinner genreSpinner = rowView.findViewById(R.id.biospinner);

        spinner.setAdapter(adapterAnimals);


        spinner.setOnItemSelectedListener(new SpinnerListener(item, genreSpinner));

        spinner.setSelection(adapterAnimals.getPosition(item.getAnimalType()));

        genreSpinner.setAdapter(animalsToGenres.get(item.getAnimalType()));
        genreSpinner.setOnItemSelectedListener(new SpinnerGenreListener(item));
        genreSpinner.setSelection(animalsToGenres.get(item.getAnimalType()).getPosition(item.getAnimalGenre()));

        Spinner spinnerNames = rowView.findViewById(R.id.spinnerNames);
        spinnerNames.setAdapter(spinnerNamesAdapter);
        spinnerNames.setOnItemSelectedListener(new SpinnerNamesListener(item));
        spinnerNames.setSelection(spinnerNamesAdapter.getPosition(new Inspectee(item.getProducerTin(), item.getProducer())));

        Spinner spinnerComments = rowView.findViewById(R.id.viewComments);
        spinnerComments.setAdapter(adapterComments);
        spinnerComments.setOnItemSelectedListener(new SpinnerCommentsListener(item,checkBox));
        spinnerComments.setSelection(adapterComments.getPosition(item.getComment().getTitle()));
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
        textView.setText(TagFormatter.format(item.getTag()));
        tagDateView.setText(simpleDateFormat.format(item.getTagDate()));
        checkBox.setChecked(item.isInRegister());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setInRegister(!item.isInRegister());
                checkBox.setChecked(item.isInRegister());
                if (item.isInRegister() &&
                        item.getComment()!=CommentType.EMPTY
                        && item.getComment()!=CommentType.SINGLE) {
                    item.setComment(CommentType.EMPTY);
                    spinnerComments.setSelection(adapterComments.getPosition(item.getComment().getTitle()));

                }
            }
        });
        return rowView;
    }

}
