package com.moksie.onthemove.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by belh0 on 14-04-2014.
 */
public class Location implements Parcelable
{
    private String code;
    private String keywords;
    private String lang;
    private String name;
    private long posx;
    private long posy;
    private String type;

    public Location(String code, String keywords, String lang, String name, long posx, long posy,
                    String type) {
        this.code = code;
        this.keywords = keywords;
        this.lang = lang;
        this.name = name;
        this.posx = posx;
        this.posy = posy;
        this.type = type;
    }

    public Location(Parcel in) {
        this.code = in.readString();
        this.keywords = in.readString();
        this.lang = in.readString();
        this.name = in.readString();
        this.posx = in.readLong();
        this.posy = in.readLong();
        this.type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(code);
        parcel.writeString(keywords);
        parcel.writeString(lang);
        parcel.writeString(name);
        parcel.writeLong(posx);
        parcel.writeLong(posy);
        parcel.writeString(type);
    }

    public String getCode() {
        return code;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getLang() {
        return lang;
    }

    public String getName() {
        return name;
    }

    public long getPosx() {
        return posx;
    }

    public long getPosy() {
        return posy;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator()
    {
        public Location createFromParcel(Parcel in) { return new Location(in); }
        public Location[] newArray(int size) { return new Location[size]; }
    };
}
