package com.moksie.onthemove.activities;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FollowFragment;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.fragments.NotFollowFragment;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.utilities.FileIO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FlightInfoActivity extends FragmentActivity
{
    public static int SEGUIR_ATUAL = 0;
    public static int SEGUIR_OUTRO = 1;
    public static int NAO_SEGUIR = 2;

    private int estado = NAO_SEGUIR;
    private Flight flight;

    //private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_flight_info);

        Bundle data = getIntent().getExtras();
        flight = (Flight) data.getParcelable("flight");

        //notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        parseVooFile();
        updateFragments(estado);

        TextView CodigoVoo = (TextView) this.findViewById(R.id.top_codigo_voo_textView);
        CodigoVoo.setText(String.valueOf(flight.getCodigovoo()));

        TextView OrigemDestino = (TextView) this.findViewById(R.id.top_origem_destino_textView);
        OrigemDestino.setText(flight.getPartidacidade()+" - "+ flight.getChegadacidade());

        TextView Companhia = (TextView) this.findViewById(R.id.top_companhia_textView);
        Companhia.setText(flight.getCodigocompanhia());

        TextView TempoEstimado = (TextView) this.findViewById(R.id.top_tempo_estimado_textView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.applyPattern("HH:mm");

        LinearLayout llp = (LinearLayout) findViewById(R.id.partida_info_LinearLayout);
        LinearLayout llc = (LinearLayout) findViewById(R.id.chegada_info_LinearLayout);

        if(flight.isPartida()) {
            TempoEstimado.setText(sdf.format(flight.getPartidatempoestimado()));

            TextView terminal = (TextView) findViewById(R.id.terminal);
            terminal.setText("Terminal - "+String.valueOf(flight.getTerminal()));

            TextView checkin = (TextView) findViewById(R.id.checkin);
            checkin.setText("Check-In - "+sdf.format(flight.getCheckininicio())+" às "+sdf.format(flight.getCheckinfim()));

            TextView portaEmbarque = (TextView) findViewById(R.id.porta_embarque);
            portaEmbarque.setText("Embarque - "+String.valueOf(flight.getPortaembarque()));

            TextView embarque = (TextView) findViewById(R.id.embarque);
            embarque.setText("Hora Prevista: "+sdf.format(flight.getEmbarque()));

            llp.setVisibility(ViewGroup.VISIBLE);
            llc.setVisibility(ViewGroup.GONE);
        }
        else {
            TempoEstimado.setText(sdf.format(flight.getChegadatempoestimado()));

            TextView tapeteBagagem = (TextView) findViewById(R.id.tapete_bagagem);
            tapeteBagagem.setText("Bagagem - Tapete "+String.valueOf(flight.getTapetebagagem()));

            TextView bagagem = (TextView) findViewById(R.id.bagagem_textView);
            bagagem.setText("Hora Prevista: "+sdf.format(flight.getBagagem()));

            TextView portaDesembarque = (TextView) findViewById(R.id.porta_desembarque);
            portaDesembarque.setText("Desembarque - "+String.valueOf(flight.getPortadesembarque()));

            TextView desembarque = (TextView) findViewById(R.id.desembarque);
            desembarque.setText("Hora Prevista: "+sdf.format(flight.getDesembarque()));

            llp.setVisibility(ViewGroup.GONE);
            llc.setVisibility(ViewGroup.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Botao ajuda
        HeaderFragment.setMsg("Neste ecrã pode consultar as informações do voo. Pode também seguir o voo, clicando no botão 'Seguir Voo'.");

        updateFragments(estado);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFragments(estado);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //updateFragments(seguir);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public void parseVooFile()
    {
        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempvoo = FileIO.deserializeVooObject(MainActivity.FILE_FLIGHT, this).toParcelable();

            if(tempvoo.getId() == flight.getId())
            {
                estado = SEGUIR_ATUAL;
                flight = tempvoo;
            }
            else
            {
                estado = SEGUIR_OUTRO;
            }
        }
        else
        {
            estado = NAO_SEGUIR;
        }
    }

    public void updateFragments(int estado)
    {
        if(estado == SEGUIR_ATUAL || estado == SEGUIR_OUTRO)
            FooterFragment.setVisibility(true);
        else
            FooterFragment.setVisibility(false);

        updateFooter();
        updateSeguir(estado);
    }

    public void updateFooter()
    {
        ScrollView layout = (ScrollView) findViewById(R.id.voo_info_resizable);
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.voo_info_footer);

        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempFlight = FileIO.deserializeVooObject(MainActivity.FILE_FLIGHT, this).toParcelable();

            FooterFragment.setVisibility(true);
            FooterFragment.setFlight(tempFlight);
            FooterFragment.updateFlight(this);
            FooterFragment.setVisibility(true);

            layout.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,8f)
            );
        }
        else
        {
            FooterFragment.setVisibility(false);
            layout.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,9f)
            );
        }

        footer.updateVisibility();
    }

    public void updateSeguir(int estado)
    {
        if(estado == SEGUIR_ATUAL)
        {
            Fragment naoseguir = new NotFollowFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.voo_info_seguir, naoseguir);
            fragmentTransaction.commit();
        }
        else
        {
            Fragment seguir = new FollowFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.voo_info_seguir, seguir);
            fragmentTransaction.commit();
        }
    }

    public void onClickSeguir()
    {
        if(estado == NAO_SEGUIR || estado == SEGUIR_OUTRO)
        {
            estado = SEGUIR_ATUAL;
            updateSeguir(estado);
            FileIO.removeFile(MainActivity.FILE_FLIGHT, this);
            FileIO.serializeObject(MainActivity.FILE_FLIGHT, flight.toSerializable(), this);
            Log.w("FLAG", "Escreveu? vooID: "+ flight.getId());

            //Footer
            updateFooter();
        }

        //createNotification();
    }

    public void onClickNaoSeguir()
    {
        if(estado == SEGUIR_ATUAL)
        {
            estado = NAO_SEGUIR;
            updateSeguir(estado);
            FileIO.removeFile(MainActivity.FILE_FLIGHT, this);
            Log.w("FLAG", "Removeu? vooID: "+ flight.getId());

            //Footer
            updateFooter();
        }

        //notificationManager.cancel(01);
    }

    /*private void createNotification()
    {
        Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_icon_sobre),
                getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width),
                getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_height),
                true);

        Intent intent = new Intent(this, FlightInfoActivity.class);
        intent.putExtra("voo", flight);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 01, intent, Intent.FLAG_ACTIVITY_CLEAR_TASK);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);
        builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle(flight.getPartidacidade() +" - "+ flight.getChegadacidade());
        builder.setContentText("O Voo parte às "+ flight.getPartidatempoestimado().getHours());

        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        long diff = getDateDiff(flight.getPartidatempoestimado(),currentDate,TimeUnit.MILLISECONDS);
        if(diff < 3600000)
            builder.setSubText("Tem "+TimeUnit.MILLISECONDS.convert(diff,TimeUnit.MILLISECONDS)+" minutos para o check-in");
        //builder.setNumber(101);
        builder.setTicker("Está agora a seguir um voo");
        builder.setSmallIcon(R.drawable.ic_icon_sobre);
        builder.setLargeIcon(bm);
        builder.setAutoCancel(true);
        builder.setPriority(0);
        builder.setOngoing(true);

        final Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;

        notificationManager.notify(01,notification);
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit)
    {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }*/
}
