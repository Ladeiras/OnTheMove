package com.moksie.onthemove.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;


/**
 * Esta classe é usada para executar uma tarefa em background com o objectivo de obter um Bitmap
 * a partir do URL da imagem sem que a UI seja bloqueada
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class BGTGetImage extends AsyncTask<String, Void, Bitmap> {
    private String url;

    public BGTGetImage(String url) {
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected Bitmap doInBackground(String... urls) {
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(this.url).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return image;
    }
}
