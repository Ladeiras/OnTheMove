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
import android.widget.Toast;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.LocationListAdapter;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.objects.Location;
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

public class LocationListActivity extends FragmentActivity {

    private static final String OPTION_LOCATIONS = "locations";

    private static final String CODE = "Code";
    private static final String KEYWORDS = "Keywords";
    private static final String LANG = "Lang";
    private static final String NAME = "Name";
    private static final String POSX = "PosX";
    private static final String POSY = "PosY";
    private static final String TYPE_TAG = "Type";
    private static final String DESC = "Desc";

    //private static final String GET_LOCATIONS_BY_TYPE = "getLocationsByType";
    private static final String GET_LOCATIONS_BY_PLANT = "getLocationsByPlant";
    //private static final String PARAM_LOCATION_TYPES = "locationTypes";
    private static final String PARAM_PLANT_CODE = "plantCode";
    private static final String PARAM_LANG = "lang";
    private static final String PT = "PT";

    BGTGetJSONArray bgt;

    private ArrayList<Location> locations = new ArrayList<Location>();
    private String service;
    private ListView locationsList;
    private Plant plant;
    private Airport airport;
    private String title;

    private ProgressDialog pd;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("airport");
        service = intent.getStringExtra("service");
        plant = (Plant) intent.getParcelableExtra("plant");

        pd = new ProgressDialog(this);
        pd.setMessage("A carregar Locais");
        getLocations();

        fiterLocations();

        if(locations.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Sem resultados",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        LocationListAdapter plantsListAdapter = new LocationListAdapter(this, android.R.layout.simple_expandable_list_item_1, locations);
        locationsList = (ListView) findViewById(R.id.locations_listView);

        locationsList.setAdapter(plantsListAdapter);

        TextView titleTV = (TextView) findViewById(R.id.locations_name);
        titleTV.setText(service);

        locationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l) {
                Intent intent = new Intent(LocationListActivity.this, PlantActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                Location location = (Location) locationsList.getAdapter().getItem(i);
                intent.putExtra("option", OPTION_LOCATIONS);
                intent.putExtra("location", location);
                intent.putExtra("title", plant.getName()+" - "+location.getName());
                intent.putExtra("url", plant.getImageurl());
                LocationListActivity.this.startActivity(intent);
            }
        });

        updateFragments();
    }

    private void fiterLocations()
    {
        ArrayList<Location> temp = new ArrayList<Location>();

        for(int i=0; i<locations.size();i++)
        {
            if(locations.get(i).getType().equals(service))
                temp.add(locations.get(i));
        }

        locations = temp;
    }

    public void getLocations()
    {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(2);
        apiParams.add(new BasicNameValuePair(PARAM_PLANT_CODE, plant.getCode()));
        apiParams.add(new BasicNameValuePair(PARAM_LANG, PT));
        bgt = new BGTGetJSONArray(MainActivity.BASE_API_URL+GET_LOCATIONS_BY_PLANT, "GET", apiParams);

        try {
            JSONArray apJSON = bgt.execute().get();

            for(int i=0; i < apJSON.length(); i++)
            {
                JSONObject a = apJSON.getJSONObject(i);

                String code = a.getString(CODE);
                String keywords = a.getString(KEYWORDS);
                String lang = a.getString(LANG);
                String name = a.getString(NAME);
                long posx = a.getLong(POSX);
                long posy = a.getLong(POSY);
                String type = a.getJSONObject(TYPE_TAG).getString(DESC);

                locations.add(new Location(code, keywords, lang, name, posx, posy, type));
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
