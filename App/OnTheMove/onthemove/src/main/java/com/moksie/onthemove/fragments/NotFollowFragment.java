package com.moksie.onthemove.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moksie.onthemove.R;
import com.moksie.onthemove.activities.FlightInfoActivity;

/**
 * Este Fragmente corresponde ao botão usado para deixar de seguir um voo na vista da
 * FlightInfoActivity
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class NotFollowFragment extends Fragment
{
    public static boolean visibility = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_not_follow, container, false);

        //Listner do botão. Quando carregado cancela qualque notificação presente no momento
        Button mButton = (Button) rootView.findViewById(R.id.nao_seguir_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((FlightInfoActivity)getActivity()).onClickNaoSeguir();
                CancelNotification(((FlightInfoActivity)getActivity()),01);
            }
        });

        updateVisibility();

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

    /**
     * Função que cancela qualquer notificação
     *
     * @param ctx Contexto da Activity de chamada
     * @param notifyId Id da notificação
     */
    public static void CancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }
}
