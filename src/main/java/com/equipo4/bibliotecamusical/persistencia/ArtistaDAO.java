package com.equipo4.bibliotecamusical.persistencia;

import com.equipo4.bibliotecamusical.entidades.Album;
import com.equipo4.bibliotecamusical.entidades.Artista;
import com.equipo4.bibliotecamusical.entidades.Cancion;
import com.equipo4.bibliotecamusical.excepciones.ElementoNoEncontradoException;
import com.equipo4.bibliotecamusical.excepciones.PersistenciaException;
import com.equipo4.bibliotecamusical.interfaces.IArtistaDAO;
import com.equipo4.bibliotecamusical.interfaces.IConexion;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class ArtistaDAO implements IArtistaDAO {

    private final IConexion conexion;
    private final MongoCollection<Artista> coleccionArtistas;

    public ArtistaDAO(IConexion conexion) {
        this.conexion = conexion;
        this.coleccionArtistas = conexion.conexion().getCollection("artistas", Artista.class);
    }

    @Override
    public void poblarArtistasDePrueba() throws PersistenciaException {
        coleccionArtistas.drop();
        List<Artista> artistas = PoblarArtistas.generarArtistas(conexion);
        try {
            coleccionArtistas.insertMany(artistas);
        } catch (MongoException ex) {
            throw new PersistenciaException("No se pudo poblar la colección de artistas: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Artista> listarTodos() {
        return coleccionArtistas.find().into(new ArrayList<>());
    }

    @Override
    public Artista buscarPorId(String idArtista) throws ElementoNoEncontradoException {
        Artista artista = coleccionArtistas.find(Filters.eq("_id", new ObjectId(idArtista))).first();
        if (artista == null) {
            throw new ElementoNoEncontradoException("No existe un artista con id " + idArtista);
        }
        return artista;
    }

    @Override
    public List<Artista> buscarArtistas(String texto, boolean porNombre, boolean porGenero) {
        List<Bson> criterios = new ArrayList<>();
        if (porNombre) criterios.add(Filters.regex("nombre", java.util.regex.Pattern.quote(texto), "i"));
        if (porGenero) criterios.add(Filters.regex("genero", java.util.regex.Pattern.quote(texto), "i"));

        if (criterios.isEmpty()) {
            return listarTodos();
        }
        return coleccionArtistas.find(Filters.and(criterios)).into(new ArrayList<>());
    }

    @Override
    public List<Album> buscarAlbumes(String texto, boolean porNombre, boolean porGenero, boolean porFecha) {
        List<Bson> etapas = new ArrayList<>();
        etapas.add(Aggregates.unwind("$albumes"));

        List<Bson> criterios = new ArrayList<>();
        if (porNombre) criterios.add(Filters.regex("albumes.nombre", java.util.regex.Pattern.quote(texto), "i"));
        if (porGenero) criterios.add(Filters.regex("albumes.genero", java.util.regex.Pattern.quote(texto), "i"));
        if (porFecha) criterios.add(Filters.regex("albumes.fechaLanzamiento", java.util.regex.Pattern.quote(texto), "i"));
        if (!criterios.isEmpty()) {
            etapas.add(Aggregates.match(Filters.and(criterios)));
        }

        etapas.add(Aggregates.project(Projections.fields(
                Projections.computed("album", "$albumes"),
                Projections.computed("artistaId", "$_id"),
                Projections.computed("nombreArtista", "$nombre")
        )));

        AggregateIterable<Document> resultado = coleccionArtistas.aggregate(etapas, Document.class);

        List<Album> albumes = new ArrayList<>();
        for (Document fila : resultado) {
            Document albumDoc = fila.get("album", Document.class);
            Album album = documentToAlbum(albumDoc);
            album.setArtistaId(fila.getObjectId("artistaId"));
            album.setNombreArtista(fila.getString("nombreArtista"));
            albumes.add(album);
        }
        return albumes;
    }

    @Override
    public Album buscarAlbumPorId(String idAlbum) throws ElementoNoEncontradoException {
        Artista artista = coleccionArtistas.find(
                Filters.elemMatch("albumes", Filters.eq("_id", new ObjectId(idAlbum)))
        ).first();

        if (artista == null) {
            throw new ElementoNoEncontradoException("No existe un álbum con id " + idAlbum);
        }

        return artista.getAlbumes().stream()
                .filter(a -> a.getId().toHexString().equals(idAlbum))
                .findFirst()
                .map(a -> {
                    a.setArtistaId(artista.getId());
                    a.setNombreArtista(artista.getNombre());
                    return a;
                })
                .orElseThrow(() -> new ElementoNoEncontradoException("Álbum no encontrado tras localizar artista."));
    }

    @Override
    public Cancion buscarCancionPorId(String idCancion) throws ElementoNoEncontradoException {
        List<Bson> etapas = List.of(
                Aggregates.unwind("$albumes"),
                Aggregates.unwind("$albumes.canciones"),
                Aggregates.match(Filters.eq("albumes.canciones._id", new ObjectId(idCancion))),
                Aggregates.project(Projections.computed("cancion", "$albumes.canciones"))
        );

        Document fila = coleccionArtistas.aggregate(etapas, Document.class).first();
        if (fila == null) {
            throw new ElementoNoEncontradoException("No existe una canción con id " + idCancion);
        }

        Document cancionDoc = fila.get("cancion", Document.class);
        return documentToCancion(cancionDoc);
    }

    private Album documentToAlbum(Document d) {
        Album album = new Album();
        album.setId(d.getObjectId("_id"));
        album.setNombre(d.getString("nombre"));
        album.setFechaLanzamiento(d.getString("fechaLanzamiento"));
        album.setGenero(d.getString("genero"));
        album.setImagenPortada(d.getString("imagenPortada"));
        List<Document> cancionesDocs = d.getList("canciones", Document.class);
        List<Cancion> canciones = new ArrayList<>();
        if (cancionesDocs != null) {
            for (Document cd : cancionesDocs) {
                canciones.add(documentToCancion(cd));
            }
        }
        album.setCanciones(canciones);
        return album;
    }

    private Cancion documentToCancion(Document d) {
        Cancion cancion = new Cancion();
        cancion.setId(d.getObjectId("_id"));
        cancion.setTitulo(d.getString("titulo"));
        cancion.setDuracion(d.getString("duracion"));
        return cancion;
    }
}
