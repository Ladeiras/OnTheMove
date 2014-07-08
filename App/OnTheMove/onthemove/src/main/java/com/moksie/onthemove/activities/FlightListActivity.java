package com.moksie.onthemove.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.FlightAdapter;
import com.moksie.onthemove.adapters.SearchAdapter;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FlightListActivity extends FragmentActivity {

    /*private static final String ID = "Id";
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
    private static final String DESEMBARQUE = "Desembarque";*/

    private static final int TAB_TEXT_SIZE = 25;
    private static final int TAB_PARTIDAS = 0;
    private static final int TAB_CHEGADAS = 1;
    private static final int OPTION_NUMERO_VOO = 0;
    private static final int OPTION_ORIGEM_DESTINO = 1;
    private static final int OPTION_COMPANHIA = 2;
    private static final int OPTION_SHOW_ALL = 3;
    public static final int OPTION_DEFAULT = 4;
    private static final String OPTION_NUMERO_VOO_VALUE = "Número do Voo";
    private static final String OPTION_ORIGEM_DESTINO_VALUE_ORIGEM = "Origem";
    private static final String OPTION_ORIGEM_DESTINO_VALUE_DESTINO = "Destino";
    private static final String OPTION_COMPANHIA_VALUE = "Companhia Aérea";
    private static final String OPTION_SHOW_ALL_VALUE = "Todos os Voos";
    private static final String OPTION_DEFAULT_VALUE = "";


    //OPT Flight
    private static final String CODE = "Code";
    private static final String CITY = "City";

    private static final String AIRLINECODE = "AirlineCode";
    private static final String ARRIVALAIRPORT_TAG = "ArrivalAirport";
    private static final String ARRIVALPLANNEDTIME = "ArrivalPlannedTime";
    private static final String ARRIVALREALTIME = "ArrivalRealTime";
    private static final String BOARDINGCLOSETIME = "BoardingCloseTime";
    private static final String BOARDINGDOORCODE = "BoardingDoorCode";
    private static final String BOARDINGDURATION = "BoardingDuration";
    private static final String BOARDINGOPENTIME = "BoardingOpenTime";
    private static final String CHECKINCODES = "CheckInCodes";
    private static final String CHECKINCLOSETIME = "CheckinCloseTime";
    private static final String CHECKINDURATION = "CheckinDuration";
    private static final String CHECKINOPENTIME = "CheckinOpenTime";
    private static final String DEPARTAIRPORT_TAG = "DepartAirport";
    private static final String DEPARTPLANNEDTIME = "DepartPlannedTime";
    private static final String DEPARTREALTIME = "DepartRealTime";
    private static final String LUGGAGECLOSETIME = "LuggageCloseTime";
    private static final String LUGGAGEOPENTIME = "LuggageOpenTime";
    private static final String LUGGAGEPICKAVGDURATION = "LuggagePickAvgDuration";
    private static final String LUGGAGEPLATFORMS = "LuggagePlatforms";
    private static final String STATUS = "Status";

    private static final String GET_DEPARTURES = "getDepartures";
    private static final String GET_ARRIVALS = "getArrivals";

    private static final String FROM_TIME_PARAM = "fromTime";
    private static final String TO_TIME_PARAM = "toTime";
    private static final String MAX_COUNT_PARAM = "maxCount";

    ArrayList<Flight> departures = new ArrayList<Flight>();
    ArrayList<Flight> arrivals = new ArrayList<Flight>();

    private static final boolean DEPARTURE_TYPE = true;
    private static final boolean ARRIVAL_TYPE = false;

    private ListView departuresList;
    private ListView arrivalsList;

    private ProgressDialog pd;
    private Airport airport;
    private TabHost mTabHost;
    private Flight flightASeguir;
    private BGTGetJSONArray bgt;

    /*String PARTIDA_API_URL;
    String CHEGADA_API_URL;

    private Airport airport;
    private ListView partidasList;
    private ListView chegadasList;

    ArrayList<Flight> partidas = new ArrayList<Flight>();
    ArrayList<Flight> chegadas = new ArrayList<Flight>();*/

    final ArrayList<String> options = new ArrayList(Arrays.asList(OPTION_NUMERO_VOO_VALUE,
            OPTION_ORIGEM_DESTINO_VALUE_DESTINO, OPTION_COMPANHIA_VALUE, OPTION_SHOW_ALL_VALUE, OPTION_DEFAULT_VALUE));

    //ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights);

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("airport");

        pd = new ProgressDialog(this);
        pd.setMessage("A carregar os Voos");

        //Botao ajuda
        HeaderFragment.setMsg("Neste ecrã pode selecionar os separadores para consultar os voos. Os voos estão divididos em partidas e chegadas. Clicando num voo pode consultar informações do mesmo. Poderá ainda usar os filtros de pesquisa.");

        //Listas de Voos
        if(departures.isEmpty() && arrivals.isEmpty())
        {
            try {
                //buildPartidasList();
                //buildChegadasList();
                buildDeparturesList();
                buildArrivalsList();
            } catch (ParseException e) {
                Log.e("FLAG", "Error creating flights list");
                e.printStackTrace();
            }

            mTabHost = (TabHost) findViewById(R.id.tabHost);
            mTabHost.setup();

            //Tabs
            TabHost.TabSpec tabPartidas = mTabHost.newTabSpec("Departures");
            TabHost.TabSpec tabChegadas = mTabHost.newTabSpec("Arrivals");

            //Tab Indicators
            //View tabPartidasIndicator = LayoutInflater.from(this).inflate(R.layout.onthemovetheme_tab_indicator_holo, mTabHost.getTabWidget(), false);
            //TextView titlePartidas = (TextView) tabPartidasIndicator.findViewById(android.R.id.title);
            //titlePartidas.setText("Partidas");
            //tabPartidas.setIndicator(tabPartidasIndicator);
            tabPartidas.setIndicator("", getResources().getDrawable(R.drawable.tab_departures));

            //View tabChegadasIndicator = LayoutInflater.from(this).inflate(R.layout.onthemovetheme_tab_indicator_holo, mTabHost.getTabWidget(), false);
            //TextView titleChegadas = (TextView) tabChegadasIndicator.findViewById(android.R.id.title);
            //titleChegadas.setText("Chegadas");
            //tabPartidas.setIndicator(tabChegadasIndicator);
            tabChegadas.setIndicator("", getResources().getDrawable(R.drawable.tab_arrivals));

            //Tabs Views
            tabPartidas.setContent(new TabHost.TabContentFactory() {

                public View createTabContent(String tag) {
                    return departuresList;
                }
            });

            tabChegadas.setContent(new TabHost.TabContentFactory() {

                public View createTabContent(String tag) {
                    return arrivalsList;
                }
            });


            mTabHost.addTab(tabPartidas);
            mTabHost.addTab(tabChegadas);

            //Tab colors
            mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String s) {

                    TextView title0 = (TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
                    TextView title1 = (TextView) mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
                    title0.setTextSize(TAB_TEXT_SIZE);
                    title1.setTextSize(TAB_TEXT_SIZE);

                    if (mTabHost.getCurrentTab() == TAB_PARTIDAS) {
                        mTabHost.getTabWidget().getChildAt(TAB_PARTIDAS).setBackgroundResource(R.drawable.tab_active);
                        mTabHost.getTabWidget().getChildAt(TAB_CHEGADAS).setBackgroundResource(R.drawable.tab_inactive);
                        title0.setTextColor(getResources().getColor(R.color.active_tab_color));
                        title1.setTextColor(getResources().getColor(R.color.inactive_tab_color));
                    } else {
                        mTabHost.getTabWidget().getChildAt(TAB_PARTIDAS).setBackgroundResource(R.drawable.tab_inactive);
                        mTabHost.getTabWidget().getChildAt(TAB_CHEGADAS).setBackgroundResource(R.drawable.tab_active);
                        title0.setTextColor(getResources().getColor(R.color.inactive_tab_color));
                        title1.setTextColor(getResources().getColor(R.color.active_tab_color));
                    }

                    changeSearchOption(mTabHost.getCurrentTab());
                }
            });

            //Tabs refresh
            mTabHost.setCurrentTab(TAB_CHEGADAS);
            mTabHost.setCurrentTab(TAB_PARTIDAS);

            departuresList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int i, long l) {
                    Intent intent = new Intent(FlightListActivity.this, FlightInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Flight flight = (Flight) departuresList.getAdapter().getItem(i);
                    intent.putExtra("flight", flight);
                    FlightListActivity.this.startActivity(intent);
                }
            });

            arrivalsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int i, long l) {
                    Intent intent = new Intent(FlightListActivity.this, FlightInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Flight flight = (Flight) arrivalsList.getAdapter().getItem(i);
                    intent.putExtra("flight", flight);
                    FlightListActivity.this.startActivity(intent);
                }
            });
        }

        //Search
        final Spinner searchVoosSpinner = (Spinner)findViewById(R.id.search_spinner);
        SearchAdapter adapter = new SearchAdapter(this, android.R.layout.simple_spinner_item, options);

        searchVoosSpinner.setAdapter(adapter);
        searchVoosSpinner.setSelection(OPTION_DEFAULT);

        searchVoosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                final int option = i;

                if(option == OPTION_SHOW_ALL) {
                    if(mTabHost.getCurrentTab() == TAB_PARTIDAS)
                        updateDepartures(departures);
                    if(mTabHost.getCurrentTab() == TAB_CHEGADAS)
                        updateArrivals(arrivals);
                }
                else if(option != OPTION_DEFAULT) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(FlightListActivity.this);

                    alert.setTitle("Pesquisar por " + options.get(option));
                    //alert.setMessage(options.get(i));

                    // Set an EditText view to get user input
                    final EditText input = new EditText(FlightListActivity.this);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = String.valueOf(input.getText());
                            filterFlights(value,mTabHost.getCurrentTab(),option);
                        }
                    });

                    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    alert.show();
                }

                searchVoosSpinner.setSelection(OPTION_DEFAULT);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        updateFragments();
        //loadingDialog.dismiss();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    private void filterFlights(String value, int currentTab, int option)
    {
        ArrayList<Flight> temp = new ArrayList<Flight>();

        if(currentTab==TAB_PARTIDAS)
        {
            if(option==OPTION_NUMERO_VOO)
            {
                for(int i=0;i<departures.size();i++)
                    if(String.valueOf(departures.get(i).getCode()).contains(value) ||
                            value.contains(String.valueOf(departures.get(i))))
                        temp.add(departures.get(i));
            }
            else if(option==OPTION_COMPANHIA)
            {
                for(int i=0;i<departures.size();i++)
                    if(departures.get(i).getAirlinecode().toLowerCase().contains(value.toLowerCase()) ||
                            value.toLowerCase().contains(departures.get(i).getAirlinecode().toLowerCase()))
                        temp.add(departures.get(i));
            }
            else if(option==OPTION_ORIGEM_DESTINO)
            {
                for(int i=0;i<departures.size();i++)
                    if(departures.get(i).getArrivalairportcity().toLowerCase().contains(value.toLowerCase()) ||
                            value.toLowerCase().contains(departures.get(i).getArrivalairportcity().toLowerCase()))
                        temp.add(departures.get(i));
            }

            if(!temp.isEmpty()) {
                updateDepartures(temp);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Não foram encontrados resultados",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if(currentTab==TAB_CHEGADAS)
        {
            if(option==OPTION_NUMERO_VOO)
            {
                for(int i=0;i<arrivals.size();i++)
                    if(String.valueOf(arrivals.get(i).getCode()).contains(value) ||
                            value.contains(String.valueOf(arrivals.get(i))))
                        temp.add(arrivals.get(i));
            }
            else if(option==OPTION_COMPANHIA)
            {
                for(int i=0;i<arrivals.size();i++)
                    if(arrivals.get(i).getAirlinecode().toLowerCase().contains(value.toLowerCase()) ||
                            value.toLowerCase().contains(arrivals.get(i).getAirlinecode().toLowerCase()))
                        temp.add(arrivals.get(i));
            }
            else if(option==OPTION_ORIGEM_DESTINO)
            {
                for(int i=0;i<arrivals.size();i++)
                    if(arrivals.get(i).getDepartureairportcity().toLowerCase().contains(value.toLowerCase()) ||
                            value.toLowerCase().contains(arrivals.get(i).getDepartureairportcity().toLowerCase()))
                        temp.add(arrivals.get(i));
            }

            if(!temp.isEmpty()) {
                updateArrivals(temp);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Não foram encontrados resultados",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateDepartures(ArrayList<Flight> flights) {
        FlightAdapter departuresAdapter = new FlightAdapter(this, android.R.layout.simple_expandable_list_item_1, flights);
        departuresList = (ListView) findViewById(R.id.listView_partidas);
        departuresList.setAdapter(departuresAdapter);
    }

    private void updateArrivals(ArrayList<Flight> flights) {
        FlightAdapter arrivalsAdapter = new FlightAdapter(this, android.R.layout.simple_expandable_list_item_1, flights);
        arrivalsList = (ListView) findViewById(R.id.listView_chegadas);
        arrivalsList.setAdapter(arrivalsAdapter);
    }

    /*private void updatePartidas(ArrayList<Flight> flights) {
        FlightAdapter partidasAdapter = new FlightAdapter(this, android.R.layout.simple_expandable_list_item_1, flights);
        partidasList = (ListView) findViewById(R.id.listView_partidas);
        partidasList.setAdapter(partidasAdapter);
    }

    private void updateChegadas(ArrayList<Flight> flights) {
        FlightAdapter chegadasAdapter = new FlightAdapter(this, android.R.layout.simple_expandable_list_item_1, flights);
        chegadasList = (ListView) findViewById(R.id.listView_chegadas);
        chegadasList.setAdapter(chegadasAdapter);
    }*/

    private void changeSearchOption(int currentTab) {
        if(currentTab==TAB_PARTIDAS)
            if(options.get(OPTION_ORIGEM_DESTINO).equals(OPTION_ORIGEM_DESTINO_VALUE_ORIGEM))
                options.set(OPTION_ORIGEM_DESTINO,OPTION_ORIGEM_DESTINO_VALUE_DESTINO);

        if(currentTab==TAB_CHEGADAS)
            if(options.get(OPTION_ORIGEM_DESTINO).equals(OPTION_ORIGEM_DESTINO_VALUE_DESTINO))
                options.set(OPTION_ORIGEM_DESTINO,OPTION_ORIGEM_DESTINO_VALUE_ORIGEM);
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
        LinearLayout tabs = (LinearLayout) findViewById(R.id.flight_tabs_LinearLayout);

        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.voos_footer);

        if(parseVooFile())
        {
            FooterFragment.setVisibility(true);
            FooterFragment.setFlight(this.flightASeguir);
            FooterFragment.updateFlight(this);
            FooterFragment.setVisibility(true);

            tabs.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,10f)
            );
        }
        else
        {
            FooterFragment.setVisibility(false);

            tabs.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,11f)
            );
        }

        footer.updateVisibility();
    }

    public void buildDeparturesList() throws ParseException {

        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);

        //TODO - Para o uso dos parametros, descomentar o seguinte snippet substituindo 'valor' pelos valores dejados:
        /*
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(2);
        apiParams.add(new BasicNameValuePair(FROM_TIME_PARAM, valor);
        apiParams.add(new BasicNameValuePair(TO_TIME_PARAM, valor);
        */

        bgt = new BGTGetJSONArray(MainActivity.BASE_API_URL+GET_DEPARTURES, "GET", apiParams);

        try {
            JSONArray apJSON = bgt.execute().get();

            Log.w("FLAG", "tamanho apJSON = "+apJSON.length());

            for(int i=0; i < apJSON.length(); i++)
            {
                JSONObject a = apJSON.getJSONObject(i);

                String airlinecode = a.getString(AIRLINECODE);
                String arrivalairportcode = a.getJSONObject(ARRIVALAIRPORT_TAG).getString(CODE);
                String arrivalairportcity = a.getJSONObject(ARRIVALAIRPORT_TAG).getString(CITY);
                String arrivalplannedtime = a.getString(ARRIVALPLANNEDTIME);
                String arrivalrealtime = a.getString(ARRIVALREALTIME);
                String boardingclosetime = a.getString(BOARDINGCLOSETIME);
                String boardingdoorcode = a.getString(BOARDINGDOORCODE);
                long boardingduration = a.getLong(BOARDINGDURATION);
                String boardingopentime = a.getString(BOARDINGOPENTIME);
                String checkincodes = a.getString(CHECKINCODES);
                String checkinclosetime = a.getString(CHECKINCLOSETIME);
                long checkinduration = a.getLong(CHECKINDURATION);
                String checkinopentime = a.getString(CHECKINOPENTIME);
                String departairportcode = a.getJSONObject(DEPARTAIRPORT_TAG).getString(CODE);
                String departairportcity = a.getJSONObject(DEPARTAIRPORT_TAG).getString(CITY);
                String departplannedtime = a.getString(DEPARTPLANNEDTIME);
                String departrealtime = a.getString(DEPARTREALTIME);
                String luggageclosetime = a.getString(LUGGAGECLOSETIME);
                String luggageopentime = a.getString(LUGGAGEOPENTIME);
                String code = a.getString(CODE);
                long luggagepickavgduration = a.getLong(LUGGAGEPICKAVGDURATION);
                String luggageplatforms = a.getString(LUGGAGEPLATFORMS);
                String status = a.getString(STATUS);

                departures.add(new Flight(airlinecode, arrivalairportcode, arrivalairportcity,
                        arrivalplannedtime, arrivalrealtime, boardingclosetime, boardingdoorcode,
                        boardingduration, boardingopentime, checkincodes, checkinclosetime,
                        checkinduration, checkinopentime, code, departairportcode,
                        departairportcity, departplannedtime, departrealtime, luggageclosetime,
                        luggageopentime, luggagepickavgduration, luggageplatforms, status,
                        DEPARTURE_TYPE));
            }

            FlightAdapter departuresAdapter = new FlightAdapter(this, android.R.layout.simple_expandable_list_item_1, departures);
            departuresList = (ListView) findViewById(R.id.listView_partidas);

            departuresList.setAdapter(departuresAdapter);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void buildArrivalsList() throws ParseException {

        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);

        //TODO - Para o uso dos parametros, descomentar o seguinte snippet substituindo 'valor' pelos valores dejados:
        /*
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(2);
        apiParams.add(new BasicNameValuePair(FROM_TIME_PARAM, valor);
        apiParams.add(new BasicNameValuePair(TO_TIME_PARAM, valor);
        */

        bgt = new BGTGetJSONArray(MainActivity.BASE_API_URL+GET_ARRIVALS, "GET", apiParams);

        try {
            JSONArray apJSON = bgt.execute().get();

            Log.w("FLAG", "tamanho apJSON = "+apJSON.length());

            for(int i=0; i < apJSON.length(); i++)
            {
                JSONObject a = apJSON.getJSONObject(i);

                String airlinecode = a.getString(AIRLINECODE);
                String arrivalairportcode = a.getJSONObject(ARRIVALAIRPORT_TAG).getString(CODE);
                String arrivalairportcity = a.getJSONObject(ARRIVALAIRPORT_TAG).getString(CITY);
                String arrivalplannedtime = a.getString(ARRIVALPLANNEDTIME);
                String arrivalrealtime = a.getString(ARRIVALREALTIME);
                String boardingclosetime = a.getString(BOARDINGCLOSETIME);
                String boardingdoorcode = a.getString(BOARDINGDOORCODE);
                long boardingduration = a.getLong(BOARDINGDURATION);
                String boardingopentime = a.getString(BOARDINGOPENTIME);
                String checkincodes = a.getString(CHECKINCODES);
                String checkinclosetime = a.getString(CHECKINCLOSETIME);
                long checkinduration = a.getLong(CHECKINDURATION);
                String checkinopentime = a.getString(CHECKINOPENTIME);
                String departairportcode = a.getJSONObject(DEPARTAIRPORT_TAG).getString(CODE);
                String departairportcity = a.getJSONObject(DEPARTAIRPORT_TAG).getString(CITY);
                String departplannedtime = a.getString(DEPARTPLANNEDTIME);
                String departrealtime = a.getString(DEPARTREALTIME);
                String luggageclosetime = a.getString(LUGGAGECLOSETIME);
                String luggageopentime = a.getString(LUGGAGEOPENTIME);
                String code = a.getString(CODE);
                long luggagepickavgduration = a.getLong(LUGGAGEPICKAVGDURATION);
                String luggageplatforms = a.getString(LUGGAGEPLATFORMS);
                String status = a.getString(STATUS);

                arrivals.add(new Flight(airlinecode, arrivalairportcode, arrivalairportcity,
                        arrivalplannedtime, arrivalrealtime, boardingclosetime, boardingdoorcode,
                        boardingduration, boardingopentime, checkincodes, checkinclosetime,
                        checkinduration, checkinopentime, code, departairportcode,
                        departairportcity, departplannedtime, departrealtime, luggageclosetime,
                        luggageopentime, luggagepickavgduration, luggageplatforms, status,
                        ARRIVAL_TYPE));
            }

            FlightAdapter arrivalsAdapter = new FlightAdapter(this, android.R.layout.simple_expandable_list_item_1, arrivals);
            arrivalsList = (ListView) findViewById(R.id.listView_chegadas);

            arrivalsList.setAdapter(arrivalsAdapter);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*public void buildPartidasList() throws ParseException {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        bgt = new BGTGetJSONArray(PARTIDA_API_URL, "GET", apiParams);

        try {
            JSONArray apJSON = bgt.execute().get();

            Log.w("FLAG", "tamanho apJSON = "+apJSON.length());

            for(int i=0; i < apJSON.length(); i++)
            {
                JSONObject a = apJSON.getJSONObject(i);

                long id = a.getLong(ID);
                long CodigoVoo = a.getLong(CODIGOVOO);
                String CodigoCompanhia = a.getString(CODIGOCOMPANHIA);
                String PartidaCidade = a.getString(PARTIDACIDADE);
                String ChegadaCidade = a.getString(CHEGADACIDADE);
                long PartidaAeroportoId = a.getLong(PARTIDAAEROPORTOID);
                long ChegadaAeroportoId = a.getLong(CHEGADAAEROPORTOID);
                String PartidaTempoEstimadoStr = a.getString(PARTIDATEMPOESTIMADO);
                String ChegadaTempoEstimadoStr = a.getString(CHEGADATEMPOESTIMADO);
                String PartidaTempoRealStr = a.getString(PARTIDATEMPOREAL);
                String ChegadaTempoRealStr = a.getString(CHEGADATEMPOREAL);

                long Terminal = a.getLong(TERMINAL);
                String CheckinInicioStr = a.getString(CHECKININICIO);
                String CheckinFimStr = a.getString(CHECKINFIM);
                long PortaEmbarque = a.getLong(PORTAEMBARQUE);
                String EmbarqueStr = a.getString(EMBARQUE);
                long TapeteBagagem = a.getLong(TAPETEBAGAGEM);
                String BagagemStr = a.getString(BAGAGEM);
                long PortaDesembarque = a.getLong(PORTADESEMBARQUE);
                String DesembarqueStr = a.getString(DESEMBARQUE);

                Date PartidaTempoEstimado = MainActivity.sdf.parse(PartidaTempoEstimadoStr);
                Date ChegadaTempoEstimado = MainActivity.sdf.parse(ChegadaTempoEstimadoStr);
                Date PartidaTempoReal = MainActivity.sdf.parse(PartidaTempoRealStr);
                Date ChegadaTempoReal = MainActivity.sdf.parse(ChegadaTempoRealStr);
                Date CheckinInicio = MainActivity.sdf.parse(CheckinInicioStr);
                Date CheckinFim = MainActivity.sdf.parse(CheckinFimStr);
                Date Embarque = MainActivity.sdf.parse(EmbarqueStr);
                Date Bagagem = MainActivity.sdf.parse(BagagemStr);
                Date Desembarque = MainActivity.sdf.parse(DesembarqueStr);

                partidas.add(new Flight(id,CodigoVoo,CodigoCompanhia,PartidaCidade,
                        ChegadaCidade,PartidaAeroportoId,ChegadaAeroportoId,PartidaTempoEstimado,
                        ChegadaTempoEstimado,PartidaTempoReal,ChegadaTempoReal,
                        Terminal,CheckinInicio,CheckinFim,PortaEmbarque,Embarque,TapeteBagagem,
                        Bagagem,PortaDesembarque,Desembarque,true));

                FlightAdapter partidasAdapter = new FlightAdapter(this, android.R.layout.simple_expandable_list_item_1, partidas);
                partidasList = (ListView) findViewById(R.id.listView_partidas);

                partidasList.setAdapter(partidasAdapter);

                Log.w("FLAG", "partidasList criada!");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    /*public void buildChegadasList() throws ParseException {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        bgt = new BGTGetJSONArray(CHEGADA_API_URL, "GET", apiParams);

        try {
            JSONArray apJSON = bgt.execute().get();

            Log.w("FLAG", "tamanho apJSON = "+apJSON.length());

            for(int i=0; i < apJSON.length(); i++)
            {
                JSONObject a = apJSON.getJSONObject(i);

                long id = a.getLong(ID);
                long CodigoVoo = a.getLong(CODIGOVOO);
                String CodigoCompanhia = a.getString(CODIGOCOMPANHIA);
                String PartidaCidade = a.getString(PARTIDACIDADE);
                String ChegadaCidade = a.getString(CHEGADACIDADE);
                long PartidaAeroportoId = a.getLong(PARTIDAAEROPORTOID);
                long ChegadaAeroportoId = a.getLong(CHEGADAAEROPORTOID);
                String PartidaTempoEstimadoStr = a.getString(PARTIDATEMPOESTIMADO);
                String ChegadaTempoEstimadoStr = a.getString(CHEGADATEMPOESTIMADO);
                String PartidaTempoRealStr = a.getString(PARTIDATEMPOREAL);
                String ChegadaTempoRealStr = a.getString(CHEGADATEMPOREAL);

                long Terminal = a.getLong(TERMINAL);
                String CheckinInicioStr = a.getString(CHECKININICIO);
                String CheckinFimStr = a.getString(CHECKINFIM);
                long PortaEmbarque = a.getLong(PORTAEMBARQUE);
                String EmbarqueStr = a.getString(EMBARQUE);
                long TapeteBagagem = a.getLong(TAPETEBAGAGEM);
                String BagagemStr = a.getString(BAGAGEM);
                long PortaDesembarque = a.getLong(PORTADESEMBARQUE);
                String DesembarqueStr = a.getString(DESEMBARQUE);

                Date PartidaTempoEstimado = MainActivity.sdf.parse(PartidaTempoEstimadoStr);
                Date ChegadaTempoEstimado = MainActivity.sdf.parse(ChegadaTempoEstimadoStr);
                Date PartidaTempoReal = MainActivity.sdf.parse(PartidaTempoRealStr);
                Date ChegadaTempoReal = MainActivity.sdf.parse(ChegadaTempoRealStr);
                Date CheckinInicio = MainActivity.sdf.parse(CheckinInicioStr);
                Date CheckinFim = MainActivity.sdf.parse(CheckinFimStr);
                Date Embarque = MainActivity.sdf.parse(EmbarqueStr);
                Date Bagagem = MainActivity.sdf.parse(BagagemStr);
                Date Desembarque = MainActivity.sdf.parse(DesembarqueStr);

                chegadas.add(new Flight(id,CodigoVoo,CodigoCompanhia,PartidaCidade,
                        ChegadaCidade,PartidaAeroportoId,ChegadaAeroportoId,PartidaTempoEstimado,
                        ChegadaTempoEstimado,PartidaTempoReal,ChegadaTempoReal,
                        Terminal,CheckinInicio,CheckinFim,PortaEmbarque,Embarque,TapeteBagagem,
                        Bagagem,PortaDesembarque,Desembarque,false));

                FlightAdapter chegadasAdapter = new FlightAdapter(this, android.R.layout.simple_expandable_list_item_1, chegadas);
                chegadasList = (ListView) findViewById(R.id.listView_chegadas);

                chegadasList.setAdapter(chegadasAdapter);

                Log.w("FLAG", "chegadasList criada!");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    public boolean parseVooFile()
    {
        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            flightASeguir = FileIO.deserializeFlightObject(MainActivity.FILE_FLIGHT, this).toParcelable();
            return true;
        }

        return false;
    }

    class BGTGetJSONArray extends AsyncTask<String, String, JSONArray> {

        List<NameValuePair> postparams = new ArrayList<NameValuePair>();
        String URL = null;
        String method = null;
        InputStream is = null;
        JSONArray jArray = null;
        String json = "";

        public BGTGetJSONArray(String url, String method, List<NameValuePair> params) {
            this.URL = url;
            this.postparams = params;
            this.method = method;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected JSONArray doInBackground(String... params) {
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
                jArray = new JSONArray(json);

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
            return jArray;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            pd.dismiss();
        }
    }
}
