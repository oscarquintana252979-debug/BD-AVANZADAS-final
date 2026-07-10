package com.equipo4.bibliotecamusical.persistencia;

import com.equipo4.bibliotecamusical.entidades.Persona;
import com.equipo4.bibliotecamusical.excepciones.ElementoNoEncontradoException;
import com.equipo4.bibliotecamusical.excepciones.PersistenciaException;
import com.equipo4.bibliotecamusical.interfaces.IConexion;
import com.equipo4.bibliotecamusical.interfaces.IPersonaDAO;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;

public class PersonaDAO implements IPersonaDAO {

    private final MongoCollection<Persona> coleccionPersonas;

    public PersonaDAO(IConexion conexion) {
        this.coleccionPersonas = conexion.conexion().getCollection("personas", Persona.class);
    }

    @Override
    public Persona guardar(Persona persona) throws PersistenciaException {
        try {
            coleccionPersonas.insertOne(persona);
            return persona;
        } catch (MongoException ex) {
            throw new PersistenciaException("Error al guardar la persona: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Persona buscarPorId(String id) throws ElementoNoEncontradoException {
        Persona persona = coleccionPersonas.find(Filters.eq("_id", new ObjectId(id))).first();
        if (persona == null) {
            throw new ElementoNoEncontradoException("No existe una persona con id " + id);
        }
        return persona;
    }
}
