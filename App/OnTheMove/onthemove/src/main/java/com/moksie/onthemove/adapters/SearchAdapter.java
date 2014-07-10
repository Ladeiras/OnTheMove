package com.moksie.onthemove.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.activities.FlightListActivity;

import java.util.ArrayList;

/**
 * Nesta classe é feita a população de uma vista (elemento de um Spinner com opções de pesquisa) a
 * partir de um Array de strings com o nome das opções de pesquisa.
 * Cada elemento é composto pelo nome da opção.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

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

        if (item != null) {
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

        String item = getItem(position);

        /*
         * A opção por defeito é vazia para evitar aparecer no spinner, onde deverá aparecer
         * ('Pesquisar por...')
         */
        if( (item != null) && ( position == FlightListActivity.OPTION_DEFAULT)) {
            row.setVisibility(View.GONE);
        } else {
            row.setVisibility(View.VISIBLE);
        }

        if (item != null) {
            TextView Option = (TextView) row.findViewById(R.id.search_option);

            Option.setTextSize(15);

            if (Option != null) {
                Option.setText(item);
            }
        }

        return row;
    }

    //Numero de elementos do spinner
    @Override
    public int getCount() {
        return 4;
    }
}
