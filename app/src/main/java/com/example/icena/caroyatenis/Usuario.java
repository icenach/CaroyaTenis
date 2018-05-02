package com.example.icena.caroyatenis;

/**
 * Created by icenach on 02/05/18.
 */

public class Usuario {

    public String user;
    public String foto;

    public Usuario() {

    }

    public Usuario(String user, String foto) {
        this.user = user;
        this.foto = foto;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
