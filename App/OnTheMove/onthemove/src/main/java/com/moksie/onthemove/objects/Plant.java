package com.moksie.onthemove.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Classe Plant
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class Plant implements Parcelable
{
    private String code;
    private String imagetype;
    private String imageurl;
    private String lang;
    private long length;
    private String name;
    private String obs;
    private long posx;
    private long posy;
    private long posz;
    private String version;
    private long width;

    public Plant(String code, String imagetype, String imageurl, String lang, long length,
                 String name, String obs, long posx, long posy, long posz, String version,
                 long width) {
        this.code = code;
        this.imagetype = imagetype;
        this.imageurl = imageurl;
        this.lang = lang;
        this.length = length;
        this.name = name;
        this.obs = obs;
        this.posx = posx;
        this.posy = posy;
        this.posz = posz;
        this.version = version;
        this.width = width;
    }

    public Plant(Parcel in) {
        this.code = in.readString();
        this.imagetype = in.readString();
        this.imageurl = in.readString();
        this.lang = in.readString();
        this.length = in.readLong();
        this.name = in.readString();
        this.obs = in.readString();
        this.posx = in.readLong();
        this.posy = in.readLong();
        this.posz = in.readLong();
        this.version = in.readString();
        this.width = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(code);
        parcel.writeString(imagetype);
        parcel.writeString(imageurl);
        parcel.writeString(lang);
        parcel.writeLong(length);
        parcel.writeString(name);
        parcel.writeString(obs);
        parcel.writeLong(posx);
        parcel.writeLong(posy);
        parcel.writeLong(posz);
        parcel.writeString(version);
        parcel.writeLong(width);
    }

    public String getCode() {
        return code;
    }

    public String getImagetype() {
        return imagetype;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getLang() {
        return lang;
    }

    public long getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public String getObs() {
        return obs;
    }

    public long getPosx() {
        return posx;
    }

    public long getPosy() {
        return posy;
    }

    public long getPosz() {
        return posz;
    }

    public String getVersion() {
        return version;
    }

    public long getWidth() {
        return width;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator()
    {
        public Plant createFromParcel(Parcel in) { return new Plant(in); }
        public Plant[] newArray(int size) { return new Plant[size]; }
    };
}
