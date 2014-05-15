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
import com.moksie.onthemove.listners.MyLocationListener;
import com.moksie.onthemove.objects.Aeroporto;
import com.moksie.onthemove.utilities.DistanceComparator;

import java.util.ArrayList;
import java.util.Collections;

public class AeroportoAdapter extends ArrayAdapter<Aeroporto> {
    private Activity context;
    ArrayList<Aeroporto> data = null;

    public AeroportoAdapter(Activity context, int resource,
                          ArrayList<Aeroporto> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.aeroporto_spinner_item, parent, false);
        }

        Aeroporto item = data.get(position);

        if (item != null) { // Parse the data from each object and set it.
            TextView AeroportoPais = (TextView) row.findViewById(R.id.item_pais);
            TextView AeroportoCidade = (TextView) row.findViewById(R.id.item_cidade);
            TextView AeroportoNome = (TextView) row.findViewById(R.id.item_nome);

            if (AeroportoPais != null) {
                AeroportoPais.setText(item.getPais());
            }
            if (AeroportoCidade != null) {
                AeroportoCidade.setText(item.getCidade());
            }
            if (AeroportoNome != null) {
                AeroportoNome.setText(item.getNome());
            }
        }

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.aeroporto_spinner_item, parent, false);
        }

        Aeroporto item = data.get(position);

        if (item != null) { // Parse the data from each object and set it.
            TextView AeroportoPais = (TextView) row.findViewById(R.id.item_pais);
            TextView AeroportoCidade = (TextView) row.findViewById(R.id.item_cidade);
            TextView AeroportoNome = (TextView) row.findViewById(R.id.item_nome);

            if (AeroportoPais != null) {
                AeroportoPais.setText(item.getPais());
            }
            if (AeroportoCidade != null) {
                AeroportoCidade.setText(item.getCidade());
            }
            if (AeroportoNome != null) {
                AeroportoNome.setText(item.getNome());
            }
        }

        return row;
    }
}
