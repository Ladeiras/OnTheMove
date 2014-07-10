package com.moksie.onthemove.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Classe Parcelable Contact
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class Contact implements Parcelable
{
    private String name;
    private String email;
    private String facebook;
    private String telef;
    private String twitter;
    private String website;
    private String logourl;

    public Contact(String name, String email, String facebook, String telef, String twitter,
                   String website, String logourl) {
        this.name = name;
        this.email = email;
        this.facebook = facebook;
        this.telef = telef;
        this.twitter = twitter;
        this.website = website;
        this.logourl = logourl;
    }

    public Contact(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.facebook = in.readString();
        this.telef = in.readString();
        this.twitter = in.readString();
        this.website = in.readString();
        this.logourl = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTelef() {
        return telef;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getWebsite() {
        return website;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setTelef(String telef) {
        this.telef = telef;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(facebook);
        parcel.writeString(telef);
        parcel.writeString(twitter);
        parcel.writeString(website);
        parcel.writeString(logourl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
