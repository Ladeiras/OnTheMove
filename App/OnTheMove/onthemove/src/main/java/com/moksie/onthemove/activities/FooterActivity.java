package com.moksie.onthemove.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;

import java.text.SimpleDateFormat;

public class FooterActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition( R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        setContentView(R.layout.activity_footer);

        TextView CodigoVoo = (TextView) this.findViewById(R.id.codigo_voo_textView);
        CodigoVoo.setText(String.valueOf(FooterFragment.voo.getCodigovoo()));

        TextView OrigemDestino = (TextView) this.findViewById(R.id.origem_destino_textView);
        OrigemDestino.setText(FooterFragment.voo.getPartidacidade()+" - "+FooterFragment.voo.getChegadacidade());

        TextView TempoEstimado = (TextView) this.findViewById(R.id.tempo_estimado_textView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.applyPattern("dd/MM hh:mm");
        TempoEstimado.setText(sdf.format(FooterFragment.voo.getPartidatempoestimado()));
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
