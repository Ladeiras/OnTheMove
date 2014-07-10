package com.moksie.onthemove.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.moksie.onthemove.R;

/**
 * Este Fragment corresponde à área presente na parte superior das vistas da aplicação.
 * Nesta vista são mostrados uma imagem para o icone, uma imagem de fundo e o botao de ajuda, que
 * varia dependendo da vista atual.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class HeaderFragment extends Fragment
{
    public static String msg = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_header, container, false);

        final ImageView helpButton = (ImageButton) rootView.findViewById(R.id.help_button);
        helpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Ajuda");
                alertDialogBuilder.setMessage(msg)
                        .setCancelable(false);
                alertDialogBuilder.setNegativeButton("Fechar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }
                );
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        });

        return rootView;
    }

    public static String getMsg() {
        return msg;
    }

    public static void setMsg(String msg) {
        HeaderFragment.msg = msg;
    }
}
