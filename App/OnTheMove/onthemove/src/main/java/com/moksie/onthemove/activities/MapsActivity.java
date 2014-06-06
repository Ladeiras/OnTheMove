package com.moksie.onthemove.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moksie.onthemove.R;
import com.moksie.onthemove.adapters.MapsListAdapter;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.objects.Map;
import com.moksie.onthemove.utilities.FileIO;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {

    private ArrayList<Map> maps = new ArrayList<Map>();
    private String title;
    private ListView mapsList;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        maps = intent.getExtras().getParcelableArrayList("maps");
        title = intent.getStringExtra("title");

        MapsListAdapter mapsListAdapter = new MapsListAdapter(this, android.R.layout.simple_expandable_list_item_1, maps);
        mapsList = (ListView) findViewById(R.id.service_maps_listView);

        mapsList.setAdapter(mapsListAdapter);

        TextView titleTV = (TextView) findViewById(R.id.service_name);
        titleTV.setText(title);

        ListView list = (ListView) findViewById(R.id.service_maps_listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l) {
                Intent intent = new Intent(MapsActivity.this, MapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                Map map = (Map) mapsList.getAdapter().getItem(i);
                intent.putExtra("title", title);
                intent.putExtra("url", map.getUrl());
                MapsActivity.this.startActivity(intent);
            }
        });

        updateFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();

        HeaderFragment.setMsg("Neste ecrã pode escolher uma das plantas onde este serviço é disponibilizado");

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
                .findFragmentById(R.id.maps_footer);

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
