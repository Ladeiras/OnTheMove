package com.moksie.onthemove.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;

public class OtherServicesActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_services);
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
