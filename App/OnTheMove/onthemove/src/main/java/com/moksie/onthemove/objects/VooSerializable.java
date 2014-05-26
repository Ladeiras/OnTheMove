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
}
