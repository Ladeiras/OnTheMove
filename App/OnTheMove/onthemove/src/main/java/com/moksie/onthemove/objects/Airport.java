package com.moksie.onthemove.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Airport implements Parcelable
{
    private String code;
    private String city;
    private String country;
    private long flightAdvisedPreparationTime;
    private double lat;
    private long leaveDuration;
    private double lon;
    private String name;
    private long safetyCheckAverageDuration;
    private String timezone;
    private long toBoardingDuration;
    private long toCheckinDuration;
    private long toLuggageDuration;
    private long toSafetyCheckDuration;
    private Contact contact;

    public Airport(String code, String city, String country, String email,
                   String facebook, long flightAdvisedPreparationTime, double lat,
                   long leaveDuration, double lon, String name, long safetyCheckAverageDuration,
                   String telef, String timezone, long toBoardingDuration, long toCheckinDuration,
                   long toLuggageDuration, long toSafetyCheckDuration, String twitter,
                   String website) {
        this.code = code;
        this.city = city;
        this.country = country;
        this.flightAdvisedPreparationTime = flightAdvisedPreparationTime;
        this.lat = lat;
        this.leaveDuration = leaveDuration;
        this.lon = lon;
        this.name = name;
        this.safetyCheckAverageDuration = safetyCheckAverageDuration;
        this.timezone = timezone;
        this.toBoardingDuration = toBoardingDuration;
        this.toCheckinDuration = toCheckinDuration;
        this.toLuggageDuration = toLuggageDuration;
        this.toSafetyCheckDuration = toSafetyCheckDuration;
        this.contact = new Contact("Aeroporto", email, facebook, telef, twitter, website, "");
    }

    public Airport(Parcel in) {
        this.code = in.readString();
        this.city = in.readString();
        this.country = in.readString();
        String email = in.readString();
        String facebook = in.readString();
        this.flightAdvisedPreparationTime = in.readLong();
        this.lat = in.readDouble();
        this.leaveDuration = in.readLong();
        this.lon = in.readDouble();
        this.name = in.readString();
        this.safetyCheckAverageDuration = in.readLong();
        String telef = in.readString();
        this.timezone = in.readString();
        this.toBoardingDuration = in.readLong();
        this.toCheckinDuration = in.readLong();
        this.toLuggageDuration = in.readLong();
        this.toSafetyCheckDuration = in.readLong();
        String twitter = in.readString();
        String website = in.readString();
        this.contact = new Contact("Aeroporto", email, facebook, telef, twitter, website, "");
    }

    public String getCode() {
        return code;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public long getFlightAdvisedPreparationTime() {
        return flightAdvisedPreparationTime;
    }

    public double getLat() {
        return lat;
    }

    public long getLeaveDuration() {
        return leaveDuration;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public long getSafetyCheckAverageDuration() {
        return safetyCheckAverageDuration;
    }

    public String getTimezone() {
        return timezone;
    }

    public long getToBoardingDuration() {
        return toBoardingDuration;
    }

    public long getToCheckinDuration() {
        return toCheckinDuration;
    }

    public long getToLuggageDuration() {
        return toLuggageDuration;
    }

    public long getToSafetyCheckDuration() {
        return toSafetyCheckDuration;
    }

    public Contact getContact() {
        return contact;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(city);
        parcel.writeString(country);
        parcel.writeString(contact.getEmail());
        parcel.writeString(contact.getFacebook());
        parcel.writeLong(flightAdvisedPreparationTime);
        parcel.writeDouble(lat);
        parcel.writeLong(leaveDuration);
        parcel.writeDouble(lon);
        parcel.writeString(name);
        parcel.writeLong(safetyCheckAverageDuration);
        parcel.writeString(contact.getTelef());
        parcel.writeString(timezone);
        parcel.writeLong(toBoardingDuration);
        parcel.writeLong(toCheckinDuration);
        parcel.writeLong(toLuggageDuration);
        parcel.writeLong(toSafetyCheckDuration);
        parcel.writeString(contact.getTwitter());
        parcel.writeString(contact.getWebsite());
    }

    public static final Creator<Airport> CREATOR = new Creator<Airport>() {
        public Airport createFromParcel(Parcel in) {
            return new Airport(in);
        }

        public Airport[] newArray(int size) {
            return new Airport[size];
        }
    };
}
