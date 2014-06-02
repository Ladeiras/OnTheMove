package com.moksie.onthemove.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.objects.Airport;

import java.util.ArrayList;

public class AirportAdapter extends ArrayAdapter<Airport> {
    private Activity context;
    ArrayList<Airport> data = null;

    public AirportAdapter(Activity context, int resource,
                          ArrayList<Airport> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.airport_spinner_item, parent, false);
        }

        Airport item = data.get(position);

        if (item != null) { // Parse the data from each object and set it.
            TextView AeroportoPais = (TextView) row.findViewById(R.id.item_pais);
            TextView AeroportoCidade = (TextView) row.findViewById(R.id.item_cidade);
            TextView AeroportoNome = (TextView) row.findViewById(R.id.item_nome);

            AeroportoPais.setTextSize(18);
            AeroportoCidade.setTextSize(18);
            AeroportoNome.setTextSize(14);

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
            row = inflater.inflate(R.layout.airport_spinner_item, parent, false);
        }

        Airport item = data.get(position);

        if (item != null) { // Parse the data from each object and set it.
            TextView AeroportoPais = (TextView) row.findViewById(R.id.item_pais);
            TextView AeroportoCidade = (TextView) row.findViewById(R.id.item_cidade);
            TextView AeroportoNome = (TextView) row.findViewById(R.id.item_nome);

            AeroportoPais.setTextSize(18);
            AeroportoCidade.setTextSize(18);
            AeroportoNome.setTextSize(14);

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
