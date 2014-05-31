package com.moksie.onthemove.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.objects.Aeroporto;
import com.moksie.onthemove.objects.Voo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VooAdapter extends ArrayAdapter<Voo> {
    private Activity context;
    ArrayList<Voo> data = null;

    public VooAdapter(Activity context, int resource, ArrayList<Voo> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        Log.w("FLAG", "Criado VooAdapter!");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.voo_item, parent, false);
        }

        Voo item = data.get(position);

        if (item != null) { // Parse the data from each object and set it.
            TextView CodigoVoo = (TextView) row.findViewById(R.id.codigoVoo);
            TextView OrigemDestino = (TextView) row.findViewById(R.id.origemDestino);
            TextView Companhia = (TextView) row.findViewById(R.id.companhia);
            TextView TempoEstimado = (TextView) row.findViewById(R.id.tempoEstimado);

            if (CodigoVoo != null) {
                CodigoVoo.setText(String.valueOf(item.getCodigovoo()));
            }

            if (OrigemDestino != null) {
                OrigemDestino.setText(item.getPartidacidade()+" - "+item.getChegadacidade());
            }

            if (Companhia != null) {
                Companhia.setText(item.getCodigocompanhia());
            }

            if (TempoEstimado != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                sdf.applyPattern("dd/MM HH:mm");
                TempoEstimado.setText(sdf.format(item.getPartidatempoestimado()));
            }
        }
        else Log.w("FLAG", "Item Nulo!");

        return row;
    }
}
