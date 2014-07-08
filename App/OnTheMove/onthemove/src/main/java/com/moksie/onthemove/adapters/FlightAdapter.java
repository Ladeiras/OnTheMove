package com.moksie.onthemove.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.objects.Flight;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class FlightAdapter extends ArrayAdapter<Flight> {
    private Activity context;
    ArrayList<Flight> data = null;

    public FlightAdapter(Activity context, int resource, ArrayList<Flight> data) {
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
            row = inflater.inflate(R.layout.flight_item, parent, false);
        }

        Flight item = data.get(position);

        if (item != null) { // Parse the data from each object and set it.
            TextView CodigoVoo = (TextView) row.findViewById(R.id.codigoVoo);
            TextView OrigemDestino = (TextView) row.findViewById(R.id.origemDestino);
            //TextView Companhia = (TextView) row.findViewById(R.id.companhia);
            TextView TempoEstimado = (TextView) row.findViewById(R.id.tempoEstimado);

            if (CodigoVoo != null) {
                CodigoVoo.setText(String.valueOf(item.getCode()));
            }

            if (OrigemDestino != null) {
                OrigemDestino.setText(item.getDepartureairportcity()+" - "+item.getArrivalairportcity());
            }

          /*if (Companhia != null) {
                Companhia.setText(item.getAirlinecode());
            }*/

            if (TempoEstimado != null) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                sdf.applyPattern("dd/MM HH:mm");
                sdf.setTimeZone(TimeZone.getDefault());

                if(item.isDeparture())
                    TempoEstimado.setText(sdf.format(item.getDepartplannedtimeDate()));
                else
                    TempoEstimado.setText(sdf.format(item.getArrivalplannedtimeDate()));
            }
        }
        else Log.w("FLAG", "Item Nulo!");

        return row;
    }
}
