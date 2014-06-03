package com.moksie.onthemove.objects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by belh0 on 24-05-2014.
 */
public class FlightSerializable implements Serializable
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

    private long terminal;
    private Date checkininicio;
    private Date checkinfim;
    private long portaembarque;
    private Date embarque;
    private long tapetebagagem;
    private Date bagagem;
    private long portadesembarque;
    private Date desembarque;

    private boolean tipovoo;

    private boolean airport = false;
    private boolean checkin = false;
    private boolean security = false;
    private boolean boarding = false;

    public FlightSerializable(long id, long codigovoo, String codigocompanhia, String partidacidade,
                              String chegadacidade, long partidaaeroportoid, long chegadaaeroportoid,
                              Date partidatempoestimado, Date chegadatempoestimado, Date partidatemporeal,
                              Date chegadatemporeal, long terminal, Date checkininicio, Date checkinfim,
                              long portaembarque, Date embarque, long tapetebagagem, Date bagagem,
                              long portadesembarque, Date desembarque, boolean tipovoo) {
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
        this.terminal = terminal;
        this.checkininicio = checkininicio;
        this.checkinfim = checkinfim;
        this.portaembarque = portaembarque;
        this.embarque = embarque;
        this.tapetebagagem = tapetebagagem;
        this.bagagem = bagagem;
        this.portadesembarque = portadesembarque;
        this.desembarque = desembarque;
        this.tipovoo = tipovoo;
    }

    public Flight toParcelable()
    {
        return new Flight(this.id, this.codigovoo, this.codigocompanhia,
                this.partidacidade, this.chegadacidade, this.partidaaeroportoid,
                this.chegadaaeroportoid, this.partidatempoestimado, this.chegadatempoestimado,
                this.partidatemporeal, this.chegadatemporeal, this.terminal, this.checkininicio,
                this.checkinfim, this.portaembarque, this.embarque, this.tapetebagagem, this.bagagem,
                this.portadesembarque, this.desembarque, this.tipovoo);
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
