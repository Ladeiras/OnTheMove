package com.moksie.onthemove.objects;

import java.util.ArrayList;

/**
 * Created by belh0 on 14-04-2014.
 */
public class Service
{
    private long id;
    private String name;
    private String descricao;
    private ArrayList<Map> maps;

    public Service(long id, String name, String descricao, ArrayList<Map> maps) {
        this.id = id;
        this.name = name;
        this.descricao = descricao;
        this.maps = maps;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<Map> maps) {
        this.maps = maps;
    }
}
