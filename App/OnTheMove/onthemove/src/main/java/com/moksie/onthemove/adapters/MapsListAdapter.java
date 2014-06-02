package com.moksie.onthemove.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.objects.Map;

import java.util.ArrayList;

public class MapsListAdapter extends ArrayAdapter<Map> {
    private Activity context;
    ArrayList<Map> data = null;

    public MapsListAdapter(Activity context, int resource, ArrayList<Map> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.service_map_item, parent, false);
        }

        Map item = data.get(position);

        if (item != null) { // Parse the data from each object and set it.
            TextView MapItem = (TextView) row.findViewById(R.id.location_textView);

            if (MapItem != null) {
                MapItem.setText(item.getLocation());
            }
        }
        else Log.w("MapList", "Null Item");

        return row;
    }
}
