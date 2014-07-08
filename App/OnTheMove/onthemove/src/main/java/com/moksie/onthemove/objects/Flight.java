package com.moksie.onthemove.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.moksie.onthemove.activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Flight implements Parcelable
{
    private String airlinecode;
    private String arrivalairportcode;
    private String arrivalairportcity;
    private String arrivalplannedtime;
    private String arrivalrealtime;
    private String boardingclosetime;
    private String boardingdoorcode;
    private long boardingduration;
    private String boardingopentime;
    private String checkincodes;
    private String checkinclosetime;
    private long checkinduration;
    private String checkinopentime;
    private String code;
    private String departureairportcode;
    private String departureairportcity;
    private String departplannedtime;
    private String departrealtime;
    private String luggageclosetime;
    private String luggageopentime;
    private long luggagepickavgduration;
    private String luggageplatforms;
    private String status;

    private boolean tipovoo;

    public Flight(String airlinecode, String arrivalairportcode, String arrivalairportcity,
                  String arrivalplannedtime, String arrivalrealtime, String boardingclosetime,
                  String boardingdoorcode, long boardingduration, String boardingopentime,
                  String checkincodes, String checkinclosetime, long checkinduration,
                  String checkinopentime, String code, String departureairportcode,
                  String departureairportcity, String departplannedtime, String departrealtime,
                  String luggageclosetime, String luggageopentime, long luggagepickavgduration,
                  String luggageplatforms, String status, boolean tipovoo) {
        this.airlinecode = airlinecode;
        this.arrivalairportcode = arrivalairportcode;
        this.arrivalairportcity = arrivalairportcity;
        this.arrivalplannedtime = arrivalplannedtime;
        this.arrivalrealtime = arrivalrealtime;
        this.boardingclosetime = boardingclosetime;
        this.boardingdoorcode = boardingdoorcode;
        this.boardingduration = boardingduration;
        this.boardingopentime = boardingopentime;
        this.checkincodes = checkincodes;
        this.checkinclosetime = checkinclosetime;
        this.checkinduration = checkinduration;
        this.checkinopentime = checkinopentime;
        this.code = code;
        this.departureairportcode = departureairportcode;
        this.departureairportcity = departureairportcity;
        this.departplannedtime = departplannedtime;
        this.departrealtime = departrealtime;
        this.luggageclosetime = luggageclosetime;
        this.luggageopentime = luggageopentime;
        this.luggagepickavgduration = luggagepickavgduration;
        this.luggageplatforms = luggageplatforms;
        this.status = status;
        this.tipovoo = tipovoo;
    }

    public Flight(Parcel in) {
        this.airlinecode = in.readString();
        this.arrivalairportcode = in.readString();
        this.arrivalairportcity = in.readString();
        this.arrivalplannedtime = in.readString();
        this.arrivalrealtime = in.readString();
        this.boardingclosetime = in.readString();
        this.boardingdoorcode = in.readString();
        this.boardingduration = in.readLong();
        this.boardingopentime = in.readString();
        this.checkincodes = in.readString();
        this.checkinclosetime = in.readString();
        this.checkinduration = in.readLong();
        this.checkinopentime = in.readString();
        this.code = in.readString();
        this.departureairportcode = in.readString();
        this.departureairportcity = in.readString();
        this.departplannedtime = in.readString();
        this.departrealtime = in.readString();
        this.luggageclosetime = in.readString();
        this.luggageopentime = in.readString();
        this.luggagepickavgduration = in.readLong();
        this.luggageplatforms = in.readString();
        this.status = in.readString();
        this.tipovoo = new Boolean(in.readString());
    }

    public String uTimeToString(String uTime)
    {
        String split1[] = uTime.split("\\(");
        String split2[] = split1[1].split("\\+");
        uTime = split2[0];

        long time = Long.parseLong(uTime);
        Date date = new Date(time);
        SimpleDateFormat format = MainActivity.sdf;
        format.setTimeZone(TimeZone.getDefault());

        return format.format(date);
    }

    public Date uTimeToDate(String uTime)
    {
        String split1[] = uTime.split("\\(");
        String split2[] = split1[1].split("\\+");
        uTime = split2[0];

        long time = Long.parseLong(uTime);
        Date date = new Date(time);

        return date;
    }

    public String getAirlinecode() {
        return airlinecode;
    }

    public String getArrivalairportcode() {
        return arrivalairportcode;
    }

    public String getArrivalairportcity() {
        return arrivalairportcity;
    }

    public String getArrivalplannedtime() {
        return arrivalplannedtime;
    }

    public String getArrivalplannedtimeStr() {
        return uTimeToString(arrivalplannedtime);
    }

    public Date getArrivalplannedtimeDate() {
        return uTimeToDate(arrivalplannedtime);
    }

    public String getArrivalrealtime() {
        return arrivalrealtime;
    }

    public String getBoardingclosetime() {
        return boardingclosetime;
    }

    public String getBoardingdoorcode() {
        return boardingdoorcode;
    }

    public long getBoardingduration() {
        return boardingduration;
    }

    public String getBoardingopentime() {
        return boardingopentime;
    }

    public Date getBoardingopentimeDate() {
        return uTimeToDate(boardingopentime);
    }

    public String getCheckincodes() {
        return checkincodes;
    }

    public String getCheckinclosetime() {
        return checkinclosetime;
    }

    public Date getCheckinclosetimeDate() {
        return uTimeToDate(checkinclosetime);
    }

    public long getCheckinduration() {
        return checkinduration;
    }

    public String getCheckinopentime() {
        return checkinopentime;
    }

    public Date getCheckinopentimeDate() {
        return uTimeToDate(checkinopentime);
    }

    public String getCode() {
        return code;
    }

    public String getDepartureairportcode() {
        return departureairportcode;
    }

    public String getDepartureairportcity() {
        return departureairportcity;
    }

    public String getDepartplannedtime() {
        return departplannedtime;
    }

    public String getDepartplannedtimeStr() {
        return uTimeToString(departplannedtime);
    }

    public Date getDepartplannedtimeDate() {
        return uTimeToDate(departplannedtime);
    }

    public String getDepartrealtime() {
        return departrealtime;
    }

    public String getLuggageclosetime() {
        return luggageclosetime;
    }

    public String getLuggageopentime() {
        return luggageopentime;
    }

    public Date getLuggageopentimeDate() {
        return uTimeToDate(luggageopentime);
    }

    public long getLuggagepickavgduration() {
        return luggagepickavgduration;
    }

    public String getLuggageplatforms() {
        return luggageplatforms;
    }

    public String getStatus() {
        return status;
    }

    public boolean isDeparture() {
        return tipovoo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(airlinecode);
        parcel.writeString(arrivalairportcode);
        parcel.writeString(arrivalairportcity);
        parcel.writeString(arrivalplannedtime);
        parcel.writeString(arrivalrealtime);
        parcel.writeString(boardingclosetime);
        parcel.writeString(boardingdoorcode);
        parcel.writeLong(boardingduration);
        parcel.writeString(boardingopentime);
        parcel.writeString(checkincodes);
        parcel.writeString(checkinclosetime);
        parcel.writeLong(checkinduration);
        parcel.writeString(checkinopentime);
        parcel.writeString(code);
        parcel.writeString(departureairportcode);
        parcel.writeString(departureairportcity);
        parcel.writeString(departplannedtime);
        parcel.writeString(departrealtime);
        parcel.writeString(luggageclosetime);
        parcel.writeString(luggageopentime);
        parcel.writeLong(luggagepickavgduration);
        parcel.writeString(luggageplatforms);
        parcel.writeString(status);

        parcel.writeString(String.valueOf(tipovoo));
    }

    public static final Creator<Flight> CREATOR = new Creator<Flight>() {
        public Flight createFromParcel(Parcel in) {
            return new Flight(in);
        }

        public Flight[] newArray(int size) {
            return new Flight[size];
        }
    };

    public FlightSerializable toSerializable()
    {
        return new FlightSerializable(
            this.airlinecode,
            this.arrivalairportcode,
            this.arrivalairportcity,
            this.arrivalplannedtime,
            this.arrivalrealtime,
            this.boardingclosetime,
            this.boardingdoorcode,
            this.boardingduration,
            this.boardingopentime,
            this.checkincodes,
            this.checkinclosetime,
            this.checkinduration,
            this.checkinopentime,
            this.code,
            this.departureairportcode,
            this.departureairportcity,
            this.departplannedtime,
            this.departrealtime,
            this.luggageclosetime,
            this.luggageopentime,
            this.luggagepickavgduration,
            this.luggageplatforms,
            this.status,
            this.tipovoo);
    }
}
