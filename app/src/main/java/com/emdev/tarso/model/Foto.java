package com.emdev.tarso.model;

public class Foto {
    String nombre;
    String creador;
    String url;

    public Foto() {
    }

    public Foto(String nombre, String creador, String url) {
        this.nombre = nombre;
        this.creador = creador;
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
