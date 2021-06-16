package com.emdev.tarso.Adapter;

public class SliderData {

    // string for our image url.
    private String imgUrl;
    private String creador;

    // empty constructor which is
    // required when using Firebase.
    public SliderData() {
    }

    // Constructor
    public SliderData(String imgUrl, String creador) {
        this.imgUrl = imgUrl;
        this.creador = creador;
    }

    // Getter method.
    public String getImgUrl() {
        return imgUrl;
    }
    public String getCreador() {
        return creador;
    }

    // Setter method.
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public void setCreador(String creador) {
        this.creador = creador;
    }
}
