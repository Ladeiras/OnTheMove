package com.moksie.onthemove.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.objects.FlightSerializable;
import com.moksie.onthemove.utilities.FileIO;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Sendo a atividade central da aplicação, nesta classe é feita a gestão das notificações a partir
 * de uma Thread (Scheduler). É também a classe responsável por mostrar o menu inicial onde é
 * possível encaminhar o utilizador para o resto das funcionalidades.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class MenuMainActivity extends FragmentActivity {

    public static int TIMER_INTERVAL = 10; //Intervalo de tempo em que é feita a atualização das
                                           // notificações
    private static final long ONE_HOUR = 3600000;

    /*
     * Valores usados na máquina de estados das notificações para representar estados diferentes,
     * sendo que a variavel de security poderá ser usada futuramente
     */
    private static final int INITIAL_STATE = 0;
    private static final int S_SHOW_ALL = 2;
    private static final int S_CHECKININICIO = 3;
    //private static final int S_SECURITY = 4;
    private static final int S_BOARDING = 5;
    private static final int S_CHECKINFIM = 6;
    private static final int S_PARTIDA = 7;
    private static final int S_BAGAGEM = 8;
    private static final int S_DESEMBARQUE = 9;
    private static final int END_STATE = 10;
    private static final int END = 11;

    int state = INITIAL_STATE; //Váriavel do estado da máquina de estados das notificações

    private Airport airport; //Dados do aeroporto seleccionado
    private ScheduledExecutorService scheduler;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        //Dados do aeroporto passado da MainActivity
        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("airport");

        //Verificar se o voo a seguir pertence ao aeroporto escolhido
        parseVooFile();

        //Botao Voos
        final Button voosButton = (Button) findViewById(R.id.voos_button);
        voosButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuMainActivity.this, FlightListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("airport", airport);
                MenuMainActivity.this.startActivity(intent);
            }
        });

        //Botao Calculo Rota
        final Button cdRButton = (Button) findViewById(R.id.moveme_button);
        cdRButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuMainActivity.this, MenuRoutesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("airport", airport);
                MenuMainActivity.this.startActivity(intent);
            }
        });

        //Botao Servicos
        final Button serviceButton = (Button) findViewById(R.id.services_button);
        serviceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuMainActivity.this, MenuServicesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("airport", airport);
                MenuMainActivity.this.startActivity(intent);
            }
        });

        //Botao Contactos
        final Button contactsButton = (Button) findViewById(R.id.contacts_button);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuMainActivity.this, MenuContactsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("airport", airport);
                MenuMainActivity.this.startActivity(intent);
            }
        });

        //Botao Sobre
        final Button aboutButton = (Button) findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuMainActivity.this, AboutActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                MenuMainActivity.this.startActivity(intent);
            }
        });
		
		//Fragments
        updateFragments();

        //Notification Manager
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Scheduler - Thread que é repetida entre TIMER_INTERVAL, para verificação do tempo atual
        //em relacao ao tempo dos vários passos do utilizador para poder notifica-lo
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        Log.d("SCHEDULE", "tick");
                        try {
                            notificationController();
                        } catch (ParseException e) {
                            Log.e("Error","Notification controller");
                            e.printStackTrace();
                        }
                    }
                }, 0, TIMER_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * Esta função é chamada com um intervalo de TIMER_INTERVAL. É passado o objecto de voo a ser
     * seguido, se este existir, e são feitas as diferenças de tempos entre o tempo atual e os
     * limites de tempo dos diferentes passos a ser tomados pelo utilizador.
     * Correndo uma máquina de estados onde são verificadas as CheckBoxes que o utilizador já
     * tem marcadas e as diferenças temporais, é possível tomar uma decisão quanto à notificação a
     * ser emitida.
     *
     * @throws ParseException
     */
    private void notificationController() throws ParseException {
        if(FooterFragment.visibility) {
            long diff;

            FlightSerializable followingFlight = getFlightSerializable();

            Log.d("FLIGHTUPDATED", "true");

            //Valores das diferenças entre os limites de tempo dos passos e o tempo atual
            // (milisegundos)
            long diffPartida = getDateDiff(MainActivity.currentTime,followingFlight.getDepartplannedtimeDate(),TimeUnit.MILLISECONDS);
            Log.d("diffPartida", ""+diffPartida);
            long diffEmbarque = getDateDiff(MainActivity.currentTime,followingFlight.getBoardingopentimeDate(),TimeUnit.MILLISECONDS);
            Log.d("diffEmbarque", ""+diffEmbarque);
            long diffChekinInicio = getDateDiff(MainActivity.currentTime,followingFlight.getCheckinopentimeDate(),TimeUnit.MILLISECONDS);
            Log.d("diffChekinInicio", ""+diffChekinInicio);
            long diffChekinFim = getDateDiff(MainActivity.currentTime,followingFlight.getCheckinclosetimeDate(),TimeUnit.MILLISECONDS);
            Log.d("diffChekinFim", ""+diffChekinFim);

            long diffBagagem = getDateDiff(MainActivity.currentTime,followingFlight.getLuggageopentimeDate(),TimeUnit.MILLISECONDS);
            Log.d("diffBagagem", ""+diffBagagem);

            //Valores das CheckBoxes
            boolean BoardingCB = followingFlight.isBoarding();
            //boolean SecurityCB = followingFlight.isSecurity();
            boolean CheckinCB = followingFlight.isCheckin();
            boolean AirportCB = followingFlight.isAirport();

            state = INITIAL_STATE; //Estado inicial

            boolean isPartida = FooterFragment.flight.isDeparture(); //Tipo de Voo
                                                                     //(Partida ou Chegada)

            //Máquina de estados
            while(state != END) {
                switch (state) {
                    case INITIAL_STATE:
                        if(isPartida) {
                            if (!AirportCB)
                                state = S_SHOW_ALL;
                            else if (!CheckinCB)
                                state = S_CHECKININICIO;
                            else if (!BoardingCB)
                                state = S_BOARDING;
                            else
                                state = END_STATE;
                        }
                        else
                        {
                            state = S_BAGAGEM;
                        }
                        break;
                    case S_SHOW_ALL:
                        if (diffChekinInicio > 0)
                            state = S_CHECKININICIO;
                        else
                            state = S_CHECKINFIM;
                        break;
                    case S_CHECKININICIO:
                        if (diffChekinInicio < 0) {
                            if (diffChekinFim > 0)
                                state = S_CHECKINFIM;
                            else
                                state = S_BOARDING;
                        } else {

                            if(diffChekinInicio < ONE_HOUR) {
                                diff = (int) ((diffChekinInicio / (1000 * 60)) % 60);
                                createCustomNotification("Checkin", "Checkin", "Poderá fazer chekin dentro de " + diff + " minutos");
                                Log.d("UPDATEDFLIGHT", "Checkin: Poderá fazer chekin dentro de " + diff + " minutos");
                                state = END;
                            }
                            else state = END_STATE;
                        }
                        break;
                    case S_CHECKINFIM:
                        if (diffChekinFim < 0) {
                            if (diffEmbarque > 0)
                                state = S_BOARDING;
                            else
                                state = S_PARTIDA;
                        } else {

                            if(diffChekinFim < ONE_HOUR) {
                                diff = (int) ((diffChekinFim / (1000 * 60)) % 60);
                                createCustomNotification("Checkin", "Checkin", "Faltam " + diff + " minutos para terminar");
                                Log.d("UPDATEDFLIGHT", "Checkin: Faltam " + diff + " minutos para terminar");
                                state = END;
                            }
                            else state = END_STATE;
                        }
                        break;
                    case S_BOARDING:
                        if (diffEmbarque < 0) {
                            if (diffPartida > 0)
                                state = S_PARTIDA;
                            else
                                state = END_STATE;
                        } else {

                            if(diffEmbarque < ONE_HOUR) {
                                diff = (int) ((diffEmbarque / (1000 * 60)) % 60);
                                createCustomNotification("Embarque", "Embarque", "Faltam " + diff + " minutos");
                                Log.d("UPDATEDFLIGHT", "Embarque: Faltam " + diff + " minutos");
                                state = END;
                            }
                            else state = END_STATE;
                        }
                        break;
                    case S_PARTIDA:
                        if (diffPartida < 0)
                            state = END_STATE;
                        else
                            state = END;
                        break;
                    case S_BAGAGEM:
                        if(diffBagagem < 0)
                        {
                            state = END_STATE;
                        }
                        else
                        {
                            if(diffBagagem < ONE_HOUR) {
                                diff = (int) ((diffBagagem / (1000 * 60)) % 60);
                                createCustomNotification("Bagagem", "Tapete " + followingFlight.getLuggageplatforms(), "Faltam " + diff + " minutos");
                                Log.d("UPDATEDFLIGHT", "Bagagem: Faltam " + diff + " minutos");
                                state = END;
                            }
                            else state = END_STATE;
                        }
                        break;
                    case END_STATE:
                        notificationManager.cancel(01); //Remove a notificação
                        FooterFragment.setVisibility(false); //Evita a entrada na máquina de estados
                        state = END;
                        break;
                }
            }
        }
        else {
            notificationManager.cancel(01);
        }
    }

    /**
     * Esta função cria uma nova notificação (01) e só é compativel com as versões superiores à
     * API 8, pelo que neste momento não está em uso.
     *
     * @param ticker Mensagem que aparece diretamente na barra de notificações
     * @param contentTitle Titulo da notificação
     * @param contentText Texto da notificação
     */
    private void createNotification(String ticker, String contentTitle, String contentText)
    {
        Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_icon_sobre),
                getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width),
                getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_height),
                true);

        int requestID = (int) System.currentTimeMillis();

        Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
        intent.putExtra("airport", airport);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);
        builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);

        builder.setTicker(ticker);
        builder.setSmallIcon(R.drawable.ic_icon_sobre);
        builder.setLargeIcon(bm);
        builder.setAutoCancel(true);
        builder.setPriority(0);
        builder.setOngoing(true);

        final Notification notification = builder.build();
        //notification.flags = Notification.FLAG_NO_CLEAR;

        notificationManager.notify(01,notification);
    }

    /**
     * Esta função cria uma nova notificação (01) e é compativel com todas as versões
     *
     * @param ticker Mensagem que aparece diretamente na barra de notificações
     * @param contentTitle Titulo da notificação
     * @param contentText Texto da notificação
     */
    public void createCustomNotification(String ticker, String contentTitle, String contentText)
    {
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.custom_notification);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_logo_onmove).setContent(
                remoteViews);

        Intent resultIntent = new Intent(this, MenuMainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MenuMainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentTitle(contentTitle);
        mBuilder.setContentText(contentText);
        mBuilder.setTicker(ticker);
        Notification notification = mBuilder.build();

        notification.contentIntent = resultPendingIntent;
        notificationManager.notify(01, notification);
    }

    /**
     * Calculo da diferença entre um par de Date
     *
     * @param date1
     * @param date2
     * @param timeUnit Escolha do tempo que é retornado
     * @return Diferença entre date1 e date2 em timeUnit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit)
    {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Botao ajuda
        HeaderFragment.setMsg("Neste menu pode escolher a opção a realizar através dos ícones presentes no painel.");

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
        LinearLayout layout = (LinearLayout) findViewById(R.id.menu_resizable);
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.menu_footer);

        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempFlight = FileIO.deserializeFlightObject(MainActivity.FILE_FLIGHT, this).toParcelable();

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
        scheduler.shutdown();
    }

    /**
     * Nesta função é verificado se o ficheiro 'flight' existe, o que indica se o utilizador está ou
     * não a seguir um voo.
     * Caso o aeroporto escolhido inicialmente não corresponda à partida ou à chegada, o ficheiro
     * é removido
     *
     * @return True se o ficheiro existe, False se não
     */
    public boolean parseVooFile()
    {
        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight flight = FileIO.deserializeFlightObject(MainActivity.FILE_FLIGHT, this).toParcelable();

            if((!flight.getDepartureairportcode().equals(airport.getCode()) && flight.isDeparture())||
                (!flight.getArrivalairportcode().equals(airport.getCode()) && !flight.isDeparture()))
            {
                FileIO.removeFile(MainActivity.FILE_FLIGHT, this);
            }

            return true;
        }

        return false;
    }

    /**
     *
     * @return Objecto serializável voo que está a ser seguido
     */
    public FlightSerializable getFlightSerializable()
    {
        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            return FileIO.deserializeFlightObject(MainActivity.FILE_FLIGHT, this);
        }

        return null;
    }
}
