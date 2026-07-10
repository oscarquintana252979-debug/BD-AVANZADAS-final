package com.equipo4.bibliotecamusical.interfaces;

import com.equipo4.bibliotecamusical.entidades.Persona;
import com.equipo4.bibliotecamusical.excepciones.ElementoNoEncontradoException;
import com.equipo4.bibliotecamusical.excepciones.PersistenciaException;

public interface IPersonaDAO {
    Persona guardar(Persona persona) throws PersistenciaException;
    Persona buscarPorId(String id) throws ElementoNoEncontradoException;
}
