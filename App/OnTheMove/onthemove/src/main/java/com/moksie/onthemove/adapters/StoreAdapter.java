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
import com.moksie.onthemove.objects.Store;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Nesta classe é feita a população de uma vista (elemento de uma lista de lojas) a partir de um
 * Array de lojas.
 * Cada elemento é composto pelo logo da loja, nome e um icone que indica se a loja tem neste
 * momento alguma promoção.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

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

        if (item != null) {
            TextView StoreName = (TextView) row.findViewById(R.id.store_name);
            ImageView StoreImage = (ImageView) row.findViewById(R.id.store_image);
            ImageView PromoIcon = (ImageView) row.findViewById(R.id.promo_icon);

            if (StoreName != null) {
                StoreName.setText(item.getLocation().getName());
            }

            if (StoreImage != null) {
                new BGTGetLogoImage(StoreImage).execute(item.getContact().getLogourl());
            }

            if(PromoIcon != null) {
                if (item.isWithCampaign()) {
                    PromoIcon.setVisibility(View.VISIBLE);
                }
                else PromoIcon.setVisibility(View.INVISIBLE);
            }
        }
        else Log.w("StoreAdapter", "Null Item");

        return row;
    }

    /**
     * Esta classe é usada para executar uma tarefa em background com o objectivo de obter um Bitmap
     * a partir do URL da imagem sem que a UI seja bloqueada
     */
    class BGTGetLogoImage extends AsyncTask<String, Void, Bitmap> {
        private String url;
        private ImageView bmImage;
        private ProgressDialog pd = new ProgressDialog(context);

        public BGTGetLogoImage(ImageView bmImage) {

            this.bmImage = bmImage;
            pd.setMessage("A carregar as imagens");
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
