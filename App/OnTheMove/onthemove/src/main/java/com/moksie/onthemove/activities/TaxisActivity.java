package com.moksie.onthemove.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.objects.Voo;
import com.moksie.onthemove.utilities.FileIO;

public class TaxisActivity extends FragmentActivity {

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxis);

        LinearLayout mapaLL = (LinearLayout) findViewById(R.id.taxis_ver_mapa_layout);
        mapaLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ImageView mapIcon = (ImageView) findViewById(R.id.mapa_icon_imageView);
                mapIcon.setImageResource(R.drawable.ic_icon_mapa_active);

                Intent intent = new Intent(TaxisActivity.this, MapActivity.class);
                //TODO mandar qual o mapa pra actividade
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                //intent.putExtra("aeroporto", aeroporto);
                TaxisActivity.this.startActivity(intent);
            }
        });

        restartButtons();
        updateFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();
        restartButtons();
        updateFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartButtons();
        updateFragments();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        restartButtons();
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

        if(FileIO.fileExists(MainActivity.FILE_VOO, this))
        {
            //Ler voo do ficheiro
            Voo tempVoo = FileIO.deserializeVooObject(MainActivity.FILE_VOO, this).toParcelable();

            FooterFragment.setVisibility(true);
            FooterFragment.setVoo(tempVoo);
            FooterFragment.updateVoo(this);
            FooterFragment.setVisibility(true);
        }
        else
        {
            FooterFragment.setVisibility(false);
        }

        footer.updateVisibility();
    }

    private void restartButtons() {
        ImageView mapIcon = (ImageView) findViewById(R.id.mapa_icon_imageView);
        mapIcon.setImageResource(R.drawable.ic_icon_mapa_inative);
        //TODO adicionar os restantes botoes
    }
}
