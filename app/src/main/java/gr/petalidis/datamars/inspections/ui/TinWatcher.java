package gr.petalidis.datamars.inspections.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import gr.petalidis.datamars.inspections.validators.Validator;

public class TinWatcher implements TextWatcher {

    private Validator validator;
    private final TextView textView;

    public TinWatcher(Validator validator, TextView textView) {
        this.validator = validator;
        this.textView = textView;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String tin = textView.getText().toString();

        if (!validator.isValid(tin)) {
            textView.setError("Παρακαλώ εισάγετε σωστό ΑΦΜ");
        }
    }
}
