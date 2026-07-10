package com.equipo4.bibliotecamusical.excepciones;

public class ElementoNoEncontradoException extends PersistenciaException {

    public ElementoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
