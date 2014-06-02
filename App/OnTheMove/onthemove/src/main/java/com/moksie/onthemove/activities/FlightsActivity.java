package com.moksie.onthemove.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.FlightAdapter;
import com.moksie.onthemove.adapters.SearchAdapter;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.tasks.BGTGetJSONArray;
import com.moksie.onthemove.utilities.FileIO;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FlightsActivity extends FragmentActivity {

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

    String PARTIDA_API_URL;
    String CHEGADA_API_URL;

    private Airport airport;
    private boolean movemeFlag = false;
    private TabHost mTabHost;
    private BGTGetJSONArray bgt;
    private ListView partidasList;
    private ListView chegadasList;
    private Flight flightASeguir;

    protected boolean inhibit_spinner = true;

    ArrayList<Flight> partidas = new ArrayList<Flight>();
    ArrayList<Flight> chegadas = new ArrayList<Flight>();
    final ArrayList<String> options = new ArrayList(Arrays.asList(OPTION_NUMERO_VOO_VALUE,
            OPTION_ORIGEM_DESTINO_VALUE_DESTINO, OPTION_COMPANHIA_VALUE, OPTION_SHOW_ALL_VALUE, OPTION_DEFAULT_VALUE));
    boolean startSpinner = true;

    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        loadingDialog = ProgressDialog.show(FlightsActivity.this, "",
                "Loading. Please wait...", true);

        setContentView(R.layout.activity_flights);

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("aeroporto");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        PARTIDA_API_URL = "http://onthemove.no-ip.org:3000/api/partidas/"+ airport.getId();
        CHEGADA_API_URL = "http://onthemove.no-ip.org:3000/api/chegadas/"+ airport.getId();

        //Listas Setup
        //partidasList = (ListView) findViewById(R.id.listView_partidas);
        //chegadasList = (ListView) findViewById(R.id.listView_chegadas);

        //TODO: Popular as listas
        //List<String> tempList1 = new ArrayList<String>();
        //List<String> tempList2 = new ArrayList<String>();
        //tempList1.add("Partida 1");
        //tempList1.add("Partida 2");
        //tempList2.add("Chegada 1");
        //tempList2.add("Chegada 2");
        //partidasList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, tempList1));
        //chegadasList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, tempList2));

        //Listas de Voos
        if(partidas.isEmpty() && chegadas.isEmpty())
        {
            try {
                buildPartidasList();
                buildChegadasList();
            } catch (ParseException e) {
                Log.e("FLAG", "Erro na criacao das listas!");
                e.printStackTrace();
            }

            //Exemplo listner
            /*partidasList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View view, int position, long id)
                {
                    String item = (String) partidasList.getAdapter().getItem(position);
                    if(item != null)
                    {
                        //Do stuff with item
                        setFooterVisibility(true);
                        updateFooter();
                    }
                }
            });*/

            mTabHost = (TabHost) findViewById(R.id.tabHost);
            mTabHost.setup();

            //Tabs
            TabHost.TabSpec tabPartidas = mTabHost.newTabSpec("Partidas");
            TabHost.TabSpec tabChegadas = mTabHost.newTabSpec("Chegadas");

            //Tab Indicators
            View tabPartidasIndicator = LayoutInflater.from(this).inflate(R.layout.onthemovetheme_tab_indicator_holo, mTabHost.getTabWidget(), false);
            TextView titlePartidas = (TextView) tabPartidasIndicator.findViewById(android.R.id.title);
            titlePartidas.setText("Partidas");
            tabPartidas.setIndicator(tabPartidasIndicator);

            View tabChegadasIndicator = LayoutInflater.from(this).inflate(R.layout.onthemovetheme_tab_indicator_holo, mTabHost.getTabWidget(), false);
            TextView titleChegadas = (TextView) tabChegadasIndicator.findViewById(android.R.id.title);
            titleChegadas.setText("Chegadas");
            tabChegadas.setIndicator(tabChegadasIndicator);

            //Tabs Views
            tabPartidas.setContent(new TabHost.TabContentFactory() {

                public View createTabContent(String tag) {
                    return partidasList;
                }
            });

            tabChegadas.setContent(new TabHost.TabContentFactory() {

                public View createTabContent(String tag) {
                    return chegadasList;
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

            partidasList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int i, long l) {
                    Intent intent = new Intent(FlightsActivity.this, FlightInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Flight flight = (Flight) partidasList.getAdapter().getItem(i);
                    Log.w("FLAG", flight.getPartidacidade());
                    intent.putExtra("voo", flight);
                    FlightsActivity.this.startActivity(intent);
                }
            });

            chegadasList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int i, long l) {
                    Intent intent = new Intent(FlightsActivity.this, FlightInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Flight flight = (Flight) chegadasList.getAdapter().getItem(i);
                    Log.w("FLAG", flight.getPartidacidade());
                    intent.putExtra("voo", flight);
                    FlightsActivity.this.startActivity(intent);
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
                        updatePartidas(partidas);
                    if(mTabHost.getCurrentTab() == TAB_CHEGADAS)
                        updateChegadas(chegadas);
                }
                else if(option != OPTION_DEFAULT) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(FlightsActivity.this);

                    alert.setTitle("Pesquisar por " + options.get(option));
                    //alert.setMessage(options.get(i));

                    // Set an EditText view to get user input
                    final EditText input = new EditText(FlightsActivity.this);
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
        loadingDialog.dismiss();
    }

    private void filterFlights(String value, int currentTab, int option)
    {
        ArrayList<Flight> temp = new ArrayList<Flight>();

        if(currentTab==TAB_PARTIDAS)
        {
            if(option==OPTION_NUMERO_VOO)
            {
                for(int i=0;i<partidas.size();i++)
                    if(String.valueOf(partidas.get(i).getCodigovoo()).contains(value) ||
                            value.contains(String.valueOf(partidas.get(i))))
                        temp.add(partidas.get(i));
            }
            else if(option==OPTION_COMPANHIA)
            {
                for(int i=0;i<partidas.size();i++)
                    if(partidas.get(i).getCodigocompanhia().toLowerCase().contains(value.toLowerCase()) ||
                            value.toLowerCase().contains(partidas.get(i).getCodigocompanhia().toLowerCase()))
                        temp.add(partidas.get(i));
            }
            else if(option==OPTION_ORIGEM_DESTINO)
            {
                for(int i=0;i<partidas.size();i++)
                    if(partidas.get(i).getChegadacidade().toLowerCase().contains(value.toLowerCase()) ||
                            value.toLowerCase().contains(partidas.get(i).getChegadacidade().toLowerCase()))
                        temp.add(partidas.get(i));
            }

            if(!temp.isEmpty()) {
                updatePartidas(temp);
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
                for(int i=0;i<chegadas.size();i++)
                    if(String.valueOf(chegadas.get(i).getCodigovoo()).contains(value) ||
                            value.contains(String.valueOf(chegadas.get(i))))
                        temp.add(chegadas.get(i));
            }
            else if(option==OPTION_COMPANHIA)
            {
                for(int i=0;i<chegadas.size();i++)
                    if(chegadas.get(i).getCodigocompanhia().toLowerCase().contains(value.toLowerCase()) ||
                            value.toLowerCase().contains(chegadas.get(i).getCodigocompanhia().toLowerCase()))
                        temp.add(chegadas.get(i));
            }
            else if(option==OPTION_ORIGEM_DESTINO)
            {
                for(int i=0;i<chegadas.size();i++)
                    if(chegadas.get(i).getPartidacidade().toLowerCase().contains(value.toLowerCase()) ||
                            value.toLowerCase().contains(chegadas.get(i).getPartidacidade().toLowerCase()))
                        temp.add(chegadas.get(i));
            }

            if(!temp.isEmpty()) {
                updateChegadas(temp);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Não foram encontrados resultados",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updatePartidas(ArrayList<Flight> flights) {
        FlightAdapter partidasAdapter = new FlightAdapter(this, android.R.layout.simple_expandable_list_item_1, flights);
        partidasList = (ListView) findViewById(R.id.listView_partidas);
        partidasList.setAdapter(partidasAdapter);
    }

    private void updateChegadas(ArrayList<Flight> flights) {
        FlightAdapter chegadasAdapter = new FlightAdapter(this, android.R.layout.simple_expandable_list_item_1, flights);
        chegadasList = (ListView) findViewById(R.id.listView_chegadas);
        chegadasList.setAdapter(chegadasAdapter);
    }

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
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.voos_footer);

        if(parseVooFile())
        {
            FooterFragment.setVisibility(true);
            FooterFragment.setFlight(this.flightASeguir);
            FooterFragment.updateFlight(this);
            FooterFragment.setVisibility(true);
        }
        else FooterFragment.setVisibility(false);

        footer.updateVisibility();
    }

    public void buildPartidasList() throws ParseException {
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

                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                Date PartidaTempoEstimado = MainActivity.sdf.parse(PartidaTempoEstimadoStr);
                Date ChegadaTempoEstimado = MainActivity.sdf.parse(ChegadaTempoEstimadoStr);
                Date PartidaTempoReal = MainActivity.sdf.parse(PartidaTempoRealStr);
                Date ChegadaTempoReal = MainActivity.sdf.parse(ChegadaTempoRealStr);

                partidas.add(new Flight(id,CodigoVoo,CodigoCompanhia,PartidaCidade,
                        ChegadaCidade,PartidaAeroportoId,ChegadaAeroportoId,PartidaTempoEstimado,
                        ChegadaTempoEstimado,PartidaTempoReal,ChegadaTempoReal,true));

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
    }

    public void buildChegadasList() throws ParseException {
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

                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                Date PartidaTempoEstimado = MainActivity.sdf.parse(PartidaTempoEstimadoStr);
                Date ChegadaTempoEstimado = MainActivity.sdf.parse(ChegadaTempoEstimadoStr);
                Date PartidaTempoReal = MainActivity.sdf.parse(PartidaTempoRealStr);
                Date ChegadaTempoReal = MainActivity.sdf.parse(ChegadaTempoRealStr);

                chegadas.add(new Flight(id,CodigoVoo,CodigoCompanhia,PartidaCidade,
                        ChegadaCidade,PartidaAeroportoId,ChegadaAeroportoId,PartidaTempoEstimado,
                        ChegadaTempoEstimado,PartidaTempoReal,ChegadaTempoReal,false));

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
    }

    public boolean parseVooFile()
    {
        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            flightASeguir = FileIO.deserializeVooObject(MainActivity.FILE_FLIGHT, this).toParcelable();
            return true;
        }

        return false;
    }
}
