package com.equipo4.bibliotecamusical.interfaces;

import com.equipo4.bibliotecamusical.entidades.Album;
import com.equipo4.bibliotecamusical.entidades.Artista;
import com.equipo4.bibliotecamusical.entidades.Cancion;
import com.equipo4.bibliotecamusical.excepciones.ElementoNoEncontradoException;
import com.equipo4.bibliotecamusical.excepciones.PersistenciaException;
import java.util.List;

public interface IArtistaDAO {
    void poblarArtistasDePrueba() throws PersistenciaException;
    List<Artista> listarTodos();
    Artista buscarPorId(String idArtista) throws ElementoNoEncontradoException;
    List<Artista> buscarArtistas(String texto, boolean porNombre, boolean porGenero);
    List<Album> buscarAlbumes(String texto, boolean porNombre, boolean porGenero, boolean porFecha);
    Album buscarAlbumPorId(String idAlbum) throws ElementoNoEncontradoException;
    List<Cancion> buscarCanciones(String texto, boolean porNombre, boolean porGenero);
    Cancion buscarCancionPorId(String idCancion) throws ElementoNoEncontradoException;
}
