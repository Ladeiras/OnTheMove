package com.moksie.onthemove.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Nesta classe são implementadas funções estáticas para fazer handling dos vários erros que podem
 * ocorrer durante o funcionamento da aplicação.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class ErrorManager
{
    //Variaveis com as mensagens usadas
    private final static String ExitDialogTitle = "Sair";
    private final static String ExitDialogMessage = "Deseja terminar a aplicação?";

    /**
     * Verifica se o dispodistivo tem ligação à internet
     *
     * @param activity Activity de chamada
     * @return True se ligado, False se desligado
     */
    public static boolean isOnline(Activity activity)
    {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }

    /**
     * Caixa de dialogo que é apresentada quando o utilizador carrega no botão Back da MainActivity
     *
     * @param activity Activity de chamada
     */
    public static void Exit(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(ExitDialogTitle);
        builder.setMessage(ExitDialogMessage);
        builder.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        TextView msg = new TextView(activity);
        msg.setGravity(Gravity.CENTER_HORIZONTAL);

        builder.setView(msg)
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                    }
                }).setNegativeButton("Não",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
            }
        })
        ;
        AlertDialog alert = builder.create();
        alert.show();
    }
}
