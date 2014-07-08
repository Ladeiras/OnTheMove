package com.moksie.onthemove.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moksie.onthemove.R;
import com.moksie.onthemove.fragments.FooterFragment;
import com.moksie.onthemove.fragments.HeaderFragment;
import com.moksie.onthemove.objects.Contact;
import com.moksie.onthemove.objects.Flight;
import com.moksie.onthemove.utilities.FileIO;

/**
 * Nesta atividade é passado como parametro um objecto Contact que contém os vários contactos de uma
 * entidade.
 * Sendo os contactos mostrados em vários botões, pode ser feita uma chamada no caso de um número de
 * telefone, enviado um email ou aceder a uma página web.
 * O objecto Contact poderá conter apenas um dos tipos de contacto.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class ContactActivity extends FragmentActivity {

    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Intent intent = getIntent();
        contact = intent.getParcelableExtra("contact");//Parametro Contact

        //Texto do Botao Titulo
        TextView Title = (TextView) this.findViewById(R.id.service_name);
        Title.setText(contact.getName());

        //Texto do Botao Email
        TextView WebmailTV = (TextView) this.findViewById(R.id.service_webmail_textView);
        WebmailTV.setText(contact.getEmail());

        //Texto do Botao Website
        TextView WebSiteTV = (TextView) this.findViewById(R.id.service_website_textView);
        WebSiteTV.setText(contact.getWebsite());

        //Texto do Botao Telefone
        TextView PhoneTV = (TextView) this.findViewById(R.id.service_phone_textView);
        PhoneTV.setText(contact.getTelef());

        if(contact != null) {
            //Botao Website (Clickable LinearLayout)
            LinearLayout websiteLL = (LinearLayout) findViewById(R.id.website_LinearLayout);
            if(!contact.getWebsite().equals("null")) {
                websiteLL.setVisibility(View.VISIBLE);
                websiteLL.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String url = contact.getWebsite();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
            }
            else websiteLL.setVisibility(View.GONE);

            //Botao Email (Clickable LinearLayout)
            LinearLayout webmailLL = (LinearLayout) findViewById(R.id.webmail_LinearLayout);
            if(!contact.getEmail().equals("null")) {
                webmailLL.setVisibility(View.VISIBLE);
                webmailLL.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{contact.getEmail()});
                        //i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                        //i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                        try {
                            startActivity(Intent.createChooser(i, "Enviar mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(ContactActivity.this, "Não existem clientes de email instalados", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else webmailLL.setVisibility(View.GONE);

            //Botao Telefone (Clickable LinearLayout)
            LinearLayout phonelLL = (LinearLayout) findViewById(R.id.phone_LinearLayout);
            if(!contact.getTelef().equals("null")) {
                phonelLL.setVisibility(View.VISIBLE);
                phonelLL.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String number = contact.getTelef();
                        String split[] = number.split("\\+");
                        number = split[1];
                        number.replaceAll("\\s","");
                        number = "tel:00"+number;

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(number));
                        startActivity(intent);
                    }
                });
            }
            else phonelLL.setVisibility(View.GONE);

            //Botao Facebook (Clickable LinearLayout)
            LinearLayout facebookLL = (LinearLayout) findViewById(R.id.facebook_LinearLayout);
            if(!contact.getFacebook().equals("null")) {
                facebookLL.setVisibility(View.VISIBLE);
                facebookLL.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String url = contact.getFacebook();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
            }
            else facebookLL.setVisibility(View.GONE);

            //Botao Twitter (Clickable LinearLayout)
            LinearLayout twitterLL = (LinearLayout) findViewById(R.id.twitter_LinearLayout);
            if(!contact.getTwitter().equals("null")) {
                twitterLL.setVisibility(View.VISIBLE);
                twitterLL.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String url = contact.getTwitter();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
            }
            else twitterLL.setVisibility(View.GONE);
        }
        else
        {
            //Mensagem de erro no caso de o objecto Contact estar vazio
            Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        //Fragments
        updateFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Botao ajuda
        HeaderFragment.setMsg("Neste ecrã poderá consultar os contactos do Aeroporto");

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

    /**
     * Função de atualização de todos os Fragments desta vista
     */
    public void updateFragments()
    {
        updateFooter();
    }

    /**
     * Função de atualização do Fragment Footer que corresponde ao voo que está a ser seguido.
     * Nesta função também são atualizados os tamanhos dos restantes elementos da vista caso o
     * fragment exista ou não.
     */
    public void updateFooter()
    {
        LinearLayout layout = (LinearLayout) findViewById(R.id.service_LinearLayout);
        FooterFragment footer = (FooterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.service_footer);

        if(FileIO.fileExists(MainActivity.FILE_FLIGHT, this))
        {
            //Ler voo do ficheiro
            Flight tempFlight = FileIO.deserializeFlightObject(MainActivity.FILE_FLIGHT, this).toParcelable();

            FooterFragment.setVisibility(true);
            FooterFragment.setFlight(tempFlight);
            FooterFragment.updateFlight(this);
            FooterFragment.setVisibility(true);

            layout.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,9f)
            );
        }
        else
        {
            FooterFragment.setVisibility(false);
            layout.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,10f)
            );
        }

        footer.updateVisibility();
    }
}
