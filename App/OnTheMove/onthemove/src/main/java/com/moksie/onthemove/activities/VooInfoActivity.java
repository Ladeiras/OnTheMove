package com.moksie.onthemove.activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.NaoSeguirFragment;
import com.moksie.onthemove.fragments.SeguirFragment;
import com.moksie.onthemove.objects.Aeroporto;
import com.moksie.onthemove.objects.Voo;
import com.moksie.onthemove.utilities.FileIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VooInfoActivity extends FragmentActivity
{
    public static int SEGUIR_ATUAL = 0;
    public static int SEGUIR_OUTRO = 1;
    public static int NAO_SEGUIR = 2;

    private int estado = NAO_SEGUIR;
    private Voo voo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_voo_info);

        Bundle data = getIntent().getExtras();
        voo = (Voo) data.getParcelable("voo");

        parseVooFile();
        updateFragments(estado);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateFragments(estado);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFragments(estado);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //updateFragments(seguir);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public void parseVooFile()
    {
        if(FileIO.fileExists(MainActivity.FILE_VOO, this))
        {
            //Ler voo do ficheiro
            String temp = FileIO.readFromFile(MainActivity.FILE_VOO, this);
            Voo tempvoo = new Voo(temp);

            if(tempvoo.getId() == voo.getId())
            {
                estado = SEGUIR_ATUAL;
                voo = tempvoo;
            }
            else
            {
                estado = SEGUIR_OUTRO;
            }
        }
        else
        {
            estado = NAO_SEGUIR;
        }
    }

    public void updateFragments(int estado)
    {
        if(estado == SEGUIR_ATUAL || estado == SEGUIR_OUTRO)
            FooterFragment.setVisibility(true);
        else
            FooterFragment.setVisibility(false);

        updateFooter();
        updateSeguir(estado);
    }

    public void updateFooter()
    {
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.voo_info_footer);

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

    public void updateSeguir(int estado)
    {
        if(estado == SEGUIR_ATUAL)
        {
            Fragment naoseguir = new NaoSeguirFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.voo_info_seguir, naoseguir);
            fragmentTransaction.commit();
        }
        else
        {
            Fragment seguir = new SeguirFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.voo_info_seguir, seguir);
            fragmentTransaction.commit();
        }
    }

    public void onClickSeguir()
    {
        if(estado == NAO_SEGUIR || estado == SEGUIR_OUTRO)
        {
            estado = SEGUIR_ATUAL;
            updateSeguir(estado);
            FileIO.removeFile(MainActivity.FILE_VOO, this);
            FileIO.writeToFile(MainActivity.FILE_VOO, voo.toString(), this);
            Log.w("FLAG", "Escreveu? vooID: "+voo.getId());

            //Footer
            updateFooter();
        }
    }

    public void onClickNaoSeguir()
    {
        if(estado == SEGUIR_ATUAL)
        {
            estado = NAO_SEGUIR;
            updateSeguir(estado);
            FileIO.removeFile(MainActivity.FILE_VOO, this);
            Log.w("FLAG", "Removeu? vooID: "+voo.getId());

            //Footer
            updateFooter();
        }
    }
}
