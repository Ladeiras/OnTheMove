package com.moksie.onthemove.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.MapsListAdapter;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.objects.Map;
import com.moksie.onthemove.objects.Service;
import com.moksie.onthemove.tasks.BGTGetJSONObject;
import com.moksie.onthemove.utilities.FileIO;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServiceActivity extends FragmentActivity {

    private static final String ID = "Id";
    private static final String NOME = "Nome";
    private static final String DESCRICAO = "Descricao";
    private static final String TAG_MAPAS = "Mapas";
    private static final String LOCALIZACAO = "Localizacao";
    private static final String URL = "Url";
    private static String SERVICE_API_URL = "http://onthemove.no-ip.org:3000/api/servico/";//?id=1&nome=restaurante
    private BGTGetJSONObject bgt;

    private String serviceName;
    private Service service;
    private Airport airport;

    private ListView mapsList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent intent = getIntent();
        serviceName = intent.getStringExtra("service");
        airport = intent.getParcelableExtra("airport");

        try {
            builServiceList();
        } catch (ParseException e) {
            Log.e("Error","Parsing maps list");
            e.printStackTrace();
        }

        ListView list = (ListView) findViewById(R.id.service_maps_listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l) {
                Intent intent = new Intent(ServiceActivity.this, MapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                Map map = (Map) mapsList.getAdapter().getItem(i);
                intent.putExtra("title", service.getDescricao());
                intent.putExtra("url", map.getUrl());
                ServiceActivity.this.startActivity(intent);
            }
        });

        updateFragments();
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
                .findFragmentById(R.id.service_footer);

        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempFlight = FileIO.deserializeVooObject(MainActivity.FILE_FLIGHT, this).toParcelable();

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

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void builServiceList() throws ParseException {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(2);
        apiParams.add(new BasicNameValuePair("id",String.valueOf(airport.getId())));
        apiParams.add(new BasicNameValuePair("serv",serviceName));

        bgt = new BGTGetJSONObject(SERVICE_API_URL, "GET", apiParams);

        try {
            JSONObject serviceJSON = bgt.execute().get();
            JSONArray mapsJSON;

            long id = serviceJSON.getLong(ID);
            String name = serviceJSON.getString(NOME);
            String descricao = serviceJSON.getString(DESCRICAO);

            mapsJSON = serviceJSON.getJSONArray(TAG_MAPAS);

            ArrayList<Map> maps = new ArrayList<Map>();

            for(int j=0;j < mapsJSON.length(); j++)
            {
                JSONObject jmap = mapsJSON.getJSONObject(j);
                String location = jmap.getString(LOCALIZACAO);
                String url = jmap.getString(URL);
                maps.add(new Map(location,url));
            }

            service = new Service(id,name,descricao,maps);

            TextView Title = (TextView) this.findViewById(R.id.service_name);
            Title.setText(service.getDescricao());

            MapsListAdapter mapsListAdapter = new MapsListAdapter(this, android.R.layout.simple_expandable_list_item_1, maps);
            mapsList = (ListView) findViewById(R.id.service_maps_listView);

            mapsList.setAdapter(mapsListAdapter);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
