package com.moksie.onthemove.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.objects.Airport;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.utilities.FileIO;

public class ContactsMenuActivity extends FragmentActivity {

    private Airport airport;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_menu);

        Intent intent = getIntent();
        airport = (Airport) intent.getParcelableExtra("airport");

        //Botao Contactos aeroporto
        final Button CAButton = (Button) findViewById(R.id.contacts_aeroporto);
        CAButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactsMenuActivity.this, ServiceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("service", "aeroporto");
                intent.putExtra("airport", airport);
                ContactsMenuActivity.this.startActivity(intent);
            }
        });

        //Botao Perdidos e Achados
        final Button PAButton = (Button) findViewById(R.id.contacts_perdidos_achados);
        PAButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactsMenuActivity.this, ServiceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("service", "perdidosachados");
                intent.putExtra("airport", airport);
                ContactsMenuActivity.this.startActivity(intent);
            }
        });

        //Botao Posto Socorro
        final Button PSButton = (Button) findViewById(R.id.contacts_posto_socorro);
        PSButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactsMenuActivity.this, ServiceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("service", "postosocorro");
                intent.putExtra("airport", airport);
                ContactsMenuActivity.this.startActivity(intent);
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
                .findFragmentById(R.id.contacts_footer);

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
}
