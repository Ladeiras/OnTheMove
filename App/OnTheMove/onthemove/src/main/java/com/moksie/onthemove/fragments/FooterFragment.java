package com.moksie.onthemove.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.activities.FooterActivity;
import com.moksie.onthemove.objects.Flight;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Este Fragment corresponde à área presente na parte inferior das vistas da aplicação.
 * Nesta vista é mostrada resumidamente a informação do voo a seguir (codigo do voo, origem-destino
 * e data estimada de partida ou chegada dependendo se se trata de uma partida ou de uma chegada)
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class FooterFragment extends Fragment
{
    public static boolean visibility = true;
    public static Flight flight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_footer, container, false);
        updateVisibility();

        LinearLayout footer = (LinearLayout) rootView.findViewById(R.id.footer_button);
        footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FooterActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    /**
     * Função que altera a visibilidade do botão dependendo da variavel visibility
     */
    public void updateVisibility()
    {
        if(!visibility)
        {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(this);
            //Bug conhecido, é necessario permitir perda do estado
            ft.commitAllowingStateLoss();
        }
        else
        {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.show(this);
            //Bug conhecido, é necessario permitir perda do estado
            ft.commitAllowingStateLoss();
        }
    }

    public static void setVisibility(boolean v)
    {
        visibility = v;
    }

    public static void setFlight(Flight v)
    {
        flight = v;
    }

    /**
     * Nesta função é atualizada a informação visivel na vista a partir do objecto flight
     *
     * @param a Activity de chamada
     */
    public static void updateFlight(Activity a)
    {
        //Codigo do voo
        TextView CodigoVoo = (TextView) a.findViewById(R.id.codigo_voo_textView);
        CodigoVoo.setText(String.valueOf(flight.getCode()));

        //Origem-Destino
        TextView OrigemDestino = (TextView) a.findViewById(R.id.origem_destino_textView);
        OrigemDestino.setText(flight.getDepartureairportcity()+" - "+ flight.getArrivalairportcity());

        //Tempo estimado (dd/MM HH:mm)
        TextView TempoEstimado = (TextView) a.findViewById(R.id.tempo_estimado_textView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.applyPattern("dd/MM HH:mm");
        sdf.setTimeZone(TimeZone.getDefault());

        if(flight.isDeparture())
            TempoEstimado.setText(sdf.format(flight.getDepartrealtimeDate()));
        else
            TempoEstimado.setText(sdf.format(flight.getArrivalrealtimeDate()));
    }
}
