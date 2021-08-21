package com.sc703.proyecto.ui.gallery;

public class Usuario {
    // string variable for
    // storing employee name.

    private String ID;

    private String name;

    // string variable for storing
    // employee contact number
    private String telefono;

    // string variable for storing
    // employee address.
    private String correo;

    // an empty constructor is
    // required when using
    // Firebase Realtime Database.
    public Usuario() {

    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    // created getter and setter methods
    // for all our variables.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
