package com.moksie.onthemove.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.AeroportoAdapter;
import com.moksie.onthemove.listners.MyLocationListener;
import com.moksie.onthemove.objects.Aeroporto;
import com.moksie.onthemove.tasks.BackGroundTask2;
import com.moksie.onthemove.utilities.DistanceComparator;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    public static String FILE_VOO = "voo";
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static final String ID = "Id";
    private static final String PAIS = "Pais";
    private static final String CIDADE = "Cidade";
    private static final String NOME = "Nome";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";
    private static final String AEROPORTO_API_URL = "http://onthemove.no-ip.org:3000/api/aeroportos";
    private BackGroundTask2 bgt;

    Spinner aeroportoSpinner;
    ArrayList<Aeroporto> aeroportos = new ArrayList<Aeroporto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //Verificar se o GPS esta ligado
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Intent intent = new Intent(MainActivity.this, GPSAlertActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Construir lista de aeroportos + botao "OK"
        if(aeroportos.isEmpty())
            buildAeroportoDropDown();

        final Button button = (Button) findViewById(R.id.okbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("aeroporto", (Parcelable)aeroportos.get(aeroportoSpinner.getSelectedItemPosition()));
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buildAeroportoDropDown()
    {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        bgt = new BackGroundTask2(AEROPORTO_API_URL, "GET", apiParams);

        try {
            JSONArray apJSON = bgt.execute().get();

            for(int i=0; i < apJSON.length(); i++)
            {
                JSONObject a = apJSON.getJSONObject(i);

                long id = a.getLong(ID);
                String pais = a.getString(PAIS);
                String cidade = a.getString(CIDADE);
                String nome = a.getString(NOME);
                double latitude = a.getDouble(LATITUDE);
                double longitude = a.getDouble(LONGITUDE);

                aeroportos.add(new Aeroporto(id,pais,cidade,nome,latitude,longitude));

                sortData(aeroportos);

                AeroportoAdapter aAdapter = new AeroportoAdapter(this, android.R.layout.simple_spinner_item, aeroportos);
                aeroportoSpinner = (Spinner) findViewById(R.id.aeroportos);

                aeroportoSpinner.setAdapter(aAdapter);
                aeroportoSpinner.setPrompt("Aeroporto");

                aeroportoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //Fazer nada
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<Aeroporto> sortData(ArrayList<Aeroporto> data)
    {
        LocationManager mlocManager=null;
        LocationListener mlocListener;
        double latitude, longitude;

        mlocManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);


        //while(!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {}

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            latitude = MyLocationListener.latitude;
            longitude = MyLocationListener.longitude;
        }
        else return data;

        Collections.sort(data, new DistanceComparator(this, latitude, longitude));

        return data;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
