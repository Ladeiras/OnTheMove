package com.moksie.onthemove.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.VooAdapter;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.objects.Aeroporto;
import com.moksie.onthemove.objects.Voo;
import com.moksie.onthemove.tasks.BackGroundTask2;
import com.moksie.onthemove.utilities.FileIO;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class VoosActivity extends FragmentActivity {

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
    //private static final String VOO_API_URL = "http://onthemove.no-ip.org:3000/api/aeroportos";
    //private static String FILE_VOO = "voo";

    String PARTIDA_API_URL;
    String CHEGADA_API_URL;

    private Aeroporto aeroporto;
    private boolean movemeFlag = false;
    private TabHost mTabHost;
    private BackGroundTask2 bgt;
    private ListView partidasList;
    private ListView chegadasList;
    private Voo vooASeguir;

    ArrayList<Voo> partidas = new ArrayList<Voo>();
    ArrayList<Voo> chegadas = new ArrayList<Voo>();

    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        loadingDialog = ProgressDialog.show(VoosActivity.this, "",
                "Loading. Please wait...", true);

        setContentView(R.layout.activity_voos);

        Intent intent = getIntent();
        aeroporto = (Aeroporto) intent.getParcelableExtra("aeroporto");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        PARTIDA_API_URL = "http://onthemove.no-ip.org:3000/api/partidas/"+aeroporto.getId();
        CHEGADA_API_URL = "http://onthemove.no-ip.org:3000/api/chegadas/"+aeroporto.getId();

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
            titlePartidas.setText("PARTIDAS");
            tabPartidas.setIndicator(tabPartidasIndicator);

            View tabChegadasIndicator = LayoutInflater.from(this).inflate(R.layout.onthemovetheme_tab_indicator_holo, mTabHost.getTabWidget(), false);
            TextView titleChegadas = (TextView) tabChegadasIndicator.findViewById(android.R.id.title);
            titleChegadas.setText("CHEGADAS");
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
                    mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_active);
                    mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_active);
                    mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundResource(R.drawable.tab_inactive);
                }
            });

           //Tabs refresh
            mTabHost.setCurrentTab(1);
            mTabHost.setCurrentTab(0);

            partidasList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int i, long l) {
                    Intent intent = new Intent(VoosActivity.this, VooInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Voo voo = (Voo) partidasList.getAdapter().getItem(i);
                    Log.w("FLAG", voo.getPartidacidade());
                    intent.putExtra("voo", voo);
                    VoosActivity.this.startActivity(intent);
                }
            });

            chegadasList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int i, long l) {
                    Intent intent = new Intent(VoosActivity.this, VooInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Voo voo = (Voo) chegadasList.getAdapter().getItem(i);
                    Log.w("FLAG", voo.getPartidacidade());
                    intent.putExtra("voo", voo);
                    VoosActivity.this.startActivity(intent);
                }
            });
        }


        updateFragments();
        loadingDialog.hide();
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
            FooterFragment.setVoo(this.vooASeguir);
            FooterFragment.updateVoo(this);
            FooterFragment.setVisibility(true);
        }
        else FooterFragment.setVisibility(false);

        footer.updateVisibility();
    }

    public void buildPartidasList() throws ParseException {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        bgt = new BackGroundTask2(PARTIDA_API_URL, "GET", apiParams);

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

                partidas.add(new Voo(id,CodigoVoo,CodigoCompanhia,PartidaCidade,
                        ChegadaCidade,PartidaAeroportoId,ChegadaAeroportoId,PartidaTempoEstimado,
                        ChegadaTempoEstimado,PartidaTempoReal,ChegadaTempoReal,true));

                VooAdapter partidasAdapter = new VooAdapter(this, android.R.layout.simple_expandable_list_item_1, partidas);
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
        bgt = new BackGroundTask2(CHEGADA_API_URL, "GET", apiParams);

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

                chegadas.add(new Voo(id,CodigoVoo,CodigoCompanhia,PartidaCidade,
                        ChegadaCidade,PartidaAeroportoId,ChegadaAeroportoId,PartidaTempoEstimado,
                        ChegadaTempoEstimado,PartidaTempoReal,ChegadaTempoReal,false));

                VooAdapter chegadasAdapter = new VooAdapter(this, android.R.layout.simple_expandable_list_item_1, chegadas);
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
        if(FileIO.fileExists(MainActivity.FILE_VOO, this))
        {
            //Ler voo do ficheiro
            //String temp = FileIO.readFromFile(MainActivity.FILE_VOO, this);
            vooASeguir = FileIO.deserializeVooObject(MainActivity.FILE_VOO, this).toParcelable();
            return true;
        }

        return false;
    }
}
