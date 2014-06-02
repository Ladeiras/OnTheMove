package com.moksie.onthemove.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.utilities.FileIO;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainMenuActivity extends FragmentActivity {

    private Airport airport;
    private Flight flightASeguir;
    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("airport");

        //Verificar se o voo a seguir pertence ao aeroporto escolhido
        parseVooFile();

        //Botao Voos
        final Button voosButton = (Button) findViewById(R.id.voos_button);
        voosButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, FlightsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("airport", airport);
                MainMenuActivity.this.startActivity(intent);
            }
        });

        //Botao Calculo Rota
        final Button cdRButton = (Button) findViewById(R.id.moveme_button);
        cdRButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, RoutesMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("airport", airport);
                MainMenuActivity.this.startActivity(intent);
            }
        });

        //Botao Servicos
        final Button serviceButton = (Button) findViewById(R.id.services_button);
        serviceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ServicesMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("airport", airport);
                MainMenuActivity.this.startActivity(intent);
            }
        });

        //Botao Contactos
        final Button contactsButton = (Button) findViewById(R.id.contacts_button);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ContactsMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("airport", airport);
                MainMenuActivity.this.startActivity(intent);
            }
        });

        //Botao Sobre
        final Button aboutButton = (Button) findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, AboutActivity.class);
                MainMenuActivity.this.startActivity(intent);
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

        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempFlight = FileIO.deserializeVooObject(MainActivity.FILE_FLIGHT, this).toParcelable();

            FooterFragment.setVisibility(true);
            FooterFragment.setFlight(tempFlight);
            FooterFragment.updateFlight(this);
            FooterFragment.setVisibility(true);

            layout.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,10f)
            );
        }
        else
        {
            FooterFragment.setVisibility(false);
            layout.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,11f)
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
        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight flight = FileIO.deserializeVooObject(MainActivity.FILE_FLIGHT, this).toParcelable();

            if((flight.getPartidaaeroportoid() != airport.getId() && flight.isPartida())||
                (flight.getChegadaaeroportoid() != airport.getId() && flight.isChegada()))
            {
                FileIO.removeFile(MainActivity.FILE_FLIGHT, this);
                NotificationManager nm =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancel(01);
            }

            return true;
        }

        return false;
    }
}
