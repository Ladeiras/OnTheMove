package com.moksie.onthemove.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.SearchAdapter;
import com.moksie.onthemove.adapters.StoreAdapter;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Campaign;
import com.moksie.onthemove.objects.Contact;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.objects.Location;
import com.moksie.onthemove.objects.Store;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Nesta classe é mostrada uma lista com todas as lojas do aeorporto, contendo um spinner para
 * filtrar a informação.
 * As opções do spinner são nome da lojas, categoria, lojas com promoções e todas as lojas.
 * Através de pedidos a ambos os servidores, são obtidas as informações de locations, contacts e
 * campaigns.
 * Cada loja é representada pelo logo, nome e um icone que mostra se a loja tem alguma promoção.
 * Cada elemento loja contém um Listner que quando carregado inicia uma StoreActivity correspondendo
 * à loja seleccionada, sendo passados um Contact, um Location e um Campaign (se existir promoção)
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class StoreListActivity extends FragmentActivity {

    private static final String OPTION_LOCATIONS = "locations";

    //Variáveis usadas no pedido dos Location ao servidor a partir do tipo (Shop)
    private static final String CODE = "Code";
    private static final String KEYWORDS = "Keywords";
    private static final String LANG = "Lang";
    private static final String NAME = "Name";
    private static final String POSX = "PosX";
    private static final String POSY = "PosY";
    private static final String TYPE_TAG = "Type";
    private static final String DESC = "Desc";
    private static final String GET_LOCATIONS_BY_TYPE = "getLocationsByType";
    private static final String PARAM_LOCATION_TYPES = "locationTypes";
    private static final String LOCATION_TYPE = "Shop";
    private static final String PARAM_LANG = "lang";
    private static final String PT = "PT";

    private ArrayList<Location> locations = new ArrayList<Location>();


    //Variáveis usadas no pedido dos Contact ao servidor a partir do tipo (Shop)
    private static final String EMAIL = "Email";
    private static final String FACEBOOK = "Facebook";
    private static final String TELEF = "Telef";
    private static final String TWITTER = "Twitter";
    private static final String WEBSITE = "Website";
    private static final String LOGOURL = "LogoUrl";

    private static final String PARAM_AIRPORT_CODE = "id";
    private static final String PARAM_TYPE = "type";

    private static final String GET_CONTACT = "contact/";

    private ArrayList<Contact> contacts = new ArrayList<Contact>();


    //Variáveis usadas no pedido das Campaign ao servidor
    private static final String CAMPAIGNDESC = "CampaignDesc";
    private static final String IMAGEURL = "ImageUrl";
    private static final String SHOPCODE = "ShopCode";

    private static final String GET_SHOP_CAMPAIGNS = "getShopCampaigns";

    private ArrayList<Campaign> campaigns = new ArrayList<Campaign>();

    //Variaveis para controlo e conteudo do spinner da pesquisa das lojas
    private static final int OPTION_NOME_LOJA = 0;
    private static final int OPTION_CATEGORIA = 1;
    private static final int OPTION_LOJA_COM_PROMOCOES = 2;
    private static final int OPTION_SHOW_ALL = 3;
    public static final int OPTION_DEFAULT = 4;

    private static final String OPTION_NOME_LOJA_VALUE = "Nome das Lojas";
    private static final String OPTION_CATEGORIA_VALUE = "Categoria";
    private static final String OPTION_LOJA_COM_PROMOCOES_VALUE = "Loja com Promoções";
    private static final String OPTION_SHOW_ALL_VALUE = "Todas as Lojas";
    private static final String OPTION_DEFAULT_VALUE = "";

    //Opções de pesquisa do spinner
    final ArrayList<String> options = new ArrayList(Arrays.asList(OPTION_NOME_LOJA_VALUE,
            OPTION_CATEGORIA_VALUE, OPTION_LOJA_COM_PROMOCOES_VALUE, OPTION_SHOW_ALL_VALUE, OPTION_DEFAULT_VALUE));

    private String idAirport;
    private String startOption;
    private BGTGetJSONArray bgt;

    private boolean moksieFlag = true;

    private ProgressDialog pd;

    private ArrayList<Store> stores = new ArrayList<Store>();
    ListView storesList;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        Intent intent = getIntent();
        idAirport = intent.getStringExtra("airport");
        startOption = (String) intent.getStringExtra("option");

        pd = new ProgressDialog(this);
        pd.setMessage("A carregar lojas");

        //População das várias listas através dos pedidos ao servidor
        try {
            buildLocationsList();
            buildContactsList();
            buildCampaignsList();
            buildStoresList();
        } catch (ParseException e)
        {
            Log.e("Contact","Error building list");
            e.printStackTrace();
        }

        //Mensagem de erro caso o servidor moksie nao esteja a responder
        if(!moksieFlag)
        {
            Toast.makeText(this, "Problemas de ligação ao servidor moksie",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        //Construção da lista de lojas passando o array de lojas ao StoreAdapter
        StoreAdapter storeAdapter = new StoreAdapter(this, android.R.layout.simple_expandable_list_item_1, stores);
        storesList = (ListView) findViewById(R.id.stores_listView);

        storesList.setAdapter(storeAdapter);

        //Spinner de pesquisa
        final Spinner searchStoresSpinner = (Spinner)findViewById(R.id.search_spinner);
        SearchAdapter adapter = new SearchAdapter(this, android.R.layout.simple_spinner_item, options);

        searchStoresSpinner.setAdapter(adapter);

        //Opção de pesquisa inicial
        if(startOption.equals("promo"))
            searchStoresSpinner.setSelection(OPTION_LOJA_COM_PROMOCOES);
        else searchStoresSpinner.setSelection(OPTION_DEFAULT);

        /**
         * Listner do Spinner.
         * Quando é carregado, são apresentadas as opções, e para cada opção excepto a opção de
         * mostrar todas as lojas e lojas com promoção, é mostrada uma caixa de texto para o input
         * a ser usado na pesquisa.
         */
        searchStoresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                final int option = i;

                if(option == OPTION_SHOW_ALL) {
                    updateStores(stores);
                }
                else if(option == OPTION_LOJA_COM_PROMOCOES) {
                    filterStores(null, option);
                }
                else if(option != OPTION_DEFAULT) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(StoreListActivity.this);

                    alert.setTitle("Pesquisar por " + options.get(option));
                    //alert.setMessage(options.get(i));

                    // Set an EditText view to get user input
                    final EditText input = new EditText(StoreListActivity.this);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = String.valueOf(input.getText());
                            filterStores(value, option);
                        }
                    });

                    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    alert.show();
                }

                searchStoresSpinner.setSelection(OPTION_DEFAULT);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Listner das Lojas da Lista
        if(!stores.isEmpty()) {
            storesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int i, long l) {
                    Intent intent = new Intent(StoreListActivity.this, StoreActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Store store = (Store) storesList.getAdapter().getItem(i);
                    intent.putExtra("contact", store.getContact());
                    intent.putExtra("location", store.getLocation());
                    intent.putExtra("campaign", store.getCampaign());
                    StoreListActivity.this.startActivity(intent);
                }
            });
        }
        else
        {
            //Mensagem de erro caso a lista de lojas esteja vazia, terminando a Activity
            Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        //Fragments
        updateFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Botao ajuda
        HeaderFragment.setMsg("Neste ecrã poderá consultar uma lista das lojas existentes no aeroporto. Poderá ainda usar os filtros de pesquisa.");

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
     * Nesta função, dependendo da opção, são pesquisados as lojas.
     * A pesquisa basicamente verifica se a string de input está contida na string a ser pesquisada
     * e vice versa.
     * Se for encontrado algum resultado, este é passado para um array temporario, que é colocado
     * na view da lista de lojas.
     * Se não for encontrado nenhum resultado é apresentada uma mensagem sem alterar a View.
     * @param value String de input do utilizador
     * @param option Opção de pesquisa
     */
    private void filterStores(String value, int option)
    {
        ArrayList<Store> temp = new ArrayList<Store>();

        if(option==OPTION_NOME_LOJA)
        {
            for(int i=0;i<stores.size();i++)
                if(stores.get(i).getLocation().getName().toLowerCase().contains(value.toLowerCase()) ||
                        value.toLowerCase().contains(stores.get(i).getLocation().getName().toLowerCase()))
                    temp.add(stores.get(i));
        }
        else if(option==OPTION_CATEGORIA)
        {
            for(int i=0;i<stores.size();i++)
                if(stores.get(i).getLocation().getKeywords().toLowerCase().contains(value.toLowerCase()) ||
                        value.toLowerCase().contains(stores.get(i).getLocation().getKeywords().toLowerCase()))
                    temp.add(stores.get(i));
        }
        else if(option==OPTION_LOJA_COM_PROMOCOES)
        {
            for(int i=0;i<stores.size();i++)
                if(stores.get(i).isWithCampaign())
                    temp.add(stores.get(i));
        }

        if(!temp.isEmpty()) {
            updateStores(temp);
        }
        else
        {
            //Mensagem de erro caso não seja encontrada nehuma loja
            Toast.makeText(getApplicationContext(), "Não foram encontrados resultados",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Esta função é usada para atualizar a vista da lista das lojas
     * @param tempStores
     */
    private void updateStores(ArrayList<Store> tempStores) {
        StoreAdapter storesAdapter = new StoreAdapter(this, android.R.layout.simple_expandable_list_item_1, tempStores);
        storesList = (ListView) findViewById(R.id.stores_listView);
        storesList.setAdapter(storesAdapter);
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
        LinearLayout layout = (LinearLayout) findViewById(R.id.stores_LinearLayout);
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.stores_footer);

        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempFlight = FileIO.deserializeFlightObject(MainActivity.FILE_FLIGHT, this).toParcelable();

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

    //Construção da lista das lojas a partir das listas de locations, contacts e campaigns
    public void buildStoresList()
    {
        //Verificar se para todos os locais existem contactos e promoções
        for(int i=0;i<locations.size();i++)
        {
            Store tempStore = new Store();
            tempStore.setLocation(locations.get(i));

            for(int j=0;j<contacts.size();j++)
            {
                if(locations.get(i).getCode().equals(contacts.get(j).getName()))
                    tempStore.setContact(contacts.get(j));
            }

            for(int k=0;k<campaigns.size();k++)
            {
                if(locations.get(i).getCode().equals(campaigns.get(k).getShopcode())) {
                    tempStore.setCampaign(campaigns.get(k));
                    tempStore.setWithCampaign(true);
                }
            }

            if(null != tempStore.getLocation())
                Log.d("STORE", "location "+tempStore.getLocation().getCode()+","+tempStore.getLocation().getName());
            if(null != tempStore.getContact())
                Log.d("STORE", "contact "+tempStore.getContact().getName());
            if(null != tempStore.getCampaign())
                Log.d("STORE", "campaign "+tempStore.getCampaign().getShopcode());
            stores.add(tempStore);
        }
    }

    /**
     * Construção da lista de locations fazendo um pedido ao servidor
     * @throws ParseException
     */
    public void buildLocationsList() throws ParseException {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(2);
        apiParams.add(new BasicNameValuePair(PARAM_LOCATION_TYPES, LOCATION_TYPE));
        apiParams.add(new BasicNameValuePair(PARAM_LANG, PT));
        bgt = new BGTGetJSONArray(MainActivity.BASE_API_URL+GET_LOCATIONS_BY_TYPE, "GET", apiParams);

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

    /**
     * Construção da lista de contacts fazendo um pedido ao servidor
     * @throws ParseException
     */
    public void buildContactsList() throws ParseException {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(2);
        apiParams.add(new BasicNameValuePair(PARAM_AIRPORT_CODE, idAirport));
        apiParams.add(new BasicNameValuePair(PARAM_TYPE, LOCATION_TYPE));
        bgt = new BGTGetJSONArray(MainActivity.BASE_API2_URL+GET_CONTACT, "GET", apiParams);

        try {
            JSONArray apJSON = bgt.execute().get();

            if(null != apJSON) {
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

                    contacts.add(new Contact(code, email, facebook, telef, twitter, website,
                            logourl));
                }
            }
            else moksieFlag = false;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construção da lista de campaigns fazendo um pedido ao servidor
     * @throws ParseException
     */
    public void buildCampaignsList() throws ParseException {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(2);
        apiParams.add(new BasicNameValuePair(PARAM_LANG, PT));
        bgt = new BGTGetJSONArray(MainActivity.BASE_API_URL+GET_SHOP_CAMPAIGNS, "GET", apiParams);

        try {
            JSONArray apJSON = bgt.execute().get();

            for(int i=0; i < apJSON.length(); i++)
            {
                JSONObject a = apJSON.getJSONObject(i);

                String campaigndesc = a.getString(CAMPAIGNDESC);
                String imageurl = a.getString(IMAGEURL);
                String shopcode = a.getString(SHOPCODE);

                campaigns.add(new Campaign(campaigndesc, imageurl, shopcode));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
