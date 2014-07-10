package com.moksie.onthemove.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Classe Parcelable Campaign
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class Campaign implements Parcelable
{
    private String campaigndesc;
    private String imageurl;
    private String shopcode;

    public Campaign(String campaigndesc, String imageurl, String shopcode) {
        this.campaigndesc = campaigndesc;
        this.imageurl = imageurl;
        this.shopcode = shopcode;
    }

    public Campaign(Parcel in) {
        this.campaigndesc = in.readString();
        this.imageurl = in.readString();
        this.shopcode = in.readString();
    }

    public String getCampaigndesc() {
        return campaigndesc;
    }

    public void setCampaigndesc(String campaigndesc) {
        this.campaigndesc = campaigndesc;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getShopcode() {
        return shopcode;
    }

    public void setShopcode(String shopcode) {
        this.shopcode = shopcode;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(campaigndesc);
        parcel.writeString(imageurl);
        parcel.writeString(shopcode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Campaign> CREATOR = new Creator<Campaign>() {
        public Campaign createFromParcel(Parcel in) {
            return new Campaign(in);
        }

        public Campaign[] newArray(int size) {
            return new Campaign[size];
        }
    };
}
