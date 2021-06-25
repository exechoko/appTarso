package com.emdev.tarso.model;

public class FechaPreinscripcion {
    String inicio;
    String fin;
    String anio;
    String url;

    public FechaPreinscripcion() {
    }

    public FechaPreinscripcion(String inicio, String fin, String anio, String url) {
        this.inicio = inicio;
        this.fin = fin;
        this.anio = anio;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }
}
