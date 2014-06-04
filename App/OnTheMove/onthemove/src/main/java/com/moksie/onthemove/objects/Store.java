package com.moksie.onthemove.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by belh0 on 14-04-2014.
 */
public class Store implements Parcelable
{
    private long id;
    private String categoria;
    private String nome;
    private String imagemurl;
    private String mapaurl;
    private String webmail;
    private String website;
    private long telefone;
    private String descricao;
    private boolean compromocao;
    private String promocao;

    public Store(long id, String categoria, String nome, String imagemurl,
                 String mapaurl, String webmail, String website, long telefone, String descricao,
                 boolean compromocao, String promocao) {
        this.id = id;
        this.categoria = categoria;
        this.nome = nome;
        this.imagemurl = imagemurl;
        this.mapaurl = mapaurl;
        this.webmail = webmail;
        this.website = website;
        this.telefone = telefone;
        this.descricao = descricao;
        this.compromocao = compromocao;
        this.promocao = promocao;
    }

    public Store(Parcel in) {
        this.id = in.readLong();
        this.categoria = in.readString();
        this.nome = in.readString();
        this.imagemurl = in.readString();
        this.mapaurl = in.readString();
        this.webmail = in.readString();
        this.website = in.readString();
        this.telefone = in.readLong();
        this.descricao = in.readString();
        this.compromocao = in.readByte() != 0;
        this.promocao = in.readString();
    }

    public long getId() {
        return id;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getNome() {
        return nome;
    }

    public String getImagemurl() {
        return imagemurl;
    }

    public String getMapaurl() {
        return mapaurl;
    }

    public String getWebmail() {
        return webmail;
    }

    public String getWebsite() {
        return website;
    }

    public long getTelefone() {
        return telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isCompromocao() {
        return compromocao;
    }

    public String getPromocao() {
        return promocao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(categoria);
        parcel.writeString(nome);
        parcel.writeString(imagemurl);
        parcel.writeString(mapaurl);
        parcel.writeString(webmail);
        parcel.writeString(website);
        parcel.writeLong(telefone);
        parcel.writeString(descricao);
        parcel.writeByte((byte) (compromocao ? 1 : 0));
        parcel.writeString(promocao);
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        public Store[] newArray(int size) {
            return new Store[size];
        }
    };
}
