package com.moksie.onthemove.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by belh0 on 14-04-2014.
 */
public class Map implements Parcelable
{
    private String location;
    private String url;

    public Map(String location, String url) {
        this.location = location;
        this.url = url;
    }

    public Map(Parcel in) {
        this.location = in.readString();
        this.url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(location);
        parcel.writeString(url);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public Map createFromParcel(Parcel in) { return new Map(in); }
        public Map[] newArray(int size) { return new Map[size]; }
    };
}
