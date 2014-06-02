package com.moksie.onthemove.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moksie.onthemove.R;

import java.util.ArrayList;

public class ContactsAdapter extends ArrayAdapter<Long> {
    private Activity context;
    ArrayList<Long> data = null;

    public ContactsAdapter(Activity context, int resource, ArrayList<Long> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.contact_item, parent, false);
        }

        Long item = data.get(position);

        if (item != null) { // Parse the data from each object and set it.
            TextView Phone = (TextView) row.findViewById(R.id.phone_textView);

            String phone = String.valueOf(item);

            if (Phone != null) {
                Phone.setText("+ "+phone.substring(0,3)+" "+phone.substring(3,6)+" "+phone.substring(6,9)+" "+phone.substring(9));
            }
        }
        else Log.w("FLAG", "Item Nulo!");

        return row;
    }
}
