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

/**
 * Nesta classe é feita a população de uma vista (elemento de um Spinner de aeroportos) a partir de
 * um Array de aeroportos.
 * Cada elemento é composto pelo país, cidade e nome do aeroporto.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class AirportAdapter extends ArrayAdapter<Airport> {
    private Activity context;
    ArrayList<Airport> data = null;

    public AirportAdapter(Activity context, int resource,
                          ArrayList<Airport> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    //Elemento da lista
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.airport_spinner_item, parent, false);
        }

        Airport item = data.get(position);

        if (item != null) {
            TextView AeroportoPais = (TextView) row.findViewById(R.id.item_pais);
            TextView AeroportoCidade = (TextView) row.findViewById(R.id.item_cidade);
            TextView AeroportoNome = (TextView) row.findViewById(R.id.item_nome);

            AeroportoPais.setTextSize(18);
            AeroportoCidade.setTextSize(18);
            AeroportoNome.setTextSize(14);

            if (AeroportoPais != null) {
                AeroportoPais.setText(item.getCountry());
            }
            if (AeroportoCidade != null) {
                AeroportoCidade.setText(item.getCity());
            }
            if (AeroportoNome != null) {
                AeroportoNome.setText(item.getName());
            }
        }

        return row;
    }

    //Elemento por defeito do Spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.airport_spinner_item, parent, false);
        }

        Airport item = data.get(position);

        if (item != null) {
            TextView AeroportoPais = (TextView) row.findViewById(R.id.item_pais);
            TextView AeroportoCidade = (TextView) row.findViewById(R.id.item_cidade);
            TextView AeroportoNome = (TextView) row.findViewById(R.id.item_nome);

            AeroportoPais.setTextSize(18);
            AeroportoCidade.setTextSize(18);
            AeroportoNome.setTextSize(14);

            if (AeroportoPais != null) {
                AeroportoPais.setText(item.getCountry());
            }
            if (AeroportoCidade != null) {
                AeroportoCidade.setText(item.getCity());
            }
            if (AeroportoNome != null) {
                AeroportoNome.setText(item.getName());
            }
        }

        return row;
    }
}
