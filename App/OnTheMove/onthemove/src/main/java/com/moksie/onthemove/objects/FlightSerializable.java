package com.moksie.onthemove.objects;

import com.moksie.onthemove.activities.MainActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FlightSerializable implements Serializable
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

    private boolean airport = false;
    private boolean checkin = false;
    private boolean security = false;
    private boolean boarding = false;

    public FlightSerializable(String airlinecode, String arrivalairportcode,
                              String arrivalairportcity, String arrivalplannedtime,
                              String arrivalrealtime, String boardingclosetime,
                              String boardingdoorcode, long boardingduration,
                              String boardingopentime, String checkincodes,
                              String checkinclosetime, long checkinduration,
                              String checkinopentime, String code, String departureairportcode,
                              String departureairportcity, String departplannedtime,
                              String departrealtime, String luggageclosetime,
                              String luggageopentime, long luggagepickavgduration,
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

    public Flight toParcelable()
    {
        return new Flight(
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

    public void setAirport(boolean airport) {
        this.airport = airport;
    }

    public void setCheckin(boolean checkin) {
        this.checkin = checkin;
    }

    public void setSecurity(boolean security) {
        this.security = security;
    }

    public void setBoarding(boolean boarding) {
        this.boarding = boarding;
    }

    public boolean isAirport() {
        return airport;
    }

    public boolean isCheckin() {
        return checkin;
    }

    public boolean isSecurity() {
        return security;
    }

    public boolean isBoarding() {
        return boarding;
    }
}
