package com.moksie.onthemove.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
 * Nesta classe são mostrados dois botões, A partir do Aeroporto e Para o Aeroporto, em que ambos
 * fazem a ligação com a aplicação Move-me tendo como origem ou destino a localização do Aeroporto
 * (latitude e longitude).
 * Os dados do aeroporto são passados como parametro para esta Activity (objecto Airport).
 * Se a aplicação Move-me não estiver instalada no dispositivo é feito o reencaminhamento para a
 * PlayStore.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class MenuMoveMeActivity extends FragmentActivity {

    private Airport airport;//Aeroporto escolhido no inicio da aplicação

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moveme_menu);

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("airport");

        if(!isPackageInstalled("com.moveme", this))
        {
            //Se a aplicação Move-me não estiver instalada
            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
            marketIntent.setData(Uri.parse("market://details?id=com.moveme"));
            startActivity(intent);
            MenuMoveMeActivity.this.finish();
        }
        else
        {
            final Button aPartirButton = (Button) findViewById(R.id.apartir_button);
            aPartirButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent movemeIntent = new Intent("com.optproject.RouteFinderScreen");

                    movemeIntent.putExtra("type", 4);
                    movemeIntent.putExtra("NameFrom", "Aeroporto");
                    movemeIntent.putExtra("LatitudeTo", airport.getLat());
                    movemeIntent.putExtra("LongitudeTo", airport.getLon());

                    startActivity(movemeIntent);
                }
            });

            final Button paraButton = (Button) findViewById(R.id.para_button);
            paraButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent movemeIntent = new Intent("com.optproject.RouteFinderScreen");

                    movemeIntent.putExtra("type", 5);
                    movemeIntent.putExtra("NameTo", "Aeroporto");
                    movemeIntent.putExtra("LatitudeTo", airport.getLat());
                    movemeIntent.putExtra("LongitudeTo", airport.getLon());

                    startActivity(movemeIntent);
                }
            });
        }

        //Fragments
        updateFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Botao ajuda
        HeaderFragment.setMsg("Neste ecrã poderá escolher uma das opções listadas para calcular a sua rota.");

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
                .findFragmentById(R.id.move_me_footer);

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

    /**
     * Esta função verifica se um dado pacote está instalado no dispositivo
     * @param packagename Nome do pacote
     * @param context Contexto da aplicação
     * @return True se está instalado, False se não
     */
    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
