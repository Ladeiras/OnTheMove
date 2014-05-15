package com.moksie.onthemove.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Aeroporto;
import com.moksie.onthemove.objects.Voo;
import com.moksie.onthemove.utilities.FileIO;

public class MenuActivity extends FragmentActivity {

    private Aeroporto aeroporto;
    private Voo vooASeguir;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        //Fragments
        updateFragments();

        Intent intent = getIntent();
        aeroporto = (Aeroporto) intent.getParcelableExtra("aeroporto");

        //Botao Voos
        final Button voosButton = (Button) findViewById(R.id.voos_button);
        voosButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, VoosActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("aeroporto", aeroporto);
                MenuActivity.this.startActivity(intent);
            }
        });

        //Botao Calculo Rota
        final Button cdRButton = (Button) findViewById(R.id.moveme_button);
        cdRButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MoveMeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("aeroporto", aeroporto);
                MenuActivity.this.startActivity(intent);
            }
        });
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

    public void updateFragments()
    {
        updateFooter();
    }

    public void updateFooter()
    {
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.menu_footer);

        if(FileIO.fileExists(MainActivity.FILE_VOO, this))
        {
            //Ler voo do ficheiro
            String temp = FileIO.readFromFile(MainActivity.FILE_VOO, this);
            Voo tempVoo = new Voo(temp);

            FooterFragment.setVisibility(true);
            FooterFragment.setVoo(tempVoo);
            FooterFragment.updateVoo(this);
            FooterFragment.setVisibility(true);
        }
        else FooterFragment.setVisibility(false);

        footer.updateVisibility();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
