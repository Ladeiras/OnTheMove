package com.moksie.onthemove.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Contact;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MenuContactsActivity extends FragmentActivity {

    private static final String LOST_AND_FOUND_TYPE = "LostAndFound";
    private static final String RESCUE_STATION_TYPE = "RescueStation";

    private static final String CODE = "Code";
    private static final String EMAIL = "Email";
    private static final String FACEBOOK = "Facebook";
    private static final String TELEF = "Telef";
    private static final String TWITTER = "Twitter";
    private static final String WEBSITE = "Website";
    private static final String LOGOURL = "LogoUrl";

    private static final String PARAM_AIRPORT_CODE = "id";
    private static final String PARAM_TYPE = "type";

    private static final String API2_URL = "http://onthemove.no-ip.org:3000/api/contact/";

    private Airport airport;
    private Contact lostAndFound;
    private Contact rescueStation;
    private ProgressDialog pd;

    private BGTGetJSONArray bgt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_menu);

        pd = new ProgressDialog(this);
        pd.setMessage("A carregar Contactos");

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("airport");
        try {
            lostAndFound = getContact(LOST_AND_FOUND_TYPE);
            rescueStation = getContact(RESCUE_STATION_TYPE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Botao Contactos Aeroporto
        final Button CAButton = (Button) findViewById(R.id.contacts_aeroporto);
        CAButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuContactsActivity.this, ContactActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("contact", airport.getContact());
                MenuContactsActivity.this.startActivity(intent);
            }
        });


        //Botao Perdidos e Achados
        final Button PAButton = (Button) findViewById(R.id.contacts_perdidos_achados);
        PAButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuContactsActivity.this, ContactActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                lostAndFound.setName("Perdidos e Achados");
                intent.putExtra("contact", lostAndFound);
                MenuContactsActivity.this.startActivity(intent);
            }
        });

        //Botao Posto Socorro
        final Button PSButton = (Button) findViewById(R.id.contacts_posto_socorro);
        PSButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuContactsActivity.this, ContactActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                rescueStation.setName("Posto de Socorro");
                intent.putExtra("contact", rescueStation);
                MenuContactsActivity.this.startActivity(intent);
            }
        });


        updateFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Botao ajuda
        HeaderFragment.setMsg("Neste ecrã poderá escolher uma opção das listadas, para consultar contactos úteis.");

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
                .findFragmentById(R.id.contacts_footer);

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

    public Contact getContact(String contactType) throws ParseException {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(2);
        apiParams.add(new BasicNameValuePair(PARAM_AIRPORT_CODE, airport.getCode()));
        apiParams.add(new BasicNameValuePair(PARAM_TYPE, contactType));
        bgt = new BGTGetJSONArray(API2_URL, "GET", apiParams);

        try {
            JSONArray apJSON = bgt.execute().get();

            for(int i=0; i < apJSON.length(); i++)
            {
                JSONObject a = apJSON.getJSONObject(i);

                String code = a.getString(CODE);
                String email = a.getString(EMAIL);
                String facebook = a.getString(FACEBOOK);
                String telef = a.getString(TELEF);
                String twitter = a.getString(TWITTER);
                String website = a.getString(WEBSITE);
                String logourl = a.getString(LOGOURL);

                return new Contact(code, email, facebook, telef, twitter, website, logourl);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
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