package com.moksie.onthemove.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.ContactsAdapter;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServiceActivity extends FragmentActivity {

    private static final String ID = "Id";
    private static final String NOME = "Nome";
    private static final String TITULO = "Titulo";
    private static final String WEBSITE = "Website";
    private static final String WEBMAIL = "Webmail";
    private static final String DESCRICAO = "Descricao";

    private static final String TAG_MAPAS = "Mapas";
    private static final String LOCALIZACAO = "Localizacao";
    private static final String URL = "Url";

    private static final String TAG_TELEFONES = "Telefones";

    private static String SERVICE_API_URL = "http://onthemove.no-ip.org:3000/api/servico/";
    private BGTGetJSONObject bgt;

    private String serviceName;
    private Service service;
    private Airport airport;

    private ListView phonesList;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent intent = getIntent();
        serviceName = intent.getStringExtra("service");
        airport = intent.getParcelableExtra("airport");

        pd = new ProgressDialog(this);
        pd.setMessage("A carregar "+serviceName);

        try {
            builServiceList();
        } catch (ParseException e) {
            Log.e("Error","Parsing maps list");
            e.printStackTrace();
        }

        if(service != null) {
            LinearLayout mapLL = (LinearLayout) findViewById(R.id.ver_mapa_layout);
            mapLL.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ServiceActivity.this, MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putParcelableArrayListExtra("maps", (ArrayList<Map>) service.getMaps());
                    intent.putExtra("title", service.getTitulo());
                    ServiceActivity.this.startActivity(intent);
                }
            });

            LinearLayout websiteLL = (LinearLayout) findViewById(R.id.website_LinearLayout);
            if(!service.getWebsite().equals("")) {
                websiteLL.setVisibility(View.VISIBLE);
                websiteLL.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String url = service.getWebsite();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
            }
            else websiteLL.setVisibility(View.GONE);

            LinearLayout webmailLL = (LinearLayout) findViewById(R.id.webmail_LinearLayout);
            if(!service.getWebmail().equals("")) {
                webmailLL.setVisibility(View.VISIBLE);
                webmailLL.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{service.getWebmail()});
                        //i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                        //i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                        try {
                            startActivity(Intent.createChooser(i, "Enviar mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(ServiceActivity.this, "Não existem clientes de email instalados", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else webmailLL.setVisibility(View.GONE);

            phonesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int i, long l) {
                    Long nr = (Long) phonesList.getAdapter().getItem(i);
                    String number = "00"+nr;
                    String uri = "tel:" + number.trim() ;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            });
        }
        else
        {
            Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        updateFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(service != null) {
            //Botao ajuda
            if (service.getNome().equals("taxis")) {
                HeaderFragment.setMsg("Neste ecrã poderá consultar as informações para poder chamar um táxi. Ou então saber onde se encontram os táxis no aeroporto.");
            } else
                HeaderFragment.setMsg("Neste ecrã poderá consultar as informações sobre este serviço no Aeroporto");
        }

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
        LinearLayout layout = (LinearLayout) findViewById(R.id.service_LinearLayout);
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
            JSONArray phonesJSON;

            if(serviceJSON != null) {
                long id = serviceJSON.getLong(ID);
                String name = serviceJSON.getString(NOME);
                String titulo = serviceJSON.getString(TITULO);
                String website = serviceJSON.getString(WEBSITE);
                String webmail = serviceJSON.getString(WEBMAIL);
                String descricao = serviceJSON.getString(DESCRICAO);

                mapsJSON = serviceJSON.getJSONArray(TAG_MAPAS);

                ArrayList<Map> maps = new ArrayList<Map>();

                for (int j = 0; j < mapsJSON.length(); j++) {
                    JSONObject jmap = mapsJSON.getJSONObject(j);
                    String location = jmap.getString(LOCALIZACAO);
                    String url = jmap.getString(URL);
                    maps.add(new Map(location, url));
                }

                phonesJSON = serviceJSON.getJSONArray(TAG_TELEFONES);

                ArrayList<Long> phones = new ArrayList<Long>();

                for (int j = 0; j < phonesJSON.length(); j++) {
                    phones.add(phonesJSON.getLong(j));
                }

                service = new Service(id, name, titulo, website, webmail, descricao, maps, phones);

                TextView Title = (TextView) this.findViewById(R.id.service_name);
                Title.setText(service.getTitulo());

                TextView WebmailTV = (TextView) this.findViewById(R.id.service_webmail_textView);
                WebmailTV.setText(service.getWebmail());

                TextView WebSiteTV = (TextView) this.findViewById(R.id.service_website_textView);
                WebSiteTV.setText(service.getWebsite());

                ContactsAdapter contactsAdapter = new ContactsAdapter(this, android.R.layout.simple_expandable_list_item_1, phones);
                phonesList = (ListView) findViewById(R.id.phones_listView);

                phonesList.setAdapter(contactsAdapter);
            }
            else
            {
                Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                this.finish();
            }

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
