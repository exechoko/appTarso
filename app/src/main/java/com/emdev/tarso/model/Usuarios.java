package com.emdev.tarso.model;

public class Usuarios {
    private String nombre;
    private String password;
    private String dni;
    private String correo;
    private String isProfesor;
    private String telefono;
    private String id;
    private String imageURL;
    private String status;

    public Usuarios() {
    }

    public Usuarios(String nombre, String password, String dni, String correo, String isProfesor, String telefono, String id) {
        this.nombre = nombre;
        this.password = password;
        this.dni = dni;
        this.correo = correo;
        this.isProfesor = isProfesor;
        this.telefono = telefono;
        this.id = id;
        this.imageURL = "default";
        this.status = "online";
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getIsProfesor() {
        return isProfesor;
    }

    public void setIsProfesor(String isProfesor) {
        this.isProfesor = isProfesor;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
