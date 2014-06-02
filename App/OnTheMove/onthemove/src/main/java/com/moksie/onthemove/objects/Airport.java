package com.moksie.onthemove.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by belh0 on 14-04-2014.
 */
public class Airport implements Parcelable
{
    private long id;
    private String pais;
    private String cidade;
    private String nome;
    private double latitude;
    private double longitude;

    public Airport(long id, String pais, String cidade, String nome, double latitude, double longitude) {
        this.id = id;
        this.pais = pais;
        this.cidade = cidade;
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Airport(Parcel in) {
        this.id = in.readInt();
        this.pais = in.readString();
        this.cidade = in.readString();
        this.nome = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public long getId() {
        return id;
    }

    public String getPais() {
        return pais;
    }

    public String getCidade() {
        return cidade;
    }

    public String getNome() {
        return nome;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(pais);
        parcel.writeString(cidade);
        parcel.writeString(nome);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    public static final Parcelable.Creator<Airport> CREATOR = new Parcelable.Creator<Airport>() {
        public Airport createFromParcel(Parcel in) {
            return new Airport(in);
        }

        public Airport[] newArray(int size) {
            return new Airport[size];
        }
    };
}
