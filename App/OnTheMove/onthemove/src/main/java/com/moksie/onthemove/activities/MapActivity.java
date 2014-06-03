package com.moksie.onthemove.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.tasks.BGTGetImage;
import com.moksie.onthemove.tasks.BGTGetJSONArray;
import com.moksie.onthemove.utilities.FileIO;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class MapActivity extends FragmentActivity {

    private boolean mapClick = false;
    private Bitmap image;
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
        setContentView(R.layout.activity_map);

        pd = new ProgressDialog(this);
        pd.setMessage("A carregar a Imagem");

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");

        TextView tv = (TextView) findViewById(R.id.title_textView);
        tv.setText(title);

        ImageView iv = (ImageView) findViewById(R.id.map_imageView);
        new BGTGetMapImage(iv).execute(url);

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
        LinearLayout layout = (LinearLayout) findViewById(R.id.map_LinearLayout);
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_footer);

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
