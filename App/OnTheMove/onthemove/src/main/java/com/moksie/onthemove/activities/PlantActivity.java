package com.moksie.onthemove.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.objects.Location;
import com.moksie.onthemove.utilities.FileIO;
import com.polites.android.GestureImageView;

import java.io.InputStream;

/**
 * Nesta classe é mostrada a imagem de uma planta com ou sem pontos de interesse, dependendo da
 * opção que é passada por parametro.
 * Se a opção for OPTION_LOCATIONS, significa que é passado um objecto do tipo location e se a
 * opção for OPTION_ALL significa que apenas é necessário mostrar a imagem da planta.
 * São passados também o url da imagem da planta e o titulo a ser mostrado.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class PlantActivity extends FragmentActivity {

    //Opções da Activity
    private static final String OPTION_LOCATIONS = "locations";
    private static final String OPTION_ALL = "all";
    private static final int MARKER_SIZE = 100;

    private ProgressDialog pd;
    private Location location;
    private String option;
    private String title;
    private String url;
    private Bitmap overlayedImage;
    private Bitmap marker;
    private int posX;
    private int posY;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);

        pd = new ProgressDialog(this);
        pd.setMessage("A carregar a Imagem");

        Intent intent = getIntent();

        option = intent.getStringExtra("option");
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");

        //Imagem
        GestureImageView iv = (GestureImageView) findViewById(R.id.map_imageView);
        if(option.equals(OPTION_ALL)) {
            //Se a opção for OPTION_ALL apenas é mostrada a planta (sem pontos)
            new BGTGetMapImage(iv).execute(url);
        }
        else if(option.equals(OPTION_LOCATIONS)) {
            /*
             * Se a opção for OPTION_LOCATIONS são obtidas as coordenadas e o icone a partir do tipo
             * de location
             */
            location = (Location) intent.getParcelableExtra("location");
            String type = location.getType();
            posX = (int) location.getPosx();
            posY = (int) location.getPosy();

            //Atualização da posição para que o icone aponte para o pixel da planta pretendido
            if(posY > MARKER_SIZE)
                posY -= MARKER_SIZE;

            if(posX > MARKER_SIZE)
                posX -= MARKER_SIZE/2;

            if(type.equals("WC"))
                marker = BitmapFactory.decodeResource(getResources(), R.drawable.ic_icon_wc);
            else if(type.equals("Restaurant"))
                marker = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_icon_restaurantes);
            else if(type.equals("SmokingZone"))
                marker = BitmapFactory.decodeResource(getResources(), R.drawable.ic_icon_fumadores);
            else if(type.equals("Shop"))
                marker = BitmapFactory.decodeResource(getResources(), R.drawable.ic_icon_lojas);
            else if(type.equals("Parking"))
                marker = BitmapFactory.decodeResource(getResources(), R.drawable.ic_icon_parking);

            marker = Bitmap.createScaledBitmap(marker, MARKER_SIZE, MARKER_SIZE, true);

            new BGTGetMapImage(iv).execute(url);
        }

        TextView tv = (TextView) findViewById(R.id.title_textView);
        tv.setText(title);

        updateFragments();
    }

    /**
     * Nesta função é feita a sobreposição do bitmap da planta com o bitmap do icone do ponto da
     * localização
     * @param bitmap Bitmap da Planta
     * @param overlay Bitmap do Icone
     * @param posX
     * @param posY
     * @return Novo Bitmap com as imagens sobrepostas
     */
    public Bitmap putOverlay(Bitmap bitmap, Bitmap overlay, int posX, int posY) {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(overlay, posX, posY, paint);

        return mutableBitmap;
    }

    @Override
    protected void onStart() {
        super.onStart();

        HeaderFragment.setMsg("Neste ecrã pode consultar a planta do serviço seleccionado");

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
        LinearLayout layout = (LinearLayout) findViewById(R.id.map_LinearLayout);
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_footer);

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

    /**
     * Esta classe é usada para executar uma tarefa em background com o objectivo de obter um Bitmap
     * a partir do URL da imagem sem que a UI seja bloqueada
     */
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
            //Se o mapa tem locais é incorporado o icone na posicao correspondente
            if(option.equals(OPTION_LOCATIONS))
            {
                overlayedImage = result;
                overlayedImage = putOverlay(overlayedImage, marker, posX, posY);
                bmImage.setImageBitmap(overlayedImage);
            }
            else bmImage.setImageBitmap(result);

            pd.dismiss();
        }
    }
}
