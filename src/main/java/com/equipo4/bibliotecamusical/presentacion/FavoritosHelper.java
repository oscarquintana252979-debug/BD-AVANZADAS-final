package com.equipo4.bibliotecamusical.presentacion;

import com.equipo4.bibliotecamusical.entidades.Favorito;
import com.equipo4.bibliotecamusical.entidades.Usuario;
import com.equipo4.bibliotecamusical.excepciones.ElementoNoEncontradoException;
import com.equipo4.bibliotecamusical.excepciones.GeneroRestringidoException;
import com.equipo4.bibliotecamusical.implementaciones.ConexionDAO;
import com.equipo4.bibliotecamusical.persistencia.UsuarioDAO;
import java.awt.Color;
import java.awt.Component;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;

class FavoritosHelper {

    static final Color GRIS = new Color(120, 120, 120);
    static final Color MORADO = new Color(255, 0, 255);

    private FavoritosHelper() {}

    static Set<String> cargarClaves(String correo) {
        Set<String> claves = new HashSet<>();
        UsuarioDAO usuarioDAO = new UsuarioDAO(new ConexionDAO());
        Usuario usuario = usuarioDAO.consultarPerfilPorCorreo(correo);
        if (usuario != null && usuario.getFavoritos() != null) {
            for (Favorito f : usuario.getFavoritos()) {
                claves.add(clave(f.getTipo(), f.getElementoId().toHexString()));
            }
        }
        return claves;
    }

    static boolean esFavorito(Set<String> claves, String tipo, String elementoId) {
        return claves.contains(clave(tipo, elementoId));
    }

    static void alternar(Component parent, Set<String> claves, String correo, String tipo, String elementoId, String genero) {
        UsuarioDAO usuarioDAO = new UsuarioDAO(new ConexionDAO());
        String clave = clave(tipo, elementoId);
        try {
            if (claves.contains(clave)) {
                usuarioDAO.eliminarDeFavoritos(correo, tipo, elementoId);
                claves.remove(clave);
            } else {
                usuarioDAO.agregarAFavoritos(correo, tipo, elementoId, genero);
                claves.add(clave);
            }
        } catch (GeneroRestringidoException | ElementoNoEncontradoException ex) {
            JOptionPane.showMessageDialog(parent, ex.getMessage());
        }
    }

    private static String clave(String tipo, String elementoId) {
        return tipo + ":" + elementoId;
    }
}
