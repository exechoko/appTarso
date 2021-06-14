package com.emdev.tarso.model;

public class Track {

    private String titulo;
    private String artista;
    private int imagen;

    public Track(String titulo, String artista, int imagen) {
        this.titulo = titulo;
        this.artista = artista;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
