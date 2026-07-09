/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.equipo4.bibliotecamusical.persistencia;
import com.equipo4.bibliotecamusical.dominio.Usuario;
import com.equipo4.bibliotecamusical.negocio.UtilidadesSeguridad;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author oscar
 */
public class UsuarioDAO {

    private MongoCollection<Document> coleccionUsuarios;

    public UsuarioDAO() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase baseDatos = mongoClient.getDatabase("bibliotecaMusical4");
        this.coleccionUsuarios = baseDatos.getCollection("usuarios");
    }

    public void registrarUsuario(Usuario usuario) {
        String contrasenaHasheada = UtilidadesSeguridad.encriptarContrasena(usuario.getContrasena());

        Document docUsuario = new Document("_id", new ObjectId())
                .append("nombreUsuario", usuario.getNombreUsuario())
                .append("correo", usuario.getCorreoElectronico())
                .append("contrasena", contrasenaHasheada) 
                .append("imagenPerfil", usuario.getImagenPerfil())
                .append("generosNoDeseados", new ArrayList<String>()) 
                .append("favoritos", new ArrayList<Document>()); 

        coleccionUsuarios.insertOne(docUsuario);
        System.out.println("Usuario registrado exitosamente en MongoDB.");
    }

    public boolean iniciarSesion(String correo, String contrasenaPlana) {
        Document usuarioEncontrado = coleccionUsuarios.find(Filters.regex("correo", "^" + correo + "$", "i")).first();

        if (usuarioEncontrado == null) {
            return false;
        }

        String contrasenaHasheada = usuarioEncontrado.getString("contrasena");

        return UtilidadesSeguridad.verificarContrasena(contrasenaPlana, contrasenaHasheada);
    }

    public Document consultarPerfilPorCorreo(String correo) {
        return coleccionUsuarios.find(Filters.regex("correo", "^" + correo + "$", "i")).first();
    }


    public void actualizarPerfil(String correoActual, String nuevoNombre, String nuevaImagen) {
        coleccionUsuarios.updateOne(
                Filters.eq("correo", correoActual),
                Updates.combine(
                        Updates.set("nombreUsuario", nuevoNombre),
                        Updates.set("imagenPerfil", nuevaImagen)
                )
        );
        System.out.println("Perfil actualizado correctamente.");
    }


    public void agregarGeneroNoDeseado(String correo, String generoABloquear) {
        coleccionUsuarios.updateOne(
                Filters.eq("correo", correo),
                Updates.addToSet("generosNoDeseados", generoABloquear)
        );
        
        coleccionUsuarios.updateOne(
                Filters.eq("correo", correo),
                Updates.pull("favoritos", new Document("genero", generoABloquear))
        );
        
        System.out.println("Género bloqueado y favoritos purgados correctamente.");
    }

    public void agregarAFavoritos(String correoUsuario, String tipo, String elementoId, String genero) {
        org.bson.Document usuario = coleccionUsuarios.find(Filters.eq("correo", correoUsuario)).first();
        
        if (usuario == null) {
            throw new IllegalStateException("El sistema no encontró ningún usuario con el correo: " + correoUsuario);
        }
        
        java.util.List<String> generosBloqueados = usuario.getList("generosNoDeseados", String.class);
        if (generosBloqueados != null && generosBloqueados.contains(genero)) {
            throw new IllegalStateException("El género '" + genero + "' está bloqueado en tu perfil.");
        }

        Document nuevoFavorito = new Document()
                .append("_id", new ObjectId())
                .append("tipo", tipo)
                .append("elementoId", new ObjectId(elementoId)) 
                .append("genero", genero)
                .append("fechaAgregacion", new java.util.Date());

        coleccionUsuarios.updateOne(
                Filters.eq("correo", correoUsuario),
                Updates.addToSet("favoritos", nuevoFavorito)
        );
        System.out.println("Favorito incrustado en el arreglo correctamente.");
    }
}