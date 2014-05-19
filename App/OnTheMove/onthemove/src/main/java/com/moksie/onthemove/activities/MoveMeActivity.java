package com.moksie.onthemove.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;
import android.widget.Button;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;

public class MoveMeActivity extends FragmentActivity {

    private boolean movemeFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_me);

        if(isPackageInstalled("com.moveme", this))
            movemeFlag = true;

        final Button button = (Button) findViewById(R.id.moveme_button2);
        button.setOnClickListener(new View.OnClickListener() {
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateFooter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFooter();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateFooter();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public void updateFooter()
    {
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.move_me_footer);

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
