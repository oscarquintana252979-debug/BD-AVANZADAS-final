package com.equipo4.bibliotecamusical.persistencia;

import com.equipo4.bibliotecamusical.entidades.Favorito;
import com.equipo4.bibliotecamusical.entidades.Usuario;
import com.equipo4.bibliotecamusical.excepciones.ElementoNoEncontradoException;
import com.equipo4.bibliotecamusical.excepciones.GeneroRestringidoException;
import com.equipo4.bibliotecamusical.interfaces.IConexion;
import com.equipo4.bibliotecamusical.interfaces.IUsuarioDAO;
import com.equipo4.bibliotecamusical.negocio.UtilidadesSeguridad;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioDAO implements IUsuarioDAO {

    private final IConexion conexion;
    private final MongoCollection<Usuario> coleccionUsuarios;

    public UsuarioDAO(IConexion conexion) {
        this.conexion = conexion;
        this.coleccionUsuarios = conexion.conexion().getCollection("usuarios", Usuario.class);
    }

    @Override
    public void registrarUsuario(Usuario usuario) {
        usuario.setContrasena(UtilidadesSeguridad.encriptarContrasena(usuario.getContrasena()));
        usuario.setGenerosNoDeseados(new ArrayList<>());
        usuario.setFavoritos(new ArrayList<>());

        coleccionUsuarios.insertOne(usuario);
        System.out.println("Usuario registrado exitosamente en MongoDB.");
    }

    @Override
    public boolean iniciarSesion(String correo, String contrasenaPlana) {
        Usuario usuarioEncontrado = coleccionUsuarios.find(Filters.regex("correo", "^" + correo + "$", "i")).first();

        if (usuarioEncontrado == null) {
            return false;
        }

        return UtilidadesSeguridad.verificarContrasena(contrasenaPlana, usuarioEncontrado.getContrasena());
    }

    @Override
    public Usuario consultarPerfilPorCorreo(String correo) {
        return coleccionUsuarios.find(Filters.regex("correo", "^" + correo + "$", "i")).first();
    }

    @Override
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

    @Override
    public void agregarGeneroNoDeseado(String correo, String generoABloquear) {
        coleccionUsuarios.updateOne(
                Filters.eq("correo", correo),
                Updates.addToSet("generosNoDeseados", generoABloquear)
        );

        coleccionUsuarios.updateOne(
                Filters.eq("correo", correo),
                Updates.pull("favoritos", new org.bson.Document("genero", generoABloquear))
        );

        System.out.println("Género bloqueado y favoritos purgados correctamente.");
    }

    @Override
    public void agregarAFavoritos(String correoUsuario, String tipo, String elementoId, String genero)
            throws GeneroRestringidoException, ElementoNoEncontradoException {
        Usuario usuario = coleccionUsuarios.find(Filters.eq("correo", correoUsuario)).first();

        if (usuario == null) {
            throw new ElementoNoEncontradoException("El sistema no encontró ningún usuario con el correo: " + correoUsuario);
        }

        List<String> generosBloqueados = usuario.getGenerosNoDeseados();
        if (generosBloqueados != null && generosBloqueados.contains(genero)) {
            throw new GeneroRestringidoException(genero);
        }

        ArtistaDAO artistaDAO = new ArtistaDAO(conexion);
        switch (tipo) {
            case "artista" -> artistaDAO.buscarPorId(elementoId);
            case "album" -> artistaDAO.buscarAlbumPorId(elementoId);
            case "cancion" -> artistaDAO.buscarCancionPorId(elementoId);
            default -> throw new IllegalArgumentException("Tipo de favorito desconocido: " + tipo);
        }

        Favorito nuevoFavorito = new Favorito(tipo, new org.bson.types.ObjectId(elementoId), genero, new Date());

        coleccionUsuarios.updateOne(
                Filters.eq("correo", correoUsuario),
                Updates.addToSet("favoritos", nuevoFavorito)
        );
        System.out.println("Favorito agregado correctamente.");
    }
}
