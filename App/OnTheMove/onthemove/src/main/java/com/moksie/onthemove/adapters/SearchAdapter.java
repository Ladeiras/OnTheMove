package com.moksie.onthemove.adapters;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.activities.VoosActivity;
import com.moksie.onthemove.listners.MyLocationListener;
import com.moksie.onthemove.objects.Aeroporto;
import com.moksie.onthemove.utilities.DistanceComparator;

import java.util.ArrayList;
import java.util.Collections;

public class SearchAdapter extends ArrayAdapter<String> {
    private Activity context;
    ArrayList<String> data = null;

    public SearchAdapter(Activity context, int resource,
                            ArrayList<String> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.search_spinner_item_default, parent, false);
        }

        String item = "Pesquisar por...";

        if (item != null) { // Parse the data from each object and set it.
            TextView Option = (TextView) row.findViewById(R.id.search_option);

            Option.setTextSize(15);

            if (Option != null) {
                Option.setText(item);
            }
        }

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.search_spinner_item, parent, false);
        }

        //String item = data.get(position);
        String item = getItem(position);

        if( (item != null) && ( position == VoosActivity.OPTION_DEFAULT)) {
            row.setVisibility(View.GONE);
        } else {
            row.setVisibility(View.VISIBLE);
        }

        Log.d("ITEMS2",item);

        if (item != null) { // Parse the data from each object and set it.
            TextView Option = (TextView) row.findViewById(R.id.search_option);

            Option.setTextSize(15);

            if (Option != null) {
                Option.setText(item);
            }
        }

        return row;
    }

    @Override
    public int getCount() {
        return 4;
    }



    /* @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.search_spinner_item, parent, false);
        }

        String item = data.get(position);

        if (item != null) { // Parse the data from each object and set it.
            TextView Option = (TextView) row.findViewById(R.id.search_option);

            Option.setTextSize(15);

            if (Option != null) {
                Option.setText(item);
            }
        }
        else
        {
            TextView tv = new TextView(getContext());
            tv.setVisibility(View.GONE);
            row = tv;
        }

        return row;
    }*/
}
