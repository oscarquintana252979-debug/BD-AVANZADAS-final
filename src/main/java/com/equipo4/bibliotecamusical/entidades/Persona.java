package com.equipo4.bibliotecamusical.entidades;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class Persona {
    @BsonId
    private ObjectId id;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;

    public Persona() {}

    public Persona(String nombres, String apellidoPaterno, String apellidoMaterno) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder(nombres != null ? nombres : "");
        if (apellidoPaterno != null && !apellidoPaterno.isBlank()) {
            sb.append(" ").append(apellidoPaterno);
        }
        if (apellidoMaterno != null && !apellidoMaterno.isBlank()) {
            sb.append(" ").append(apellidoMaterno);
        }
        return sb.toString();
    }
}
