package com.equipo4.bibliotecamusical.entidades;

import java.util.Date;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class Favorito {
    @BsonId
    private ObjectId id;
    private String tipo;
    private ObjectId elementoId;
    private String genero;
    private Date fechaAgregacion;

    public Favorito() {}

    public Favorito(String tipo, ObjectId elementoId, String genero, Date fechaAgregacion) {
        this.id = new ObjectId();
        this.tipo = tipo;
        this.elementoId = elementoId;
        this.genero = genero;
        this.fechaAgregacion = fechaAgregacion;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ObjectId getElementoId() {
        return elementoId;
    }

    public void setElementoId(ObjectId elementoId) {
        this.elementoId = elementoId;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Date getFechaAgregacion() {
        return fechaAgregacion;
    }

    public void setFechaAgregacion(Date fechaAgregacion) {
        this.fechaAgregacion = fechaAgregacion;
    }
}
