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

/**
 * Nesta classe são mostrados os serviços correspondentes ao menu 'Serviços' (botões).
 * Mapa Aeroporto - Inicia PlantListActivity com a opção OPTION_ALL que significa a visualização da
 *                  planta sem pontos de interesse.
 * Lojas - Inicia StoreListActivity com a opção OPTION_ALL que significa que a opção de pesquisa
 *          inicial das lojas é todas as lojas.
 * Restaurantes - Inicia PlantListActivity onde é possivel ver a lista de plantas e respetivos
 *                  restaurantes.
 * Outros Serviços - Inicia MenuOtherServicesActivity onde é possivel escolher outros serviços
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class MenuServicesActivity extends FragmentActivity {

    //Opções dos parametros a passar para as restantes Activities
    private static final String OPTION_ALL = "all";
    private static final String OPTION_LOCATIONS = "locations";

    private Airport airport;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_menu);

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("airport");

        //Botao Mapa Aeroporto
        final Button mapButton = (Button) findViewById(R.id.services_mapa_aeroporto);
        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuServicesActivity.this, PlantListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("option", OPTION_ALL);
                intent.putExtra("service", "mapa");
                intent.putExtra("airport", airport);
                MenuServicesActivity.this.startActivity(intent);
            }
        });

        //Botao Lojas
        final Button storesButton = (Button) findViewById(R.id.services_botao_lojas);
        storesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuServicesActivity.this, StoreListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("airport", airport.getCode());
                intent.putExtra("option", OPTION_ALL);
                MenuServicesActivity.this.startActivity(intent);
            }
        });

        //Botao Restaurantes
        final Button restaurantButton = (Button) findViewById(R.id.services_botao_restaurantes);
        restaurantButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuServicesActivity.this, PlantListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("option", OPTION_LOCATIONS);
                intent.putExtra("service", "Restaurant");
                intent.putExtra("airport", airport);
                MenuServicesActivity.this.startActivity(intent);
            }
        });

        //Botao Outros Serviços
        final Button othServButton = (Button) findViewById(R.id.services_botao_outrosservicos);
        othServButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuServicesActivity.this, MenuOtherServicesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("airport", airport);
                MenuServicesActivity.this.startActivity(intent);
            }
        });

        //Fragments
        updateFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    /**
     * Função de atualização de todos os Fragments desta vista
     */
    public void updateFragments()
    {
        updateFooter();
    }

    /**
     * Função de atualização do Fragment Footer que corresponde ao voo que está a ser seguido.
     * Nesta função também são atualizados os tamanhos dos restantes elementos da vista caso o
     * fragment exista ou não.
     */
    public void updateFooter()
    {
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.services_footer);

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
