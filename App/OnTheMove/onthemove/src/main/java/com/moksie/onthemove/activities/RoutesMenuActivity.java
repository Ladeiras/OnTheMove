package com.moksie.onthemove.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.utilities.FileIO;

public class RoutesMenuActivity extends FragmentActivity {

    private boolean movemeFlag = false;
    private Airport airport;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_menu);

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("aeroporto");

        if(isPackageInstalled("com.moveme", this))
            movemeFlag = true;

        final Button moveMeButton = (Button) findViewById(R.id.moveme_button);
        moveMeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!movemeFlag)
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.moveme"));
                    startActivity(intent);
                }
                else
                {
                    Intent i;
                    PackageManager manager = getPackageManager();
                    try {
                        i = manager.getLaunchIntentForPackage("com.moveme");
                        if (i == null)
                            throw new PackageManager.NameNotFoundException();
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        startActivity(i);
                    } catch (PackageManager.NameNotFoundException e) {

                    }
                }

            }
        });

        final Button taxisButton = (Button) findViewById(R.id.taxis_button);
        taxisButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RoutesMenuActivity.this, ContactActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("airport", airport);
                intent.putExtra("contact", "taxis");
                RoutesMenuActivity.this.startActivity(intent);
            }
        });

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
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.move_me_footer);

        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempFlight = FileIO.deserializeVooObject(MainActivity.FILE_FLIGHT, this).toParcelable();

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
