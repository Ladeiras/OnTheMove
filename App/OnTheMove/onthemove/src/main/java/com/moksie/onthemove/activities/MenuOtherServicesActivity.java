package com.moksie.onthemove.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.utilities.FileIO;

public class MenuOtherServicesActivity extends FragmentActivity {

    private Airport airport;
    private static final String OPTION_LOCATIONS = "locations";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_services_menu);

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("airport");

        //Botao WC
        final Button wcButton = (Button) findViewById(R.id.services_WC);
        wcButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuOtherServicesActivity.this, PlantListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("option", OPTION_LOCATIONS);
                intent.putExtra("service", "WC");
                intent.putExtra("airport", airport);
                MenuOtherServicesActivity.this.startActivity(intent);
            }
        });

        //Botao zona fumadores
        final Button smokersAreaButton = (Button) findViewById(R.id.services_zona_fumadores);
        smokersAreaButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuOtherServicesActivity.this, PlantListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("option", OPTION_LOCATIONS);
                intent.putExtra("service", "SmokingZone");
                intent.putExtra("airport", airport);
                MenuOtherServicesActivity.this.startActivity(intent);
            }
        });

        //Botao apoio cliente - Sem dados na BD
        final Button ACButton = (Button) findViewById(R.id.services_apoio_cliente);
        ACButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuOtherServicesActivity.this, PlantListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("option", OPTION_LOCATIONS);
                intent.putExtra("service", "Apoio ao Cliente");
                intent.putExtra("airport", airport);
                MenuOtherServicesActivity.this.startActivity(intent);
            }
        });

        //Botao parking - Sem dados na BD
        final Button parkingButton = (Button) findViewById(R.id.services_botao_parking);
        parkingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuOtherServicesActivity.this, PlantListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("option", OPTION_LOCATIONS);
                intent.putExtra("service", "Parking");
                intent.putExtra("airport", airport);
                MenuOtherServicesActivity.this.startActivity(intent);
            }
        });

        updateFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Botao Ajuda
        HeaderFragment.setMsg("Neste ecrã poderá escolher uma das opções listadas.");

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public void updateFragments()
    {
        updateFooter();
    }

    public void updateFooter()
    {
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.other_services_footer);

        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempFlight = FileIO.deserializeFlightObject(MainActivity.FILE_FLIGHT, this).toParcelable();

            FooterFragment.setVisibility(true);
            FooterFragment.setFlight(tempFlight);
            FooterFragment.updateFlight(this);
            FooterFragment.setVisibility(true);
        }
        else
        {
            FooterFragment.setVisibility(false);
        }

        footer.updateVisibility();
    }
}
