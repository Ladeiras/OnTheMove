package com.moksie.onthemove.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.moksie.onthemove.R;

/**
 * Nesta classe, caso o GPS do dispositivo esteja desativado, é mostrada uma caixa de dialogo para
 * que o utilizador seja encaminhado para as definições de geolocalização ou simplesmente ignorar.
 * Caso opte por ativar as definições GPS, a lista de aeroportos inicial será organizada por ordem
 * de proximidade.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class AlertActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        showGPSDisabledAlertToUser();
    }

    /**
     * Nesta função é mostrada a caixa de dialogo com as opções ir para as definições GPS ou
     * cancelar.
     * Após a escolha a Activity é terminada.
     */
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("O GPS está desligado no seu dispositivo. Gostaria de o ligar?")
                .setCancelable(false)
                .setPositiveButton("Ir para definições para ativar GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        kill_activity();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        kill_activity();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    void kill_activity()
    {
        finish();
    }
}
