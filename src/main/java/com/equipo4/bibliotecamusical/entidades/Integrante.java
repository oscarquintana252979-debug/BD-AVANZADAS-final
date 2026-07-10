package com.equipo4.bibliotecamusical.entidades;

import org.bson.types.ObjectId;

public class Integrante {
    private ObjectId personaId;
    private String rol;
    private String fechaIngreso;
    private String fechaSalida;
    private String estadoActividad;

    public Integrante() {}

    public Integrante(ObjectId personaId, String rol, String fechaIngreso, String fechaSalida, String estadoActividad) {
        this.personaId = personaId;
        this.rol = rol;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.estadoActividad = estadoActividad;
    }

    public ObjectId getPersonaId() {
        return personaId;
    }

    public void setPersonaId(ObjectId personaId) {
        this.personaId = personaId;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getEstadoActividad() {
        return estadoActividad;
    }

    public void setEstadoActividad(String estadoActividad) {
        this.estadoActividad = estadoActividad;
    }
}
