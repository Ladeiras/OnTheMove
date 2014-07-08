package com.moksie.onthemove.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.objects.Plant;

import java.util.ArrayList;

public class PlantListAdapter extends ArrayAdapter<Plant> {
    private Activity context;
    ArrayList<Plant> data = null;

    public PlantListAdapter(Activity context, int resource, ArrayList<Plant> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.plant_item, parent, false);
        }

        Plant item = data.get(position);

        if (item != null) { // Parse the data from each object and set it.
            TextView MapItem = (TextView) row.findViewById(R.id.location_textView);

            if (MapItem != null) {
                MapItem.setText(item.getName());
            }
        }
        else Log.w("PlantsList", "Null Item");

        return row;
    }
}
