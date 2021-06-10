package com.emdev.tarso.model;

public class SolicitudProfesor {
    private String nombre;
    private String dni;
    private String correo;
    private String telefono;
    private String id;
    private String estado;

    public SolicitudProfesor() {
    }

    public SolicitudProfesor(String nombre, String dni, String correo, String telefono, String id) {
        this.nombre = nombre;
        this.dni = dni;
        this.correo = correo;
        this.telefono = telefono;
        this.id = id;
        this.estado = "false";
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
