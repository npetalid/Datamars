
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

package gr.petalidis.datamars.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.UsbAlias;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.UsbAliasService;

class EditListItemDialog extends Dialog implements View.OnClickListener {
    private final UsbAlias item;
    private  EditText editText;
    private final UsbListView usbListView;
    private final DbHandler dbHandler;
    public EditListItemDialog(Context context, UsbAlias item, UsbListView usbListView, DbHandler dbHandler) {
        super(context);
        this.item = item;
        this.usbListView = usbListView;
        this.dbHandler = dbHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item_layout);//here is your xml with EditText and 'Ok' and 'Cancel' buttons
        Button btnOk = findViewById(R.id.okButton);
        editText = findViewById(R.id.editUsbAlias);
        editText.setText(item.getAlias());

        btnOk.setOnClickListener(this);

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> dismiss());
    }

    @Override
    public void onClick(View v) {
        item.setAlias(editText.getText().toString());//here is your updated(or not updated) text
        usbListView.refreshData();
        UsbAliasService.updateWithNewAlias(dbHandler,item);

        dismiss();
    }
}