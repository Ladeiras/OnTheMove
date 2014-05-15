package com.moksie.onthemove.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moksie.onthemove.R;
import com.moksie.onthemove.activities.VooInfoActivity;

public class NaoSeguirFragment extends Fragment
{
    public static boolean visibility = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_nao_seguir, container, false);

        Button mButton = (Button) rootView.findViewById(R.id.nao_seguir_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((VooInfoActivity)getActivity()).onClickNaoSeguir();
            }
        });

        updateVisibility();

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
}
