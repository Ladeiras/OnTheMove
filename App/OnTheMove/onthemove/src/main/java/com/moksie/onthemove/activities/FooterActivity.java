package com.moksie.onthemove.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.objects.Voo;
import com.moksie.onthemove.objects.VooSerializable;
import com.moksie.onthemove.utilities.FileIO;

import java.text.SimpleDateFormat;

public class FooterActivity extends FragmentActivity {

    private VooSerializable voo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition( R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        setContentView(R.layout.activity_footer);

        //Ler voo do ficheiro
        parseVooFile();

        TextView CodigoVoo = (TextView) this.findViewById(R.id.codigo_voo_textView);
        CodigoVoo.setText(String.valueOf(FooterFragment.voo.getCodigovoo()));

        TextView OrigemDestino = (TextView) this.findViewById(R.id.origem_destino_textView);
        OrigemDestino.setText(FooterFragment.voo.getPartidacidade()+" - "+FooterFragment.voo.getChegadacidade());

        TextView TempoEstimado = (TextView) this.findViewById(R.id.tempo_estimado_textView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.applyPattern("dd/MM HH:mm");

        if(FooterFragment.voo.isPartida())
            TempoEstimado.setText(sdf.format(FooterFragment.voo.getPartidatempoestimado()));
        else
            TempoEstimado.setText(sdf.format(FooterFragment.voo.getChegadatempoestimado()));

        CheckBox airportCB = (CheckBox) findViewById(R.id.airport_checkBox);
        if(voo.isCheckin() && !airportCB.isChecked()) {airportCB.toggle();}

        CheckBox checkinCB = (CheckBox) findViewById(R.id.checkin_checkBox);
        if(voo.isCheckin() && !checkinCB.isChecked()) {checkinCB.toggle();}

        CheckBox securityCB = (CheckBox) findViewById(R.id.security_checkBox);
        if(voo.isSecurity() && !securityCB.isChecked()) {securityCB.toggle();}

        CheckBox boardingCB = (CheckBox) findViewById(R.id.boarding_checkBox);
        if(voo.isBoarding() && !boardingCB.isChecked()) {boardingCB.toggle();}

        //Botao Ver Info Voo
        final Button vooButton = (Button) findViewById(R.id.voo_seguido_button);
        vooButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FooterActivity.this, VooInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("voo",voo.toParcelable());
                FooterActivity.this.startActivity(intent);
            }
        });

        //Botao Ver Promocoes
        final Button promotionsButton = (Button) findViewById(R.id.promotions_button);
        promotionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               //TODO
            }
        });
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.airport_checkBox:
                if (checked)
                    this.voo.setAirport(true);
                else
                {
                    this.voo.setAirport(false);
                    this.voo.setCheckin(false);
                    this.voo.setSecurity(false);
                    this.voo.setBoarding(false);

                    CheckBox checkin = (CheckBox) findViewById(R.id.checkin_checkBox);
                    if(checkin.isChecked()) {checkin.toggle();}

                    CheckBox security = (CheckBox) findViewById(R.id.security_checkBox);
                    if(security.isChecked()) {security.toggle();}

                    CheckBox boarding = (CheckBox) findViewById(R.id.boarding_checkBox);
                    if(boarding.isChecked()) {boarding.toggle();}
                }
                break;
            case R.id.checkin_checkBox:
                if (checked) {
                    if(!this.voo.isAirport())
                        ((CheckBox) view).toggle();
                    else this.voo.setCheckin(true);
                }
                else
                {
                    this.voo.setCheckin(false);
                    this.voo.setSecurity(false);
                    this.voo.setBoarding(false);

                    CheckBox security = (CheckBox) findViewById(R.id.security_checkBox);
                    if(security.isChecked()) {security.toggle();}

                    CheckBox boarding = (CheckBox) findViewById(R.id.boarding_checkBox);
                    if(boarding.isChecked()) {boarding.toggle();}
                }
                break;
            case R.id.security_checkBox:
                if(checked) {
                    if(!this.voo.isCheckin())
                        ((CheckBox) view).toggle();
                    else this.voo.setSecurity(true);
                }
                else
                {
                    this.voo.setSecurity(false);
                    this.voo.setBoarding(false);

                    CheckBox boarding = (CheckBox) findViewById(R.id.boarding_checkBox);
                    if(boarding.isChecked()) {boarding.toggle();}
                }
                break;
            case R.id.boarding_checkBox:
                if(checked) {
                    if(!this.voo.isSecurity())
                        ((CheckBox) view).toggle();
                    else this.voo.setBoarding(true);
                }
                else
                {
                    this.voo.setBoarding(false);
                }
                break;
        }

        FileIO.removeFile(MainActivity.FILE_VOO, this);
        FileIO.serializeObject(MainActivity.FILE_VOO, voo, this);
    }

    public boolean parseVooFile()
    {
        if(FileIO.fileExists(MainActivity.FILE_VOO, this))
        {
            //Ler voo do ficheiro
            this.voo = FileIO.deserializeVooObject(MainActivity.FILE_VOO, this);
            return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.footer, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.abc_slide_out_bottom);
    }
}
