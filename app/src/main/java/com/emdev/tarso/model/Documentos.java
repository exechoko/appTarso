package com.emdev.tarso.model;

public class Documentos {
    String nombre;
    String curso;
    String materia;
    String fecha;
    String creador;
    String creadorIsProfesor; //YES o NO
    String url;
    String nota;
    String concepto;
    String id;

    public Documentos() {
    }

    public Documentos(String nombre, String curso, String materia, String fecha, String creador, String creadorIsProfesor, String url, String id) {
        this.nombre = nombre;
        this.curso = curso;
        this.materia = materia;
        this.fecha = fecha;
        this.creador = creador;
        this.creadorIsProfesor = creadorIsProfesor;
        this.url = url;
        this.nota = "Sin nota";
        this.concepto = "Sin concepto";
        this.id = id;
    }

    public String getCreadorIsProfesor() {
        return creadorIsProfesor;
    }

    public void setCreadorIsProfesor(String creadorIsProfesor) {
        this.creadorIsProfesor = creadorIsProfesor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }
}
