package com.moksie.onthemove.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import java.util.TimeZone;

/**
 * Nesta classe são mostradas as informações de um voo seleccionado, dando a opção de poder ser
 * seguido. Contém um fragment que corresponde a dois botões possíveis, seguir ou não o voo.
 * Se já está a seguir um qualquer outro voo ou nenhum, o botão será para seguir, enquanto que se
 * já está a seguir o voo, o botão será para não seguir.
 * No caso de ser um voo do tipo partida, são mostrados o terminal, intervalo do checkin e hora
 * prevista de partida. No caso de ser do tipo chegada, são mostrados o tapete da bagagem, hora
 * prevista da bagagem, porta de desembarque e hora prevista de desembarque.
 * Para a apresentação da informação é passado como parametro um objecto do tipo Flight.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class FlightInfoActivity extends FragmentActivity
{
    /**
     * Variáveis para a máquina de estados, onde é decidido qual o botão a apresentar e gerir o voo
     * a seguir guardado em memória.
     */
    public static int SEGUIR_ATUAL = 0;
    public static int SEGUIR_OUTRO = 1;
    public static int NAO_SEGUIR = 2;

    private int estado = NAO_SEGUIR;//Estado inicial para a máquina de estados
    private Flight flight;//Voo passado como parametro para a atividade

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_flight_info);

        Bundle data = getIntent().getExtras();
        flight = (Flight) data.getParcelable("flight");

        //Atribuição de um estado
        parseVooFile();
        //Atualização dos framents dependendo do estado
        updateFragments(estado);

        //Texto do codigo do voo da barra superior
        TextView CodigoVoo = (TextView) this.findViewById(R.id.top_codigo_voo_textView);
        CodigoVoo.setText(String.valueOf(flight.getCode()));

        //Texto da origem e destino do voo da barra superior
        TextView OrigemDestino = (TextView) this.findViewById(R.id.top_origem_destino_textView);
        OrigemDestino.setText(flight.getDepartureairportcity()+" - "+ flight.getArrivalairportcity());

        //Texto do codigo da companhia aerea da barra superior (ja presente no codigo do voo)
        /*TextView Companhia = (TextView) this.findViewById(R.id.top_companhia_textView);
        Companhia.setText(flight.getAirlinecode());*/

        //Texto do tempo estimado do voo da barra superior (HH:mm)
        TextView TempoEstimado = (TextView) this.findViewById(R.id.top_tempo_estimado_textView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.applyPattern("HH:mm");
        sdf.setTimeZone(TimeZone.getDefault());

        //Campo de informações se o voo for uma partida
        LinearLayout llp = (LinearLayout) findViewById(R.id.partida_info_LinearLayout);
        //Campo de informações se o voo for uma chegada
        LinearLayout llc = (LinearLayout) findViewById(R.id.chegada_info_LinearLayout);

        if(flight.isDeparture()) {
            //No caso de ser uma partida

            //Texto do tempo estimado
            TempoEstimado.setText(sdf.format(flight.getDepartplannedtimeDate()));

            //Texto do terminal
            TextView terminal = (TextView) findViewById(R.id.terminal);
            terminal.setText("Terminal - "+String.valueOf(flight.getBoardingdoorcode()));

            //Texto do checkin
            TextView checkin = (TextView) findViewById(R.id.checkin);
            checkin.setText("Check-In - "+sdf.format(flight.getCheckinopentimeDate())+" às "+sdf.format(flight.getCheckinclosetimeDate()));

            //Texto da porta de embarque
            TextView portaEmbarque = (TextView) findViewById(R.id.porta_embarque);
            portaEmbarque.setText("Embarque - "+String.valueOf(flight.getBoardingdoorcode()));

            //Texto do tempo estimado de embarque
            TextView embarque = (TextView) findViewById(R.id.embarque);
            embarque.setText("Hora Prevista: "+sdf.format(flight.getBoardingopentimeDate()));

            //Informação das partidas visivel e das chegadas eliminado
            llp.setVisibility(ViewGroup.VISIBLE);
            llc.setVisibility(ViewGroup.GONE);
        }
        else {
            //No caso de ser uma chegada
            TempoEstimado.setText(sdf.format(flight.getArrivalplannedtimeDate()));

            //Texto do tapete de bagagem
            TextView tapeteBagagem = (TextView) findViewById(R.id.tapete_bagagem);
            tapeteBagagem.setText("Bagagem - Tapete "+String.valueOf(flight.getLuggageplatforms()));

            //Texto da hora prevista da bagagem
            TextView bagagem = (TextView) findViewById(R.id.bagagem_textView);
            bagagem.setText("Hora Prevista: "+sdf.format(flight.getLuggageopentimeDate()));

            //Texto da porta de desembarque
            /*TextView portaDesembarque = (TextView) findViewById(R.id.porta_desembarque);
            portaDesembarque.setText("Desembarque - "+String.valueOf(flight.getPortadesembarque()));

            //Texto da hora prevista de desembarque
            TextView desembarque = (TextView) findViewById(R.id.desembarque);
            desembarque.setText("Hora Prevista: "+sdf.format(flight.getDesembarque()));*/

            //Informação das partidas eliminado e das chegadas visivel
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
        //updateFragments(estado);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    /**
     * Esta função faz a leitura do ficheiro do voo a seguir guardado em memória e atribui um estado
     * SEGUIR_ATUAL - Caso o voo a seguir seja o mesmo do voo atual
     * SEGUIR_OUTRO - Caso esteja a seguir um outro voo
     * NAO_SEGUIR - Caso não esteja a seguir nenhum voo
     */
    public void parseVooFile()
    {
        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempvoo = FileIO.deserializeFlightObject(MainActivity.FILE_FLIGHT, this).toParcelable();

            if(tempvoo.getCode().equals(flight.getCode()))
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

    /**
     * Nesta função é atualizada a visibilidade do fragmento do voo a seguir dependendo do estado
     *
     * @param estado Representa o estado dependendo da relação entre o voo a seguir e o voo a ser
     *               visualizado.
     */
    public void updateFragments(int estado)
    {
        if(estado == SEGUIR_ATUAL || estado == SEGUIR_OUTRO)
            FooterFragment.setVisibility(true);
        else
            FooterFragment.setVisibility(false);

        updateFooter();
        updateSeguir(estado);
    }

    /**
     * Função de atualização do Fragment Footer que corresponde ao voo que está a ser seguido.
     * Nesta função também são atualizados os tamanhos dos restantes elementos da vista caso o
     * fragment exista ou não.
     */
    public void updateFooter()
    {
        ScrollView layout = (ScrollView) findViewById(R.id.voo_info_resizable);
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.voo_info_footer);

        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempFlight = FileIO.deserializeFlightObject(MainActivity.FILE_FLIGHT, this).toParcelable();

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

    /**
     * Este função é usado quando se carrega no botao de seguir ou nao o voo, sendo atualizado pelo
     * botao oposto.
     *
     * @param estado Representa o estado dependendo da relação entre o voo a seguir e o voo a ser
     *               visualizado.
     */
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

    /**
     * Quando o botao de seguir é carregado é atualizado o estado e o ficheiro do voo a seguir em
     * memória
     */
    public void onClickSeguir()
    {
        if(estado == NAO_SEGUIR || estado == SEGUIR_OUTRO)
        {
            estado = SEGUIR_ATUAL;
            updateSeguir(estado);
            FileIO.removeFile(MainActivity.FILE_FLIGHT, this);
            FileIO.serializeObject(MainActivity.FILE_FLIGHT, flight.toSerializable(), this);

            //Footer
            updateFooter();
        }
    }

    /**
     * Quando o botao de nao seguir é carregado é atualizado o estado e eliminado o ficheiro do voo
     * a seguir da memória
     */
    public void onClickNaoSeguir()
    {
        if(estado == SEGUIR_ATUAL)
        {
            estado = NAO_SEGUIR;
            updateSeguir(estado);
            FileIO.removeFile(MainActivity.FILE_FLIGHT, this);

            //Footer
            updateFooter();
        }
    }
}
