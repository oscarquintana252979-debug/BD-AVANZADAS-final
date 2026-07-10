package com.equipo4.bibliotecamusical.entidades;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class Cancion {
    @BsonId
    private ObjectId id;
    private String titulo;
    private String duracion;

    public Cancion() {}

    public Cancion(String titulo, String duracion) {
        this.id = new ObjectId();
        this.titulo = titulo;
        this.duracion = duracion;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}
