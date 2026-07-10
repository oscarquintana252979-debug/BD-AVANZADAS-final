package com.equipo4.bibliotecamusical.interfaces;

import com.equipo4.bibliotecamusical.entidades.Usuario;
import com.equipo4.bibliotecamusical.excepciones.ElementoNoEncontradoException;
import com.equipo4.bibliotecamusical.excepciones.GeneroRestringidoException;

public interface IUsuarioDAO {
    void registrarUsuario(Usuario usuario);
    boolean iniciarSesion(String correo, String contrasenaPlana);
    Usuario consultarPerfilPorCorreo(String correo);
    void actualizarPerfil(String correoActual, String nuevoNombre, String nuevaImagen);
    void agregarGeneroNoDeseado(String correo, String generoABloquear);
    void agregarAFavoritos(String correoUsuario, String tipo, String elementoId, String genero)
            throws GeneroRestringidoException, ElementoNoEncontradoException;
}
