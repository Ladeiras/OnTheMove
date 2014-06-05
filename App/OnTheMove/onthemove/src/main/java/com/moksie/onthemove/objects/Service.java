package com.moksie.onthemove.objects;

import java.util.ArrayList;

/**
 * Created by belh0 on 14-04-2014.
 */
public class Service
{
    private long id;
    private String nome;
    private String titulo;
    private String website;
    private String webmail;
    private String descricao;
    private ArrayList<Map> maps;
    private ArrayList<Long> telefones;

    public Service(long id, String nome, String titulo, String website, String webmail,
                   String descricao, ArrayList<Map> maps, ArrayList<Long> telefones) {
        this.id = id;
        this.nome = nome;
        this.titulo = titulo;
        this.website = website;
        this.webmail = webmail;
        this.descricao = descricao;
        this.maps = maps;
        this.telefones = telefones;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebmail() {
        return webmail;
    }

    public void setWebmail(String webmail) {
        this.webmail = webmail;
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

    public ArrayList<Long> getTelefones() {
        return telefones;
    }

    public void setTelefones(ArrayList<Long> telefones) {
        this.telefones = telefones;
    }
}
