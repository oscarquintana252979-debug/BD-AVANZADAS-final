package com.equipo4.bibliotecamusical.excepciones;

public class GeneroRestringidoException extends PersistenciaException {

    public GeneroRestringidoException(String genero) {
        super("El género '" + genero + "' está bloqueado en tu perfil.");
    }
}
