package com.moksie.onthemove.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.ContactsAdapter;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Contact;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.tasks.BGTGetJSONObject;
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

public class ContactActivity extends FragmentActivity {

    private static final String ID = "Id";
    private static final String IDAEROPORTO = "IdAeroporto";
    private static final String TITULO = "Titulo";
    private static final String WEBSITE = "Website";
    private static final String MAPAURL = "MapaURL";
    private static final String DESCRICAO = "Descricao";
    private static final String TAG_TELEFONES = "Telefones";
    private static String CONTACTO_API_URL = "http://onthemove.no-ip.org:3000/api/contacto/";
    private Airport airport;
    private String contactName;
    private BGTGetJSONObject bgt;

    private Contact contact;
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
        setContentView(R.layout.activity_contact);

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("airport");
        contactName = intent.getStringExtra("contact");

        pd = new ProgressDialog(this);
        pd.setMessage("A carregar "+contactName);

        try {
            buildContactList();
        } catch (ParseException e)
        {
            Log.e("Contact","Error building list");
            e.printStackTrace();
        }

        LinearLayout mapaLL = (LinearLayout) findViewById(R.id.taxis_ver_mapa_layout);
        mapaLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ImageView mapIcon = (ImageView) findViewById(R.id.mapa_icon_imageView);
                mapIcon.setImageResource(R.drawable.ic_icon_mapa_active);

                Intent intent = new Intent(ContactActivity.this, MapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("url", contact.getMapaurl());
                intent.putExtra("title", contact.getTitulo());
                ContactActivity.this.startActivity(intent);
            }
        });

        TextView description = (TextView) findViewById(R.id.contact_description);
        description.setText(contact.getDescricao());

        restartButtons();
        updateFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();
        restartButtons();
        updateFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartButtons();
        updateFragments();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        restartButtons();
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
        LinearLayout layout = (LinearLayout) findViewById(R.id.contact_LinearLayout);
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.move_me_footer);

        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempFlight = FileIO.deserializeVooObject(MainActivity.FILE_FLIGHT, this).toParcelable();

            FooterFragment.setVisibility(true);
            FooterFragment.setFlight(tempFlight);
            FooterFragment.updateFlight(this);
            FooterFragment.setVisibility(true);

            layout.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,9f)
            );
        }
        else
        {
            FooterFragment.setVisibility(false);
            layout.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,10f)
            );
        }

        footer.updateVisibility();
    }

    private void restartButtons() {
        ImageView mapIcon = (ImageView) findViewById(R.id.mapa_icon_imageView);
        mapIcon.setImageResource(R.drawable.ic_icon_mapa_inative);
        //TODO adicionar os restantes botoes
    }

    public void buildContactList() throws ParseException {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(2);
        apiParams.add(new BasicNameValuePair("id",String.valueOf(airport.getId())));
        apiParams.add(new BasicNameValuePair("contact",contactName));

        bgt = new BGTGetJSONObject(CONTACTO_API_URL, "GET", apiParams);

        try {
            JSONObject contactJSON = bgt.execute().get();
            JSONArray phonesJSON;

            long id = contactJSON.getLong(ID);
            long idAeroporto = contactJSON.getLong(IDAEROPORTO);
            String Titulo = contactJSON.getString(TITULO);
            String WebSite = contactJSON.getString(WEBSITE);
            String MapaURL = contactJSON.getString(MAPAURL);
            String Descricao = contactJSON.getString(DESCRICAO);

            phonesJSON = contactJSON.getJSONArray(TAG_TELEFONES);

            ArrayList<Long> contactos = new ArrayList<Long>();

            for(int j=0;j < phonesJSON.length(); j++)
            {
                contactos.add(phonesJSON.getLong(j));
            }

            contact = new Contact(id,idAeroporto,Titulo,WebSite,MapaURL,Descricao,contactos);

            TextView WebSiteTV = (TextView) this.findViewById(R.id.taxi_website_textView);
            WebSiteTV.setText(contact.getWebsite());

            ContactsAdapter contactsAdapter = new ContactsAdapter(this, android.R.layout.simple_expandable_list_item_1, contactos);
            ListView contactosList = (ListView) findViewById(R.id.listView_contactos);

            contactosList.setAdapter(contactsAdapter);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
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

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            pd.dismiss();
        }
    }
}
