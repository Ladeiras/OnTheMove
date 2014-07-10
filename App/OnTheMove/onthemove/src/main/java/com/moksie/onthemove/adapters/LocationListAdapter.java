package com.moksie.onthemove.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.objects.Location;
import com.moksie.onthemove.objects.Plant;

import java.util.ArrayList;

/**
 * Nesta classe é feita a população de uma vista (elemento de uma lista de locations) a partir de um
 * Array de locations.
 * Cada elemento é composto pelo nome.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class LocationListAdapter extends ArrayAdapter<Location> {
    private Activity context;
    ArrayList<Location> data = null;

    public LocationListAdapter(Activity context, int resource, ArrayList<Location> data) {
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

        Location item = data.get(position);

        if (item != null) {
            TextView LocationItem = (TextView) row.findViewById(R.id.location_textView);

            if (LocationItem != null) {
                LocationItem.setText(item.getName());
            }
        }
        else Log.w("PlantsList", "Null Item");

        return row;
    }
}
