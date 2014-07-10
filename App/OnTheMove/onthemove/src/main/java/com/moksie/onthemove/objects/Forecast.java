package com.moksie.onthemove.objects;

/**
 * Classe Forecast
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class Forecast {

    private static final boolean DEPARTURE = true;
    private static final boolean ARRIVAL = false;

    private boolean type;
    private String code;
    private String status;
    private String departure;
    private String arrival;

    public Forecast(boolean type, String code, String status, String departure, String arrival) {
        this.type = type;
        this.code = code;
        this.status = status;
        this.departure = departure;
        this.arrival = arrival;
    }

    public boolean isDeparture() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }
}
