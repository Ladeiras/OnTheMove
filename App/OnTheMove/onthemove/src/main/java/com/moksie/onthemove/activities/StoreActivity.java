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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.ContactsAdapter;
import com.moksie.onthemove.adapters.StoreAdapter;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Contact;
import com.moksie.onthemove.objects.Flight;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StoreActivity extends FragmentActivity {

    private Store store;
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
        setContentView(R.layout.activity_store);

        LinearLayout mapaLL = (LinearLayout) findViewById(R.id.store_ver_mapa_layout);
        mapaLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(StoreActivity.this, MapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("url", store.getMapaurl());
                intent.putExtra("title", store.getNome());
                StoreActivity.this.startActivity(intent);
            }
        });

        LinearLayout websiteLL = (LinearLayout) findViewById(R.id.store_website_LinearLayout);
        websiteLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = store.getWebsite();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        LinearLayout phoneLL = (LinearLayout) findViewById(R.id.store_phone_LinearLayout);
        phoneLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String number = "00"+store.getTelefone();
                String uri = "tel:" + number.trim() ;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        LinearLayout webmailLL = (LinearLayout) findViewById(R.id.store_webmail_LinearLayout);
        webmailLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{store.getWebmail()});
                //i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                //i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Enviar mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(StoreActivity.this, "Não existem clientes de email instalados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pd = new ProgressDialog(this);
        pd.setMessage("A carregar a Imagem");

        Intent intent = getIntent();
        store = (Store) intent.getParcelableExtra("store");

        TextView title = (TextView) findViewById(R.id.title_textView);
        title.setText(store.getNome());

        TextView website = (TextView) findViewById(R.id.website_textView);
        website.setText(store.getWebsite());

        String phone = String.valueOf(store.getTelefone());
        TextView phoneTV = (TextView) findViewById(R.id.webmail_textView);
        phoneTV.setText("+ "+phone.substring(0,3)+" "+phone.substring(3,6)+" "+phone.substring(6,9)+" "+phone.substring(9));

        TextView webmail = (TextView) findViewById(R.id.webmail_textView);
        webmail.setText(store.getWebmail());

        ImageView image = (ImageView) findViewById(R.id.store_image);
        new BGTGetMapImage(image).execute(store.getImagemurl());

        TextView description = (TextView) findViewById(R.id.store_description);
        description.setText(store.getDescricao());

        TextView promo = (TextView) findViewById(R.id.store_promotion);

        if(store.isCompromocao()) {
            promo.setVisibility(View.VISIBLE);
            promo.setText(store.getPromocao());
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

    class BGTGetMapImage extends AsyncTask<String, Void, Bitmap> {
        private String url;
        private ImageView bmImage;

        public BGTGetMapImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
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
            pd.dismiss();
        }
    }
}
