package com.moksie.onthemove.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by belh0 on 14-04-2014.
 */
public class Contact
{
    private long id;
    private long idaeroporto;
    private String titulo;
    private String website;
    private String mapaurl;
    private String descricao;
    private ArrayList<Long> contactos;

    public Contact(long id, long idaeroporto, String titulo, String website, String mapaurl, String descricao, ArrayList<Long> contactos) {
        this.id = id;
        this.idaeroporto = idaeroporto;
        this.titulo = titulo;
        this.website = website;
        this.mapaurl = mapaurl;
        this.descricao = descricao;
        this.contactos = contactos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdaeroporto() {
        return idaeroporto;
    }

    public void setIdaeroporto(long idaeroporto) {
        this.idaeroporto = idaeroporto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMapaurl() {
        return mapaurl;
    }

    public void setMapaurl(String mapaurl) {
        this.mapaurl = mapaurl;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ArrayList<Long> getContactos() {
        return contactos;
    }

    public void setContactos(ArrayList<Long> contactos) {
        this.contactos = contactos;
    }
}
