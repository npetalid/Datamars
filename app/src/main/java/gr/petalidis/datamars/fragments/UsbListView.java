
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

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import gr.petalidis.datamars.inspections.domain.UsbAlias;

public class UsbListView extends ListView {
    public UsbListView(Context context, List<UsbAlias> aliases) {
        super(context);
         final ArrayAdapter<UsbAlias> usbAliasArrayAdapter = new ArrayAdapter<UsbAlias>(context,
                android.R.layout.simple_list_item_1,android.R.id.text1,aliases);
        this.setAdapter(usbAliasArrayAdapter);
    }

    public void refreshData()
    {
        if (this.getAdapter()!=null) {
            ArrayAdapter<UsbAlias> usbAliasArrayAdapter = (ArrayAdapter<UsbAlias>) getAdapter();
            usbAliasArrayAdapter.notifyDataSetChanged();
        }
    }
}
