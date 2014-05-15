package com.moksie.onthemove.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by belh0 on 14-04-2014.
 */
public class Voo implements Parcelable
{
    private long id;
    private long codigovoo;
    private String codigocompanhia;
    private String partidacidade;
    private String chegadacidade;
    private Date partidatempoestimado;
    private Date chegadatempoestimado;
    private Date partidatemporeal;
    private Date chegadatemporeal;

    public Voo(long id, long codigovoo, String codigocompanhia, String partidacidade,
               String chegadacidade, Date partidatempoestimado, Date chegadatempoestimado,
               Date partidatemporeal, Date chegadatemporeal) {
        this.id = id;
        this.codigovoo = codigovoo;
        this.codigocompanhia = codigocompanhia;
        this.partidacidade = partidacidade;
        this.chegadacidade = chegadacidade;
        this.partidatempoestimado = partidatempoestimado;
        this.chegadatempoestimado = chegadatempoestimado;
        this.partidatemporeal = partidatemporeal;
        this.chegadatemporeal = chegadatemporeal;
    }

    public Voo(String str)
    {
        String list[] = str.split(",");
        this.id = Long.parseLong(list[0]);
        this.codigovoo = Long.parseLong(list[1]);
        this.codigocompanhia = list[2];
        this.partidacidade = list[3];
        this.chegadacidade = list[4];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            this.partidatempoestimado = sdf.parse(list[5]);
            this.chegadatempoestimado = sdf.parse(list[6]);
            this.partidatemporeal = sdf.parse(list[7]);
            this.chegadatemporeal = sdf.parse(list[8]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Voo(Parcel in) {
        this.id = in.readLong();
        this.codigovoo = in.readLong();
        this.codigocompanhia = in.readString();
        this.partidacidade = in.readString();
        this.chegadacidade = in.readString();
        this.partidatempoestimado = new Date(in.readLong());
        this.chegadatempoestimado = new Date(in.readLong());
        this.partidatemporeal = new Date(in.readLong());
        this.chegadatemporeal = new Date(in.readLong());
    }

    public long getId() {
        return id;
    }

    public long getCodigovoo() {
        return codigovoo;
    }

    public String getCodigocompanhia() {
        return codigocompanhia;
    }

    public String getPartidacidade() {
        return partidacidade;
    }

    public String getChegadacidade() {
        return chegadacidade;
    }

    public Date getPartidatempoestimado() {
        return partidatempoestimado;
    }

    public Date getChegadatempoestimado() {
        return chegadatempoestimado;
    }

    public Date getPartidatemporeal() {
        return partidatemporeal;
    }

    public Date getChegadatemporeal() {
        return chegadatemporeal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(codigovoo);
        parcel.writeString(codigocompanhia);
        parcel.writeString(partidacidade);
        parcel.writeString(chegadacidade);
        parcel.writeLong(partidatempoestimado.getTime());
        parcel.writeLong(chegadatempoestimado.getTime());
        parcel.writeLong(partidatemporeal.getTime());
        parcel.writeLong(chegadatemporeal.getTime());
    }

    public static final Parcelable.Creator<Voo> CREATOR = new Parcelable.Creator<Voo>() {
        public Voo createFromParcel(Parcel in) {
            return new Voo(in);
        }

        public Voo[] newArray(int size) {
            return new Voo[size];
        }
    };

    @Override
    public String toString() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        return this.getId()+","+
                this.getCodigovoo()+","+
                this.getCodigocompanhia()+","+
                this.getPartidacidade()+","+
                this.getChegadacidade()+","+
                sdf.format(this.getPartidatempoestimado())+","+
                sdf.format(this.getChegadatempoestimado())+","+
                sdf.format(this.getPartidatemporeal())+","+
                sdf.format(this.getChegadatemporeal());
    }
}
