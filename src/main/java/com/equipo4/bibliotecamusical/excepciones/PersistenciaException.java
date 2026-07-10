package com.equipo4.bibliotecamusical.excepciones;

public class PersistenciaException extends Exception {

    public PersistenciaException() {
        super();
    }

    public PersistenciaException(String mensaje) {
        super(mensaje);
    }

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
