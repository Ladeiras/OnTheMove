package com.moksie.onthemove.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by belh0 on 14-04-2014.
 */
public class Flight implements Parcelable
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

    public Flight(long id, long codigovoo, String codigocompanhia, String partidacidade,
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

    public Flight(String str)
    {
        String list[] = str.split(",");
        this.id = Long.parseLong(list[0]);
        this.codigovoo = Long.parseLong(list[1]);
        this.codigocompanhia = list[2];
        this.partidacidade = list[3];
        this.chegadacidade = list[4];
        this.partidaaeroportoid = Long.parseLong(list[5]);
        this.chegadaaeroportoid = Long.parseLong(list[6]);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            this.partidatempoestimado = sdf.parse(list[7]);
            this.chegadatempoestimado = sdf.parse(list[8]);
            this.partidatemporeal = sdf.parse(list[9]);
            this.chegadatemporeal = sdf.parse(list[10]);
            this.checkininicio = sdf.parse(list[12]);
            this.checkinfim = sdf.parse(list[13]);
            this.embarque = sdf.parse(list[15]);
            this.bagagem = sdf.parse(list[17]);
            this.desembarque = sdf.parse(list[19]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.terminal = Long.parseLong(list[11]);
        this.portaembarque = Long.parseLong(list[14]);
        this.tapetebagagem = Long.parseLong(list[16]);
        this.portadesembarque = Long.parseLong(list[18]);

        this.tipovoo = Boolean.parseBoolean(list[20]);
    }

    public Flight(Parcel in) {
        this.id = in.readLong();
        this.codigovoo = in.readLong();
        this.codigocompanhia = in.readString();
        this.partidacidade = in.readString();
        this.chegadacidade = in.readString();
        this.partidaaeroportoid = in.readLong();
        this.chegadaaeroportoid = in.readLong();
        this.partidatempoestimado = new Date(in.readLong());
        this.chegadatempoestimado = new Date(in.readLong());
        this.partidatemporeal = new Date(in.readLong());
        this.chegadatemporeal = new Date(in.readLong());

        this.terminal = in.readLong();
        this.checkininicio = new Date(in.readLong());
        this.checkinfim = new Date(in.readLong());
        this.portaembarque = in.readLong();
        this.embarque = new Date(in.readLong());
        this.tapetebagagem = in.readLong();
        this.bagagem = new Date(in.readLong());
        this.portadesembarque = in.readLong();
        this.desembarque = new Date(in.readLong());

        this.tipovoo = new Boolean(in.readString());
    }

    public long getId() {
        return id;
    }

    public long getCodigovoo() {
        return codigovoo;
    }

    public String getCodigocompanhia() {
        return codigocompanhia;
    }

    public String getPartidacidade() {
        return partidacidade;
    }

    public String getChegadacidade() {
        return chegadacidade;
    }

    public long getPartidaaeroportoid() {
        return partidaaeroportoid;
    }

    public long getChegadaaeroportoid() {
        return chegadaaeroportoid;
    }

    public Date getPartidatempoestimado() {
        return partidatempoestimado;
    }

    public Date getChegadatempoestimado() {
        return chegadatempoestimado;
    }

    public Date getPartidatemporeal() {
        return partidatemporeal;
    }

    public Date getChegadatemporeal() {
        return chegadatemporeal;
    }

    public long getTerminal() {
        return terminal;
    }

    public Date getCheckininicio() {
        return checkininicio;
    }

    public Date getCheckinfim() {
        return checkinfim;
    }

    public long getPortaembarque() {
        return portaembarque;
    }

    public Date getEmbarque() {
        return embarque;
    }

    public long getTapetebagagem() {
        return tapetebagagem;
    }

    public Date getBagagem() {
        return bagagem;
    }

    public long getPortadesembarque() {
        return portadesembarque;
    }

    public Date getDesembarque() {
        return desembarque;
    }

    public boolean isPartida() {
        if(tipovoo) return true;
        return false;
    }

    public boolean isChegada() {
        if(!tipovoo) return true;
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(codigovoo);
        parcel.writeString(codigocompanhia);
        parcel.writeString(partidacidade);
        parcel.writeString(chegadacidade);
        parcel.writeLong(partidaaeroportoid);
        parcel.writeLong(chegadaaeroportoid);
        parcel.writeLong(partidatempoestimado.getTime());
        parcel.writeLong(chegadatempoestimado.getTime());
        parcel.writeLong(partidatemporeal.getTime());
        parcel.writeLong(chegadatemporeal.getTime());

        parcel.writeLong(terminal);
        parcel.writeLong(checkininicio.getTime());
        parcel.writeLong(checkinfim.getTime());
        parcel.writeLong(portaembarque);
        parcel.writeLong(embarque.getTime());
        parcel.writeLong(tapetebagagem);
        parcel.writeLong(bagagem.getTime());
        parcel.writeLong(portadesembarque);
        parcel.writeLong(desembarque.getTime());

        parcel.writeString(String.valueOf(tipovoo));
    }

    public static final Parcelable.Creator<Flight> CREATOR = new Parcelable.Creator<Flight>() {
        public Flight createFromParcel(Parcel in) {
            return new Flight(in);
        }

        public Flight[] newArray(int size) {
            return new Flight[size];
        }
    };

    @Override
    public String toString() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        return this.getId()+","+
                this.getCodigovoo()+","+
                this.getCodigocompanhia()+","+
                this.getPartidacidade()+","+
                this.getChegadacidade()+","+
                this.getPartidaaeroportoid()+","+
                this.getChegadaaeroportoid()+","+
                sdf.format(this.getPartidatempoestimado())+","+
                sdf.format(this.getChegadatempoestimado())+","+
                sdf.format(this.getPartidatemporeal())+","+
                sdf.format(this.getChegadatemporeal())+","+

                this.getTerminal()+","+
                sdf.format(this.getCheckininicio())+","+
                sdf.format(this.getCheckinfim())+","+
                this.getPortaembarque()+","+
                sdf.format(this.getEmbarque())+","+
                this.getTapetebagagem()+","+
                sdf.format(this.getBagagem())+","+
                this.getPortadesembarque()+","+
                sdf.format(this.getDesembarque())+","+

                this.tipovoo;
    }

    public FlightSerializable toSerializable()
    {
        return new FlightSerializable(this.id, this.codigovoo, this.codigocompanhia,
                this.partidacidade, this.chegadacidade, this.partidaaeroportoid,
                this.chegadaaeroportoid, this.partidatempoestimado, this.chegadatempoestimado,
                this.partidatemporeal, this.chegadatemporeal, this.terminal, this.checkininicio,
                this.checkinfim, this.portaembarque, this.embarque, this.tapetebagagem, this.bagagem,
                this.portadesembarque, this.desembarque, this.tipovoo);
    }
}
