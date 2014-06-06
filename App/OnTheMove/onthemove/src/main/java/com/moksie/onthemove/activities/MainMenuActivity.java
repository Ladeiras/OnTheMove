package com.moksie.onthemove.activities;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.MapsListAdapter;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.objects.FlightSerializable;
import com.moksie.onthemove.objects.Map;
import com.moksie.onthemove.objects.Service;
import com.moksie.onthemove.utilities.FileIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainMenuActivity extends FragmentActivity {

    private static final String ID = "Id";
    private static final String CODIGOVOO = "CodigoVoo";
    private static final String CODIGOCOMPANHIA = "CodigoCompanhia";
    private static final String PARTIDACIDADE = "PartidaCidade";
    private static final String CHEGADACIDADE = "ChegadaCidade";
    private static final String PARTIDAAEROPORTOID = "PartidaAeroportoId";
    private static final String CHEGADAAEROPORTOID = "ChegadaAeroportoId";
    private static final String PARTIDATEMPOESTIMADO = "PartidaTempoEstimado";
    private static final String CHEGADATEMPOESTIMADO = "ChegadaTempoEstimado";
    private static final String PARTIDATEMPOREAL = "PartidaTempoReal";
    private static final String CHEGADATEMPOREAL = "ChegadaTempoReal";

    private static final String TERMINAL = "Terminal";
    private static final String CHECKININICIO = "CheckinInicio";
    private static final String CHECKINFIM = "CheckinFim";
    private static final String PORTAEMBARQUE = "PortaEmbarque";
    private static final String EMBARQUE = "Embarque";
    private static final String TAPETEBAGAGEM = "TapeteBagagem";
    private static final String BAGAGEM = "Bagagem";
    private static final String PORTADESEMBARQUE = "PortaDesembarque";
    private static final String DESEMBARQUE = "Desembarque";

    private static String FLIGHT_API_URL = "http://onthemove.no-ip.org:3000/api/voos/";

    public static int TIMER_INTERVAL = 30;
    public static boolean FOLLOWING = false;

    private static final long ONE_HOUR = 3600000;

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

    int state = INITIAL_STATE;

    private Airport airport;
    private Flight flightASeguir;
    private Context ctx = this;
    private ScheduledExecutorService scheduler;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                MainMenuActivity.this.startActivity(intent);
            }
        });
		
		//Fragments
        updateFragments();

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        //Toast.makeText(ctx,"TOSTA",3000).show();
                        //MainActivity.currentTime
                        Log.d("SCHEDULE", "yep");
                        try {
                            notificationController();
                        } catch (ParseException e) {
                            Log.e("Error","Notification controller");
                            e.printStackTrace();
                        }
                    }
                }, 0, TIMER_INTERVAL, TimeUnit.SECONDS);
    }

    private void notificationController() throws ParseException {
        if(FooterFragment.visibility) {

            //createNotification("Teste1", "Teste2", "Teste3");
            long diff;

            FlightSerializable followingFlight = getFlightSerializable();
            Flight updatedFlight = getFlight(FooterFragment.flight);
            updateFlight(updatedFlight.toSerializable(), followingFlight);

            Log.d("FLIGHTUPDATED", "true");

            long diffPartida = getDateDiff(MainActivity.currentTime,updatedFlight.getPartidatempoestimado(),TimeUnit.MILLISECONDS);
            long diffEmbarque = getDateDiff(MainActivity.currentTime,updatedFlight.getEmbarque(),TimeUnit.MILLISECONDS);
            long diffChekinInicio = getDateDiff(MainActivity.currentTime,updatedFlight.getCheckininicio(),TimeUnit.MILLISECONDS);
            long diffChekinFim = getDateDiff(MainActivity.currentTime,updatedFlight.getCheckinfim(),TimeUnit.MILLISECONDS);

            long diffDesembarque = getDateDiff(MainActivity.currentTime,updatedFlight.getDesembarque(),TimeUnit.MILLISECONDS);
            long diffBagagem = getDateDiff(MainActivity.currentTime,updatedFlight.getBagagem(),TimeUnit.MILLISECONDS);

            boolean BoardingCB = followingFlight.isBoarding();
            //boolean SecurityCB = followingFlight.isSecurity();
            boolean CheckinCB = followingFlight.isCheckin();
            boolean AirportCB = followingFlight.isAirport();

            state = INITIAL_STATE;

            boolean isPartida = FooterFragment.flight.isPartida();

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
                                createNotification("Checkin", "Checkin", "Poderá fazer chekin dentro de " + diff + " minutos");
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
                                createNotification("Checkin", "Checkin", "Faltam " + diff + " minutos para terminar");
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
                                createNotification("Embarque", "Embarque", "Faltam " + diff + " minutos");
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
                            if(diffDesembarque > 0)
                                state = S_DESEMBARQUE;
                            else
                                state = END_STATE;
                        }
                        else
                        {
                            if(diffBagagem < ONE_HOUR) {
                                diff = (int) ((diffBagagem / (1000 * 60)) % 60);
                                createNotification("Bagagem", "Tapete " + updatedFlight.getTapetebagagem(), "Faltam " + diff + " minutos");
                                Log.d("UPDATEDFLIGHT", "Bagagem: Faltam " + diff + " minutos");
                                state = END;
                            }
                            else state = END_STATE;
                        }
                        break;
                    case S_DESEMBARQUE:
                        if(diffDesembarque < 0)
                        {
                            state = END_STATE;
                        }
                        else
                        {
                            if(diffDesembarque < ONE_HOUR) {
                                diff = (int) ((diffDesembarque / (1000 * 60)) % 60);
                                createNotification("Desembarque", "Porta " + updatedFlight.getPortadesembarque(), "Faltam " + diff + " minutos");
                                Log.d("UPDATEDFLIGHT", "Desembarque: Faltam " + diff + " minutos");
                                state = END;
                            }
                            else state = END_STATE;
                        }
                        break;
                    case END_STATE:
                        notificationManager.cancel(01);
                        FooterFragment.setVisibility(false);
                        state = END;
                        break;
                }
            }
        }
        else {
            notificationManager.cancel(01);
        }
    }

    private void updateFlight(FlightSerializable updatedFlight, FlightSerializable followingFlight)
    {
        updatedFlight.setAirport(followingFlight.isAirport());
        updatedFlight.setCheckin(followingFlight.isCheckin());
        updatedFlight.setSecurity(followingFlight.isSecurity());
        updatedFlight.setBoarding(followingFlight.isBoarding());

        FileIO.removeFile(MainActivity.FILE_FLIGHT, getApplicationContext());
        FileIO.serializeObject(MainActivity.FILE_FLIGHT, updatedFlight, getApplicationContext());
    }

    public Flight getFlight(Flight flight) throws ParseException {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);

        BGTGetJSONObject bgt = new BGTGetJSONObject(FLIGHT_API_URL+flight.getId(), "GET", apiParams);
        Flight updatedFlight = null;

        try {
            JSONObject flightJSON = bgt.execute().get();

            long id = flightJSON.getLong(ID);
            long CodigoVoo = flightJSON.getLong(CODIGOVOO);
            String CodigoCompanhia = flightJSON.getString(CODIGOCOMPANHIA);
            String PartidaCidade = flightJSON.getString(PARTIDACIDADE);
            String ChegadaCidade = flightJSON.getString(CHEGADACIDADE);
            long PartidaAeroportoId = flightJSON.getLong(PARTIDAAEROPORTOID);
            long ChegadaAeroportoId = flightJSON.getLong(CHEGADAAEROPORTOID);
            String PartidaTempoEstimadoStr = flightJSON.getString(PARTIDATEMPOESTIMADO);
            String ChegadaTempoEstimadoStr = flightJSON.getString(CHEGADATEMPOESTIMADO);
            String PartidaTempoRealStr = flightJSON.getString(PARTIDATEMPOREAL);
            String ChegadaTempoRealStr = flightJSON.getString(CHEGADATEMPOREAL);

            long Terminal = flightJSON.getLong(TERMINAL);
            String CheckinInicioStr = flightJSON.getString(CHECKININICIO);
            String CheckinFimStr = flightJSON.getString(CHECKINFIM);
            long PortaEmbarque = flightJSON.getLong(PORTAEMBARQUE);
            String EmbarqueStr = flightJSON.getString(EMBARQUE);
            long TapeteBagagem = flightJSON.getLong(TAPETEBAGAGEM);
            String BagagemStr = flightJSON.getString(BAGAGEM);
            long PortaDesembarque = flightJSON.getLong(PORTADESEMBARQUE);
            String DesembarqueStr = flightJSON.getString(DESEMBARQUE);

            Date PartidaTempoEstimado = MainActivity.sdf.parse(PartidaTempoEstimadoStr);
            Date ChegadaTempoEstimado = MainActivity.sdf.parse(ChegadaTempoEstimadoStr);
            Date PartidaTempoReal = MainActivity.sdf.parse(PartidaTempoRealStr);
            Date ChegadaTempoReal = MainActivity.sdf.parse(ChegadaTempoRealStr);
            Date CheckinInicio = MainActivity.sdf.parse(CheckinInicioStr);
            Date CheckinFim = MainActivity.sdf.parse(CheckinFimStr);
            Date Embarque = MainActivity.sdf.parse(EmbarqueStr);
            Date Bagagem = MainActivity.sdf.parse(BagagemStr);
            Date Desembarque = MainActivity.sdf.parse(DesembarqueStr);

            updatedFlight = new Flight(id,CodigoVoo,CodigoCompanhia,PartidaCidade,
                    ChegadaCidade,PartidaAeroportoId,ChegadaAeroportoId,PartidaTempoEstimado,
                    ChegadaTempoEstimado,PartidaTempoReal,ChegadaTempoReal,
                    Terminal,CheckinInicio,CheckinFim,PortaEmbarque,Embarque,TapeteBagagem,
                    Bagagem,PortaDesembarque,Desembarque,flight.isPartida());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return updatedFlight;
    }

    private void createNotification(String ticker, String contentTitle, String contentText)
    {
        Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_icon_sobre),
                getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width),
                getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_height),
                true);

        int requestID = (int) System.currentTimeMillis();

        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
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
        scheduler.shutdown();
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
            }

            return true;
        }

        return false;
    }

    class BGTGetJSONObject extends AsyncTask<String, String, JSONObject> {

        List<NameValuePair> postparams = new ArrayList<NameValuePair>();
        String URL = null;
        String method = null;
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        public BGTGetJSONObject(String url, String method, List<NameValuePair> params) {
            this.URL = url;
            this.postparams = params;
            this.method = method;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            // Making HTTP request
            try {
                // Making HTTP request
                // check for request method

                if (method.equals("POST")) {
                    // request method is POST
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(URL);
                    httpPost.setEntity(new UrlEncodedFormEntity(postparams));

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();

                } else if (method == "GET") {
                    // request method is GET
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    String paramString = URLEncodedUtils
                            .format(postparams, "utf-8");
                    URL += "?" + paramString;
                    HttpGet httpGet = new HttpGet(URL);

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                }

                // read input stream returned by request into a string using StringBuilder
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();

                // create a JSONObject from the json string
                jObj = new JSONObject(json);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            // return JSONObject (this is a class variable and null is returned if something went bad)
            return jObj;
        }
    }

    public FlightSerializable getFlightSerializable()
    {
        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler flight do ficheiro
            return FileIO.deserializeVooObject(MainActivity.FILE_FLIGHT, this);
        }

        return null;
    }
}
