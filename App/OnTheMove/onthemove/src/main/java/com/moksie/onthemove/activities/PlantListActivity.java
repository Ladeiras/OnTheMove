package com.moksie.onthemove.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.PlantListAdapter;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.objects.Plant;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PlantListActivity extends FragmentActivity {

    private static final String OPTION_LOCATIONS = "locations";
    private static final String OPTION_ALL = "all";

    private static final String CODE = "Code";
    private static final String IMAGETYPE = "ImageType";
    private static final String IMAGEURL = "ImageUrl";
    private static final String LANG = "Lang";
    private static final String LENGTH = "Length";
    private static final String NAME = "Name";
    private static final String OBS = "Obs";
    private static final String POSX = "PosX";
    private static final String POSY = "PosY";
    private static final String POSZ = "PosZ";
    private static final String VERSION = "Version";
    private static final String WIDTH = "Width";

    private static final String GET_PLANTS = "getPlants";
    private static final String PARAM_LANG = "lang";
    private static final String PT = "PT";
    private BGTGetJSONArray bgt;

    private ArrayList<Plant> plants = new ArrayList<Plant>();

    private String title;
    private ListView plantsList;
    private ProgressDialog pd;
    private String service;
    private Airport airport;
    private String option;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);

        Intent intent = getIntent();
        airport = intent.getParcelableExtra("airport");
        service = intent.getStringExtra("service");
        option = intent.getStringExtra("option");

        title = "Plantas - "+service;

        pd = new ProgressDialog(this);
        pd.setMessage("A carregar Plantas");
        getPlants();

        PlantListAdapter plantListAdapter = new PlantListAdapter(this, android.R.layout.simple_expandable_list_item_1, plants);
        plantsList = (ListView) findViewById(R.id.service_maps_listView);

        plantsList.setAdapter(plantListAdapter);

        TextView titleTV = (TextView) findViewById(R.id.service_name);
        titleTV.setText(title);

        ListView list = (ListView) findViewById(R.id.service_maps_listView);

        if(option.equals(OPTION_LOCATIONS)) {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int i, long l) {
                    Intent intent = new Intent(PlantListActivity.this, LocationListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Plant plant = (Plant) plantsList.getAdapter().getItem(i);
                    intent.putExtra("service", service);
                    intent.putExtra("plant", plant);
                    PlantListActivity.this.startActivity(intent);
                }
            });
        }
        else if(option.equals(OPTION_ALL))
        {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int i, long l) {
                    Intent intent = new Intent(PlantListActivity.this, PlantActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Plant plant = (Plant) plantsList.getAdapter().getItem(i);
                    intent.putExtra("option", OPTION_ALL);
                    intent.putExtra("title", plant.getName());
                    intent.putExtra("url", plant.getImageurl());
                    PlantListActivity.this.startActivity(intent);
                }
            });
        }

        updateFragments();
    }

    public void getPlants()
    {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        apiParams.add(new BasicNameValuePair(PARAM_LANG, PT));
        bgt = new BGTGetJSONArray(MainActivity.BASE_API_URL+GET_PLANTS, "GET", apiParams);

        try {
            JSONArray apJSON = bgt.execute().get();

            for(int i=0; i < apJSON.length(); i++)
            {
                JSONObject a = apJSON.getJSONObject(i);

                String code = a.getString(CODE);
                String imagetype = a.getString(IMAGETYPE);
                String imageurl = a.getString(IMAGEURL);
                String lang = a.getString(LANG);
                long length = a.getLong(LENGTH);
                String name = a.getString(NAME);
                String obs = a.getString(OBS);
                long posx = a.getLong(POSX);
                long posy = a.getLong(POSY);
                long posz = a.getLong(POSZ);
                String version = a.getString(VERSION);
                long width = a.getLong(WIDTH);

                plants.add(new Plant(code, imagetype, imageurl, lang, length, name, obs, posx, posy,
                        posz, version, width));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        HeaderFragment.setMsg("Neste ecrã pode escolher uma das plantas onde este serviço é disponibilizado");

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
                .findFragmentById(R.id.maps_footer);

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
