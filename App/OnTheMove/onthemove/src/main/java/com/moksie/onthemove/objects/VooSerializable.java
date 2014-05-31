package com.moksie.onthemove.objects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by belh0 on 24-05-2014.
 */
public class VooSerializable implements Serializable
{
    private long id;
    private long codigovoo;
    private String codigocompanhia;
    private String partidacidade;
    private String chegadacidade;
    private long partidaaeroportoid;
    private long chegadaaeroportoid;
    private Date partidatempoestimado;
    private Date chegadatempoestimado;
    private Date partidatemporeal;
    private Date chegadatemporeal;
    private boolean tipovoo;

    private boolean airport = false;
    private boolean checkin = false;
    private boolean security = false;
    private boolean boarding = false;

    public VooSerializable(long id, long codigovoo, String codigocompanhia, String partidacidade,
                           String chegadacidade, long partidaaeroportoid, long chegadaaeroportoid,
                           Date partidatempoestimado, Date chegadatempoestimado,
                           Date partidatemporeal, Date chegadatemporeal, boolean tipovoo) {
        this.id = id;
        this.codigovoo = codigovoo;
        this.codigocompanhia = codigocompanhia;
        this.partidacidade = partidacidade;
        this.chegadacidade = chegadacidade;
        this.partidaaeroportoid = partidaaeroportoid;
        this.chegadaaeroportoid = chegadaaeroportoid;
        this.partidatempoestimado = partidatempoestimado;
        this.chegadatempoestimado = chegadatempoestimado;
        this.partidatemporeal = partidatemporeal;
        this.chegadatemporeal = chegadatemporeal;
        this.tipovoo = tipovoo;
    }

    public Voo toParcelable()
    {
        return new Voo(this.id, this.codigovoo, this.codigocompanhia,
                this.partidacidade, this.chegadacidade, this.partidaaeroportoid,
                this.chegadaaeroportoid, this.partidatempoestimado, this.chegadatempoestimado,
                this.partidatemporeal, this.chegadatemporeal, this.tipovoo);
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
