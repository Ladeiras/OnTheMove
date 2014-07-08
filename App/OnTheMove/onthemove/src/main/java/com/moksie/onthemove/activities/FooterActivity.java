package com.moksie.onthemove.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.FlightSerializable;
import com.moksie.onthemove.utilities.FileIO;

import java.text.SimpleDateFormat;

public class FooterActivity extends FragmentActivity {

    private FlightSerializable flight;
    private Context ctx = this;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition( R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        setContentView(R.layout.activity_footer);

        //Ler flight do ficheiro
        parseVooFile();

        TextView CodigoVoo = (TextView) this.findViewById(R.id.codigo_voo_textView);
        CodigoVoo.setText(String.valueOf(FooterFragment.flight.getCode()));

        TextView OrigemDestino = (TextView) this.findViewById(R.id.origem_destino_textView);
        OrigemDestino.setText(FooterFragment.flight.getDepartureairportcity()+" - "+FooterFragment.flight.getArrivalairportcity());

        TextView TempoEstimado = (TextView) this.findViewById(R.id.tempo_estimado_textView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.applyPattern("dd/MM HH:mm");

        if(FooterFragment.flight.isDeparture())
            TempoEstimado.setText(sdf.format(FooterFragment.flight.getDepartplannedtimeDate()));
        else
            TempoEstimado.setText(sdf.format(FooterFragment.flight.getArrivalplannedtimeDate()));


        LinearLayout cbLL = (LinearLayout) this.findViewById(R.id.checkboxesLinearLayout);
        if(!FooterFragment.flight.isDeparture()) {
            cbLL.setVisibility(View.GONE);
        }
        else {
            cbLL.setVisibility(View.VISIBLE);

            CheckBox airportCB = (CheckBox) findViewById(R.id.airport_checkBox);
            if (flight.isAirport() && !airportCB.isChecked()) {
                airportCB.toggle();
            }

            CheckBox checkinCB = (CheckBox) findViewById(R.id.checkin_checkBox);
            if (flight.isCheckin() && !checkinCB.isChecked()) {
                checkinCB.toggle();
            }

            CheckBox securityCB = (CheckBox) findViewById(R.id.security_checkBox);
            if (flight.isSecurity() && !securityCB.isChecked()) {
                securityCB.toggle();
            }

            CheckBox boardingCB = (CheckBox) findViewById(R.id.boarding_checkBox);
            if (flight.isBoarding() && !boardingCB.isChecked()) {
                boardingCB.toggle();
            }
        }

        //Botao Ver Info Voo
        final Button vooButton = (Button) findViewById(R.id.voo_seguido_button);
        vooButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FooterActivity.this, FlightInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("flight", flight.toParcelable());
                FooterActivity.this.startActivity(intent);
            }
        });

        //Botao Ver Promocoes
        final Button promotionsButton = (Button) findViewById(R.id.promotions_button);
        promotionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FooterActivity.this, StoreListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                String codeAirport;
                if(FooterFragment.flight.isDeparture())
                    codeAirport = FooterFragment.flight.getDepartureairportcode();
                else codeAirport = FooterFragment.flight.getArrivalairportcode();

                intent.putExtra("airport", codeAirport);
                intent.putExtra("option", "promo");
                FooterActivity.this.startActivity(intent);
            }
        });

        //Botao Nao Seguir Voo
        final Button unfollowButton = (Button) findViewById(R.id.unfollow_button);
        unfollowButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FileIO.removeFile(MainActivity.FILE_FLIGHT, ctx);
                CancelNotification(ctx, 01);
                onBackPressed();
            }
        });
    }

    public static void CancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Botao ajuda
        HeaderFragment.setMsg("Neste ecrã pode informar a aplicação dos passos que foram tomados até ao momento e receber notificações apropriadas para cada estado. Pode também a partir daqui consultar o voo seguido ou então as promoções existentes no aeroporto.");
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.airport_checkBox:
                if (checked)
                    this.flight.setAirport(true);
                else
                {
                    this.flight.setAirport(false);
                    this.flight.setCheckin(false);
                    this.flight.setSecurity(false);
                    this.flight.setBoarding(false);

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
                    if(!this.flight.isAirport())
                        ((CheckBox) view).toggle();
                    else this.flight.setCheckin(true);
                }
                else
                {
                    this.flight.setCheckin(false);
                    this.flight.setSecurity(false);
                    this.flight.setBoarding(false);

                    CheckBox security = (CheckBox) findViewById(R.id.security_checkBox);
                    if(security.isChecked()) {security.toggle();}

                    CheckBox boarding = (CheckBox) findViewById(R.id.boarding_checkBox);
                    if(boarding.isChecked()) {boarding.toggle();}
                }
                break;
            case R.id.security_checkBox:
                if(checked) {
                    if(!this.flight.isCheckin())
                        ((CheckBox) view).toggle();
                    else this.flight.setSecurity(true);
                }
                else
                {
                    this.flight.setSecurity(false);
                    this.flight.setBoarding(false);

                    CheckBox boarding = (CheckBox) findViewById(R.id.boarding_checkBox);
                    if(boarding.isChecked()) {boarding.toggle();}
                }
                break;
            case R.id.boarding_checkBox:
                if(checked) {
                    if(!this.flight.isSecurity())
                        ((CheckBox) view).toggle();
                    else this.flight.setBoarding(true);
                }
                else
                {
                    this.flight.setBoarding(false);
                }
                break;
        }

        FileIO.removeFile(MainActivity.FILE_FLIGHT, this);
        FileIO.serializeObject(MainActivity.FILE_FLIGHT, flight, this);
    }

    public boolean parseVooFile()
    {
        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler flight do ficheiro
            this.flight = FileIO.deserializeFlightObject(MainActivity.FILE_FLIGHT, this);
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
