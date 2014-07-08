package com.moksie.onthemove.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.TextView;

import com.moksie.onthemove.R;

/**
 * Created by belh0 on 08/06/2014.
 */
public class ErrorManager
{
    private final static String ExitMessage = "Sair?";

    public static boolean isOnline(Activity activity)
    {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void Exit(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(ExitMessage);
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
