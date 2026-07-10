package com.equipo4.bibliotecamusical.entidades;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

public class Cancion {
    @BsonId
    private ObjectId id;
    private String titulo;
    private String duracion;
    @BsonIgnore
    private ObjectId albumId;
    @BsonIgnore
    private ObjectId artistaId;
    @BsonIgnore
    private String nombreAlbum;
    @BsonIgnore
    private String nombreArtista;
    @BsonIgnore
    private String genero;

    public Cancion() {}

    public Cancion(String titulo, String duracion) {
        this.id = new ObjectId();
        this.titulo = titulo;
        this.duracion = duracion;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public ObjectId getAlbumId() {
        return albumId;
    }

    public void setAlbumId(ObjectId albumId) {
        this.albumId = albumId;
    }

    public ObjectId getArtistaId() {
        return artistaId;
    }

    public void setArtistaId(ObjectId artistaId) {
        this.artistaId = artistaId;
    }

    public String getNombreAlbum() {
        return nombreAlbum;
    }

    public void setNombreAlbum(String nombreAlbum) {
        this.nombreAlbum = nombreAlbum;
    }

    public String getNombreArtista() {
        return nombreArtista;
    }

    public void setNombreArtista(String nombreArtista) {
        this.nombreArtista = nombreArtista;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
