package com.moksie.onthemove.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.activities.FooterActivity;
import com.moksie.onthemove.activities.MainActivity;
import com.moksie.onthemove.activities.VooInfoActivity;
import com.moksie.onthemove.activities.VoosActivity;
import com.moksie.onthemove.objects.Voo;

import java.text.SimpleDateFormat;

public class FooterFragment extends Fragment
{
    public static boolean visibility = true;
    public static Voo voo;

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

    public static void setVoo(Voo v)
    {
        voo = v;
    }

    public static void updateVoo(Activity a)
    {
        TextView CodigoVoo = (TextView) a.findViewById(R.id.codigo_voo_textView);
        CodigoVoo.setText(String.valueOf(voo.getCodigovoo()));

        TextView OrigemDestino = (TextView) a.findViewById(R.id.origem_destino_textView);
        OrigemDestino.setText(voo.getPartidacidade()+" - "+voo.getChegadacidade());

        TextView TempoEstimado = (TextView) a.findViewById(R.id.tempo_estimado_textView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.applyPattern("dd/MM hh:mm");

        if(voo.isPartida())
            TempoEstimado.setText(sdf.format(voo.getPartidatempoestimado()));
        else
            TempoEstimado.setText(sdf.format(voo.getChegadatempoestimado()));
    }
}
