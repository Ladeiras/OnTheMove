package com.moksie.onthemove.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.utilities.FileIO;

public class MenuMoveMeActivity extends FragmentActivity {

    private Airport airport;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moveme_menu);

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("airport");

        if(!isPackageInstalled("com.moveme", this))
        {
            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
            marketIntent.setData(Uri.parse("market://details?id=com.moveme"));
            startActivity(intent);
            MenuMoveMeActivity.this.finish();
        }
        else
        {
            final Button aPartirButton = (Button) findViewById(R.id.apartir_button);
            aPartirButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent movemeIntent = new Intent("com.optproject.RouteFinderScreen");

                    movemeIntent.putExtra("type", 4);
                    movemeIntent.putExtra("NameFrom", "Aeroporto");
                    movemeIntent.putExtra("LatitudeTo", airport.getLat());
                    movemeIntent.putExtra("LongitudeTo", airport.getLon());

                    startActivity(movemeIntent);
                }
            });

            final Button paraButton = (Button) findViewById(R.id.para_button);
            paraButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent movemeIntent = new Intent("com.optproject.RouteFinderScreen");

                    movemeIntent.putExtra("type", 5);
                    movemeIntent.putExtra("NameTo", "Aeroporto");
                    movemeIntent.putExtra("LatitudeTo", airport.getLat());
                    movemeIntent.putExtra("LongitudeTo", airport.getLon());

                    startActivity(movemeIntent);
                }
            });
            /*Intent i;
            PackageManager manager = getPackageManager();
            try {
                i = manager.getLaunchIntentForPackage("com.moveme");
                if (i == null)
                    throw new PackageManager.NameNotFoundException();
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {

            }*/
        }

        updateFragments();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Botao ajuda
        HeaderFragment.setMsg("Neste ecrã poderá escolher uma das opções listadas para calcular a sua rota.");

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
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.move_me_footer);

        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempFlight = FileIO.deserializeFlightObject(MainActivity.FILE_FLIGHT, this).toParcelable();

            FooterFragment.setVisibility(true);
            FooterFragment.setFlight(tempFlight);
            FooterFragment.updateFlight(this);
            FooterFragment.setVisibility(true);
        }
        else
        {
            FooterFragment.setVisibility(false);
        }

        footer.updateVisibility();
    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
