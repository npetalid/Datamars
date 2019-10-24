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
