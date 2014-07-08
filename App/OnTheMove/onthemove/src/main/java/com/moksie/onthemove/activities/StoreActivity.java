package com.moksie.onthemove.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Campaign;
import com.moksie.onthemove.objects.Contact;
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

public class StoreActivity extends FragmentActivity {

    private static final String OPTION_LOCATIONS = "locations";

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

    //private static final String CODE = "Code";
    private static final String KEYWORDS = "Keywords";
    //private static final String LANG = "Lang";
    //private static final String NAME = "Name";
    //private static final String POSX = "PosX";
    //private static final String POSY = "PosY";
    private static final String TYPE_TAG = "Type";
    private static final String DESC = "Desc";

    //private static final String GET_LOCATIONS_BY_TYPE = "getLocationsByType";
    private static final String GET_LOCATIONS_BY_PLANT = "getLocationsByPlant";
    //private static final String PARAM_LOCATION_TYPES = "locationTypes";
    private static final String PARAM_PLANT_CODE = "plantCode";
    //private static final String PARAM_LANG = "lang";
    //private static final String PT = "PT";

    private BGTGetJSONArray bgt;

    private ArrayList<Plant> plants = new ArrayList<Plant>();
    private ArrayList<Location> locations = new ArrayList<Location>();
    private Plant plant;

    private Contact contact;
    private Location location;
    private Campaign campaign;

    private ProgressDialog pd1;
    private ProgressDialog pd2;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        pd1 = new ProgressDialog(this);
        pd2 = new ProgressDialog(this);
        pd1.setMessage("A carregar a imagem");
        pd2.setMessage("A carregar Plantas");

        Intent intent = getIntent();
        location = (Location) intent.getParcelableExtra("location");
        contact = (Contact) intent.getParcelableExtra("contact");
        campaign = (Campaign) intent.getParcelableExtra("campaign");

        getPlants();

        for(int i=0;i<plants.size();i++)
        {
            locations = new ArrayList<Location>();
            getLocations(plants.get(i).getCode());

            for(int j=0;j<locations.size();j++)
            {
                if(location.getCode().equals(locations.get(j).getCode()))
                    plant = plants.get(i);
            }
        }

        LinearLayout mapaLL = (LinearLayout) findViewById(R.id.store_ver_mapa_layout);
        mapaLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(StoreActivity.this, PlantActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("url", plant.getImageurl());
                intent.putExtra("title", location.getName());
                intent.putExtra("location", location);
                intent.putExtra("option", OPTION_LOCATIONS);
                StoreActivity.this.startActivity(intent);
            }
        });

        LinearLayout websiteLL = (LinearLayout) findViewById(R.id.store_website_LinearLayout);
        if(!contact.getWebsite().equals("null")) {
            websiteLL.setVisibility(View.VISIBLE);
            websiteLL.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String url = contact.getWebsite();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }
        else websiteLL.setVisibility(View.GONE);

        LinearLayout phoneLL = (LinearLayout) findViewById(R.id.store_phone_LinearLayout);
        if(!contact.getTelef().equals("null")) {
            phoneLL.setVisibility(View.VISIBLE);
            phoneLL.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String number = contact.getTelef();
                    String split[] = number.split("\\+");
                    number = split[1];
                    number.replaceAll("\\s", "");
                    number = "tel:00" + number;

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(number));
                    startActivity(intent);
                }
            });
        }
        else phoneLL.setVisibility(View.GONE);

        LinearLayout webmailLL = (LinearLayout) findViewById(R.id.store_webmail_LinearLayout);
        if(!contact.getEmail().equals("null")) {
            webmailLL.setVisibility(View.VISIBLE);
            webmailLL.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{contact.getEmail()});
                    //i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                    //i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                    try {
                        startActivity(Intent.createChooser(i, "Enviar mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(StoreActivity.this, "Não existem clientes de email instalados", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else webmailLL.setVisibility(View.GONE);

        LinearLayout facebookLL = (LinearLayout) findViewById(R.id.store_facebook_LinearLayout);
        if(!contact.getFacebook().equals("null")) {
            facebookLL.setVisibility(View.VISIBLE);
            facebookLL.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String url = contact.getFacebook();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }
        else facebookLL.setVisibility(View.GONE);

        LinearLayout twitterLL = (LinearLayout) findViewById(R.id.store_twitter_LinearLayout);
        if(!contact.getTwitter().equals("null")) {
            twitterLL.setVisibility(View.VISIBLE);
            twitterLL.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String url = contact.getTwitter();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }
        else twitterLL.setVisibility(View.GONE);

        TextView title = (TextView) findViewById(R.id.title_textView);
        title.setText(location.getName());

        TextView website = (TextView) findViewById(R.id.website_textView);
        website.setText(contact.getWebsite());

        TextView phone = (TextView) findViewById(R.id.phone_textView);
        phone.setText(contact.getTelef());

        TextView webmail = (TextView) findViewById(R.id.webmail_textView);
        webmail.setText(contact.getEmail());

        ImageView image = (ImageView) findViewById(R.id.store_image);
        new BGTGetMapImage(image).execute(contact.getLogourl());

        //TextView description = (TextView) findViewById(R.id.store_description);
        //description.setText(store.getDescricao());

        TextView promo = (TextView) findViewById(R.id.store_promotion);

        if(null != campaign) {
            promo.setVisibility(View.VISIBLE);
            promo.setText(campaign.getCampaigndesc());
        }
        else promo.setVisibility(View.GONE);

        updateFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Botao ajuda
        HeaderFragment.setMsg("Neste ecrã poderá consultar informações sobre a loja.");

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
        LinearLayout layout = (LinearLayout) findViewById(R.id.store_LinearLayout);
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.store_footer);

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

    public void getLocations(String plantCode)
    {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(2);
        apiParams.add(new BasicNameValuePair(PARAM_PLANT_CODE, plantCode));
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

    class BGTGetMapImage extends AsyncTask<String, Void, Bitmap> {
        private String url;
        private ImageView bmImage;

        public BGTGetMapImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd1.show();
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            bmImage.setImageBitmap(result);
            pd1.dismiss();
        }
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
            pd2.show();
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
            pd2.dismiss();
        }
    }
}
