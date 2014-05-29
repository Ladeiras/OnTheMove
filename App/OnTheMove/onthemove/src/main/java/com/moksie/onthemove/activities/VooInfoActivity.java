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
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.NaoSeguirFragment;
import com.moksie.onthemove.fragments.SeguirFragment;
import com.moksie.onthemove.objects.Aeroporto;
import com.moksie.onthemove.objects.Voo;
import com.moksie.onthemove.utilities.FileIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class VooInfoActivity extends FragmentActivity
{
    public static int SEGUIR_ATUAL = 0;
    public static int SEGUIR_OUTRO = 1;
    public static int NAO_SEGUIR = 2;

    private int estado = NAO_SEGUIR;
    private Voo voo;

    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_voo_info);

        Bundle data = getIntent().getExtras();
        voo = (Voo) data.getParcelable("voo");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        parseVooFile();
        updateFragments(estado);

        TextView CodigoVoo = (TextView) this.findViewById(R.id.top_codigo_voo_textView);
        CodigoVoo.setText(String.valueOf(voo.getCodigovoo()));

        TextView OrigemDestino = (TextView) this.findViewById(R.id.top_origem_destino_textView);
        OrigemDestino.setText(voo.getPartidacidade()+" - "+voo.getChegadacidade());

        TextView Companhia = (TextView) this.findViewById(R.id.top_companhia_textView);
        Companhia.setText(voo.getCodigocompanhia());

        TextView TempoEstimado = (TextView) this.findViewById(R.id.top_tempo_estimado_textView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.applyPattern("HH:mm");

        if(voo.isPartida())
            TempoEstimado.setText(sdf.format(voo.getPartidatempoestimado()));
        else
            TempoEstimado.setText(sdf.format(voo.getChegadatempoestimado()));
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        if(FileIO.fileExists(MainActivity.FILE_VOO, this))
        {
            //Ler voo do ficheiro
            //String temp = FileIO.readFromFile(MainActivity.FILE_VOO, this);
            //Voo tempvoo = new Voo(temp);
            Voo tempvoo = FileIO.deserializeVooObject(MainActivity.FILE_VOO, this).toParcelable();

            if(tempvoo.getId() == voo.getId())
            {
                estado = SEGUIR_ATUAL;
                voo = tempvoo;
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

        if(FileIO.fileExists(MainActivity.FILE_VOO, this))
        {
            //Ler voo do ficheiro
            //String temp = FileIO.readFromFile(MainActivity.FILE_VOO, this);
            //Voo tempVoo = new Voo(temp);
            Voo tempVoo = FileIO.deserializeVooObject(MainActivity.FILE_VOO, this).toParcelable();

            FooterFragment.setVisibility(true);
            FooterFragment.setVoo(tempVoo);
            FooterFragment.updateVoo(this);
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
            Fragment naoseguir = new NaoSeguirFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.voo_info_seguir, naoseguir);
            fragmentTransaction.commit();
        }
        else
        {
            Fragment seguir = new SeguirFragment();
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
            FileIO.removeFile(MainActivity.FILE_VOO, this);
            //FileIO.writeToFile(MainActivity.FILE_VOO, voo.toString(), this);
            FileIO.serializeObject(MainActivity.FILE_VOO, voo.toSerializable(), this);
            Log.w("FLAG", "Escreveu? vooID: "+voo.getId());

            //Footer
            updateFooter();
        }

        createNotification();
    }

    public void onClickNaoSeguir()
    {
        if(estado == SEGUIR_ATUAL)
        {
            estado = NAO_SEGUIR;
            updateSeguir(estado);
            FileIO.removeFile(MainActivity.FILE_VOO, this);
            Log.w("FLAG", "Removeu? vooID: "+voo.getId());

            //Footer
            updateFooter();
        }

        notificationManager.cancel(01);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void createNotification()
    {
        Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_icon_sobre),
                getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width),
                getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_height),
                true);

        Intent intent = new Intent(this, VooInfoActivity.class);
        intent.putExtra("voo", voo);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 01, intent, Intent.FLAG_ACTIVITY_CLEAR_TASK);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);
        builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle(voo.getPartidacidade() +" - "+ voo.getChegadacidade());
        builder.setContentText("O Voo parte às "+ voo.getPartidatempoestimado().getHours());

        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        long diff = getDateDiff(voo.getPartidatempoestimado(),currentDate,TimeUnit.MILLISECONDS);
        if(diff < 3600000)
            builder.setSubText("Tem "+TimeUnit.MINUTES.convert(diff,TimeUnit.MILLISECONDS)+" minutos para o check-in");
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
    }
}
