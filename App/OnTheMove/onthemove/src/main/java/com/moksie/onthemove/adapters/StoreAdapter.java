package com.moksie.onthemove.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.objects.Store;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StoreAdapter extends ArrayAdapter<Store> {
    private Activity context;
    ArrayList<Store> data = null;

    public StoreAdapter(Activity context, int resource, ArrayList<Store> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.store_item, parent, false);
        }

        Store item = data.get(position);

        if (item != null) { // Parse the data from each object and set it.
            TextView StoreName = (TextView) row.findViewById(R.id.store_name);
            ImageView StoreImage = (ImageView) row.findViewById(R.id.store_image);
            ImageView PromoIcon = (ImageView) row.findViewById(R.id.promo_icon);

            if (StoreName != null) {
                StoreName.setText(item.getNome());
            }

            if (StoreImage != null) {
                new BGTGetMapImage(StoreImage).execute(item.getImagemurl());
            }

            if(PromoIcon != null) {
                if (item.isCompromocao()) {
                    PromoIcon.setVisibility(View.VISIBLE);
                }
                else PromoIcon.setVisibility(View.INVISIBLE);
            }
        }
        else Log.w("StoreAdapter", "Null Item");

        return row;
    }

    class BGTGetMapImage extends AsyncTask<String, Void, Bitmap> {
        private String url;
        private ImageView bmImage;
        private ProgressDialog pd = new ProgressDialog(context);

        public BGTGetMapImage(ImageView bmImage) {

            this.bmImage = bmImage;
            pd.setMessage("A carregar os imagens");
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
