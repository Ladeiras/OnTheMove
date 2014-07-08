package com.moksie.onthemove.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.AirportAdapter;
import com.moksie.onthemove.listners.MyLocationListener;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.tasks.BGTGetJSONArray;
import com.moksie.onthemove.utilities.DistanceComparator;
import com.moksie.onthemove.utilities.ErrorManager;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    public static final String BASE_API_URL = "http://move-me.mobi:35004/OnTheMove/OnTheMoveService.svc/";
    public static String FILE_FLIGHT = "flight";
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    //TODO usado somente para testes
    public static Date currentTime;

    private static final String ID = "Id";
    private static final String PAIS = "Pais";
    private static final String CIDADE = "Cidade";
    private static final String NOME = "Nome";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";
    private static final String AEROPORTO_API_URL = "http://onthemove.no-ip.org:3000/api/aeroportos";
    private BGTGetJSONArray bgt;

    Spinner OPTaeroportoSpinner;
    ArrayList<Airport> airports = new ArrayList<Airport>();

    //OPT Airport
    private static final String CODE = "Code";
    private static final String CITY = "City";
    private static final String COUNTRY = "Country";
    private static final String EMAIL = "Email";
    private static final String FACEBOOK = "Facebook";
    private static final String FLIGHTADVISEDPREPARATIONTIME = "FlightAdvisedPreparationTime";
    private static final String LAT = "Lat";
    private static final String LEAVEDURATION = "LeaveDuration";
    private static final String LON = "Lon";
    private static final String NAME = "Name";
    private static final String SAFETYCHECKAVERAGEDURATION = "SafetyCheckAverageDuration";
    private static final String TELEF = "Telef";
    private static final String TIMEZONE = "Timezone";
    private static final String TOBOARDINGDURATION = "ToBoardingDuration";
    private static final String TOCHECKINDURATION = "ToCheckinDuration";
    private static final String TOLUGGAGEDURATION = "ToLuggageDuration";
    private static final String TOSAFETYCHECKDURATION = "ToSafetyCheckDuration";
    private static final String TWITTER = "Twitter";
    private static final String WEBSITE = "Website";

    private static final String GET_AIRPORTS_BY_TEXT = "getAirportsByText";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //TODO usado somente para testes
        try {
            currentTime = sdf.parse("2014-07-06T13:30:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        /*if(airports.isEmpty())
            buildAirportsDropDown();*/

        if(airports.isEmpty())
            buildOPTAirportsDropDown();


        /*final Button button = (Button) findViewById(R.id.okbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                intent.putExtra("airport", (Parcelable) airports.get(aeroportoSpinner.getSelectedItemPosition()));
                MainActivity.this.startActivity(intent);
            }
        });*/

        final Button button = (Button) findViewById(R.id.okbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuMainActivity.class);
                intent.putExtra("airport", (Parcelable) airports.get(OPTaeroportoSpinner.getSelectedItemPosition()));
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

    public ArrayList<Airport> sortAirports(ArrayList<Airport> data)
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

    public void buildOPTAirportsDropDown()
    {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        bgt = new BGTGetJSONArray(BASE_API_URL+GET_AIRPORTS_BY_TEXT, "GET", apiParams);

        try {
            JSONArray apJSON = bgt.execute().get();

            for(int i=0; i < apJSON.length(); i++)
            {
                JSONObject a = apJSON.getJSONObject(i);

                String code = a.getString(CODE);
                String city = a.getString(CITY);
                String country = a.getString(COUNTRY);
                String email = a.getString(EMAIL);
                String facebook = a.getString(FACEBOOK);
                long flightadvisedpreparationtime = a.getLong(FLIGHTADVISEDPREPARATIONTIME);
                double lat = a.getDouble(LAT);
                long leaveduration = a.getLong(LEAVEDURATION);
                double lon = a.getDouble(LON);
                String name = a.getString(NAME);
                long safetycheckaverageduration = a.getLong(SAFETYCHECKAVERAGEDURATION);
                String telef = a.getString(TELEF);
                String timezone = a.getString(TIMEZONE);
                long toboardingduration = a.getLong(TOBOARDINGDURATION);
                long tocheckinduration = a.getLong(TOCHECKINDURATION);
                long toluggageduration = a.getLong(TOLUGGAGEDURATION);
                long tosafetycheckduration = a.getLong(TOSAFETYCHECKDURATION);
                String twitter = a.getString(TWITTER);
                String website = a.getString(WEBSITE);

                airports.add(new Airport(code,city,country,email,facebook,
                        flightadvisedpreparationtime,lat,leaveduration,lon,name,
                        safetycheckaverageduration,telef,timezone,toboardingduration,
                        tocheckinduration,toluggageduration,tosafetycheckduration,twitter,website));

                sortAirports(airports);

                AirportAdapter aAdapter = new AirportAdapter(this, android.R.layout.simple_spinner_item, airports);
                OPTaeroportoSpinner = (Spinner) findViewById(R.id.aeroportos);

                OPTaeroportoSpinner.setAdapter(aAdapter);
                OPTaeroportoSpinner.setPrompt("Aeroporto");

                OPTaeroportoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        ErrorManager.Exit(this);
    }
}
