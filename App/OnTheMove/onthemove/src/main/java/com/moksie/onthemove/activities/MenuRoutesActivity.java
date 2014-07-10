package com.moksie.onthemove.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Nesta classe são mostradas as opções do calculo de rota.
 * São mostrados dois botões, Transportes Colectivos e Taxis, sendo que o primeiro faz ligação a uma
 * Activity onde se faz a ligação à aplicação Move-me tendo como parametros predefinidos a
 * localização do aeroporto e o segundo faz ligação à ContactActivity, onde são mostrados os
 * contactos dos táxis obtidos a partir do segundo servidor (servidor de contactos).
 * Os dados do aeroporto são passados como parametro para esta Activity (objecto Airport).
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class MenuRoutesActivity extends FragmentActivity {

    private static final String CONTACT_TAXI = "Taxi";

    //Variáveis usadas no pedido do contacto Taxi ao servidor
    private static final String CODE = "Code";
    private static final String EMAIL = "Email";
    private static final String FACEBOOK = "Facebook";
    private static final String TELEF = "Telef";
    private static final String TWITTER = "Twitter";
    private static final String WEBSITE = "Website";
    private static final String LOGOURL = "LogoUrl";

    private static final String PARAM_AIRPORT_CODE = "id";
    private static final String PARAM_TYPE = "type";

    private static final String GET_CONTACT = "contact/";

    private BGTGetJSONArray bgt;

    private Airport airport;
    private Contact taxiContact;
    private boolean moksieFlag = true;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_menu);

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("airport");

        pd = new ProgressDialog(this);
        pd.setMessage("A carregar Contactos");
        getContacts(CONTACT_TAXI);

        //Botao Transportes Colectivos
        final Button moveMeButton = (Button) findViewById(R.id.moveme_button);
        moveMeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuRoutesActivity.this, MenuMoveMeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("airport", airport);
                MenuRoutesActivity.this.startActivity(intent);
            }
        });

        //Botao taxis
        final Button taxisButton = (Button) findViewById(R.id.taxis_button);
        if(moksieFlag) {
            taxisButton.setVisibility(View.VISIBLE);
            taxisButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(MenuRoutesActivity.this, ContactActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("contact", taxiContact);
                    MenuRoutesActivity.this.startActivity(intent);
                }
            });
        }
        else {
            taxisButton.setVisibility(View.INVISIBLE);
        }

        //Fragments
        updateFragments();
    }

    public void getContacts(String type)
    {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(2);
        apiParams.add(new BasicNameValuePair(PARAM_AIRPORT_CODE, airport.getCode()));
        apiParams.add(new BasicNameValuePair(PARAM_TYPE, type));
        bgt = new BGTGetJSONArray(MainActivity.BASE_API2_URL+GET_CONTACT, "GET", apiParams);

        try {
            JSONArray apJSON = bgt.execute().get();
            if(null != apJSON) {
                for (int i = 0; i < apJSON.length(); i++) {
                    JSONObject a = apJSON.getJSONObject(i);

                    String code = a.getString(CODE);
                    String email = a.getString(EMAIL);
                    String facebook = a.getString(FACEBOOK);
                    String telef = a.getString(TELEF);
                    String twitter = a.getString(TWITTER);
                    String website = a.getString(WEBSITE);
                    String logourl = a.getString(LOGOURL);

                    taxiContact = new Contact(code, email, facebook, telef, twitter, website, logourl);
                }
            }
            else
            {
                moksieFlag = false;
                Toast.makeText(this, "Problemas de ligação ao servidor moksie",
                        Toast.LENGTH_LONG).show();
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

        //Botao ajuda
        HeaderFragment.setMsg("Neste ecrã poderá escolher uma das opções listadas para calcular a sua rota.");

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
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.move_me_footer);

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

    /**
     * Esta classe tem como objectivo a criação de uma tarefa em background para fazer pedidos ao
     * servidor sem bloquear a UI.
     * Os pedidos poderão ser GET ou POST, sendo que o GET devolve um JSONArray
     */
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
