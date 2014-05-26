package com.moksie.onthemove.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Aeroporto;
import com.moksie.onthemove.objects.Voo;
import com.moksie.onthemove.utilities.FileIO;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MenuActivity extends FragmentActivity {

    private Aeroporto aeroporto;
    private Voo vooASeguir;
    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        aeroporto = (Aeroporto) intent.getParcelableExtra("aeroporto");

        //Verificar se o voo a seguir pertence ao aeroporto escolhido
        parseVooFile();

        //Botao Voos
        final Button voosButton = (Button) findViewById(R.id.voos_button);
        voosButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, VoosActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("aeroporto", aeroporto);
                MenuActivity.this.startActivity(intent);
            }
        });

        //Botao Calculo Rota
        final Button cdRButton = (Button) findViewById(R.id.moveme_button);
        cdRButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MoveMeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("aeroporto", aeroporto);
                MenuActivity.this.startActivity(intent);
            }
        });

        //Botao Servicos
        final Button serviceButton = (Button) findViewById(R.id.services_button);
        serviceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ServicesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("aeroporto", aeroporto);
                MenuActivity.this.startActivity(intent);
            }
        });

        //Botao Contactos
        final Button contactsButton = (Button) findViewById(R.id.contacts_button);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ContactsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("aeroporto", aeroporto);
                MenuActivity.this.startActivity(intent);
            }
        });
		
		//Fragments
        updateFragments();

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        //Toast.makeText(ctx,"TOSTA",3000).show();
                    }
                }, 5, 5, TimeUnit.SECONDS);
				

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFragments();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateFragments();
    }

    public void updateFragments()
    {
        updateFooter();
    }

    public void updateFooter()
    {
        LinearLayout layout = (LinearLayout) findViewById(R.id.menu_resizable);
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.menu_footer);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public boolean parseVooFile()
    {
        if(FileIO.fileExists(MainActivity.FILE_VOO, this))
        {
            //Ler voo do ficheiro
            //String temp = FileIO.readFromFile(MainActivity.FILE_VOO, this);
            //Voo voo = new Voo(temp);

            Voo voo = FileIO.deserializeVooObject(MainActivity.FILE_VOO, this).toParcelable();

            if((voo.getPartidaaeroportoid() != aeroporto.getId() && voo.isPartida())||
                (voo.getChegadaaeroportoid() != aeroporto.getId() && voo.isChegada()))
            {
                FileIO.removeFile(MainActivity.FILE_VOO, this);
                NotificationManager nm =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancel(01);
            }

            return true;
        }

        return false;
    }
}
