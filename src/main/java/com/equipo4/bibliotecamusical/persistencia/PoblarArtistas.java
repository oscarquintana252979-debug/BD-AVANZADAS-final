package com.equipo4.bibliotecamusical.persistencia;

import com.equipo4.bibliotecamusical.entidades.Album;
import com.equipo4.bibliotecamusical.entidades.Artista;
import com.equipo4.bibliotecamusical.entidades.Cancion;
import com.equipo4.bibliotecamusical.entidades.Integrante;
import com.equipo4.bibliotecamusical.entidades.Persona;
import com.equipo4.bibliotecamusical.excepciones.PersistenciaException;
import com.equipo4.bibliotecamusical.implementaciones.ConexionDAO;
import com.equipo4.bibliotecamusical.interfaces.IConexion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Genera los 60 artistas de prueba (30 solistas + 30 bandas, 30 por cada
 * integrante del equipo) exigidos por el proyecto. Se invoca desde
 * {@link ArtistaDAO#poblarArtistasDePrueba()} y, de forma independiente,
 * desde {@link #main(String[])} para poder correrla por línea de comandos.
 */
public class PoblarArtistas {

    public static void main(String[] args) {
        try {
            new ArtistaDAO(new ConexionDAO()).poblarArtistasDePrueba();
            System.out.println("¡Éxito! Se insertaron 60 artistas, 120 álbumes y 360 canciones.");
        } catch (PersistenciaException e) {
            System.err.println("Error en la inserción masiva: " + e.getMessage());
        }
    }

    public static List<Artista> generarArtistas(IConexion conexion) throws PersistenciaException {
        List<Artista> artistas = new ArrayList<>();
        artistas.addAll(generarSolistas(conexion));
        artistas.addAll(generarBandas(conexion));
        return artistas;
    }

    private static List<Artista> generarSolistas(IConexion conexion) throws PersistenciaException {
        String[][] datos = {
            {"Bad Bunny", "url_badbunny.jpg", "Reggaeton"},
            {"The Weeknd", "url_weeknd.jpg", "R&B"},
            {"Taylor Swift", "url_taylor.jpg", "Pop"},
            {"Luis Miguel", "url_luismi.jpg", "Pop Latino"},
            {"Dua Lipa", "url_dualipa.jpg", "Pop"},
            {"Ed Sheeran", "url_ed.jpg", "Pop"},
            {"Billie Eilish", "url_billie.jpg", "Pop Alternativo"},
            {"Bruno Mars", "url_bruno.jpg", "R&B/Pop"},
            {"Shakira", "url_shakira.jpg", "Pop Latino"},
            {"Post Malone", "url_postmalone.jpg", "Hip Hop"},
            {"Ariana Grande", "url_ariana.jpg", "Pop"},
            {"Drake", "url_drake.jpg", "Rap"},
            {"Eminem", "url_eminem.jpg", "Rap"},
            {"Rosalía", "url_rosalia.jpg", "Flamenco Pop"},
            {"Harry Styles", "url_harry.jpg", "Pop"},
            {"Karol G", "url_karolg.jpg", "Reggaeton"},
            {"Adele", "url_adele.jpg", "Pop/Soul"},
            {"Rihanna", "url_rihanna.jpg", "R&B"},
            {"Justin Bieber", "url_justin.jpg", "Pop"},
            {"Selena Gomez", "url_selena.jpg", "Pop"},
            {"Camilo", "url_camilo.jpg", "Pop Latino"},
            {"Peso Pluma", "url_pesopluma.jpg", "Corridos"},
            {"SZA", "url_sza.jpg", "R&B"},
            {"Olivia Rodrigo", "url_olivia.jpg", "Pop Rock"},
            {"J Balvin", "url_jbalvin.jpg", "Reggaeton"},
            {"Feid", "url_feid.jpg", "Reggaeton"},
            {"Kali Uchis", "url_kali.jpg", "R&B/Pop"},
            {"Sam Smith", "url_samsmith.jpg", "Pop/Soul"},
            {"Doja Cat", "url_doja.jpg", "Pop/Rap"},
            {"Christian Nodal", "url_nodal.jpg", "Regional Mexicano"}
        };

        PersonaDAO personaDAO = new PersonaDAO(conexion);
        List<Artista> solistas = new ArrayList<>();
        for (String[] fila : datos) {
            Persona persona = personaDAO.guardar(partirNombre(fila[0]));
            solistas.add(crearSolista(fila[0], fila[1], fila[2], persona));
        }
        return solistas;
    }

    private static List<Artista> generarBandas(IConexion conexion) throws PersistenciaException {
        PersonaDAO personaDAO = new PersonaDAO(conexion);
        List<Artista> bandas = new ArrayList<>();

        bandas.add(crearBanda(conexion, personaDAO, "Metallica", "url_metallica.jpg", "Heavy Metal", Arrays.asList(
            new String[]{"James Hetfield", "Vocalista/Guitarra", "1981", "N/A", "Activo"},
            new String[]{"Lars Ulrich", "Baterista", "1981", "N/A", "Activo"},
            new String[]{"Kirk Hammett", "Guitarrista", "1983", "N/A", "Activo"},
            new String[]{"Cliff Burton", "Bajista", "1982", "1986", "Inactivo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "The Beatles", "url_beatles.jpg", "Rock/Pop", Arrays.asList(
            new String[]{"Paul McCartney", "Vocalista/Bajista", "1960", "1970", "Inactivo"},
            new String[]{"John Lennon", "Vocalista/Guitarra", "1960", "1970", "Inactivo"},
            new String[]{"George Harrison", "Guitarrista", "1960", "1970", "Inactivo"},
            new String[]{"Ringo Starr", "Baterista", "1962", "1970", "Inactivo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Coldplay", "url_coldplay.jpg", "Pop Rock", Arrays.asList(
            new String[]{"Chris Martin", "Vocalista", "1996", "N/A", "Activo"},
            new String[]{"Jonny Buckland", "Guitarrista", "1996", "N/A", "Activo"},
            new String[]{"Guy Berryman", "Bajista", "1997", "N/A", "Activo"},
            new String[]{"Will Champion", "Baterista", "1997", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Queen", "url_queen.jpg", "Rock", Arrays.asList(
            new String[]{"Freddie Mercury", "Vocalista", "1970", "1991", "Inactivo"},
            new String[]{"Brian May", "Guitarrista", "1970", "N/A", "Activo"},
            new String[]{"Roger Taylor", "Baterista", "1970", "N/A", "Activo"},
            new String[]{"John Deacon", "Bajista", "1971", "1997", "Inactivo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Linkin Park", "url_linkin.jpg", "Nu Metal", Arrays.asList(
            new String[]{"Mike Shinoda", "Vocalista/Rap", "1996", "N/A", "Activo"},
            new String[]{"Brad Delson", "Guitarrista", "1996", "N/A", "Activo"},
            new String[]{"Chester Bennington", "Vocalista", "1999", "2017", "Inactivo"},
            new String[]{"Joe Hahn", "DJ", "1996", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Arctic Monkeys", "url_arctic.jpg", "Indie Rock", Arrays.asList(
            new String[]{"Alex Turner", "Vocalista", "2002", "N/A", "Activo"},
            new String[]{"Jamie Cook", "Guitarrista", "2002", "N/A", "Activo"},
            new String[]{"Matt Helders", "Baterista", "2002", "N/A", "Activo"},
            new String[]{"Nick O'Malley", "Bajista", "2006", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Imagine Dragons", "url_imagine.jpg", "Pop Rock", Arrays.asList(
            new String[]{"Dan Reynolds", "Vocalista", "2008", "N/A", "Activo"},
            new String[]{"Wayne Sermon", "Guitarrista", "2009", "N/A", "Activo"},
            new String[]{"Ben McKee", "Bajista", "2009", "N/A", "Activo"},
            new String[]{"Daniel Platzman", "Baterista", "2011", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Red Hot Chili Peppers", "url_rhcp.jpg", "Funk Rock", Arrays.asList(
            new String[]{"Anthony Kiedis", "Vocalista", "1983", "N/A", "Activo"},
            new String[]{"Flea", "Bajista", "1983", "N/A", "Activo"},
            new String[]{"Chad Smith", "Baterista", "1988", "N/A", "Activo"},
            new String[]{"John Frusciante", "Guitarrista", "2019", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Foo Fighters", "url_foo.jpg", "Rock", Arrays.asList(
            new String[]{"Dave Grohl", "Vocalista/Guitarra", "1994", "N/A", "Activo"},
            new String[]{"Nate Mendel", "Bajista", "1995", "N/A", "Activo"},
            new String[]{"Pat Smear", "Guitarrista", "2010", "N/A", "Activo"},
            new String[]{"Taylor Hawkins", "Baterista", "1997", "2022", "Inactivo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Maroon 5", "url_maroon.jpg", "Pop", Arrays.asList(
            new String[]{"Adam Levine", "Vocalista", "1994", "N/A", "Activo"},
            new String[]{"Jesse Carmichael", "Tecladista", "1994", "N/A", "Activo"},
            new String[]{"James Valentine", "Guitarrista", "2001", "N/A", "Activo"},
            new String[]{"Matt Flynn", "Baterista", "2006", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Nirvana", "url_nirvana.jpg", "Grunge", Arrays.asList(
            new String[]{"Kurt Cobain", "Vocalista/Guitarra", "1987", "1994", "Inactivo"},
            new String[]{"Krist Novoselic", "Bajista", "1987", "1994", "Inactivo"},
            new String[]{"Dave Grohl", "Baterista", "1990", "1994", "Inactivo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Pink Floyd", "url_pink.jpg", "Rock Progresivo", Arrays.asList(
            new String[]{"David Gilmour", "Vocalista/Guitarra", "1967", "2014", "Inactivo"},
            new String[]{"Roger Waters", "Bajista/Vocalista", "1965", "1985", "Inactivo"},
            new String[]{"Richard Wright", "Tecladista", "1965", "2008", "Inactivo"},
            new String[]{"Nick Mason", "Baterista", "1965", "2014", "Inactivo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "The Strokes", "url_strokes.jpg", "Indie Rock", Arrays.asList(
            new String[]{"Julian Casablancas", "Vocalista", "1998", "N/A", "Activo"},
            new String[]{"Albert Hammond Jr.", "Guitarrista", "1998", "N/A", "Activo"},
            new String[]{"Nikolai Fraiture", "Bajista", "1998", "N/A", "Activo"},
            new String[]{"Fabrizio Moretti", "Baterista", "1998", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Muse", "url_muse.jpg", "Rock Alternativo", Arrays.asList(
            new String[]{"Matt Bellamy", "Vocalista/Guitarra", "1994", "N/A", "Activo"},
            new String[]{"Chris Wolstenholme", "Bajista", "1994", "N/A", "Activo"},
            new String[]{"Dominic Howard", "Baterista", "1994", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "The Rolling Stones", "url_stones.jpg", "Rock", Arrays.asList(
            new String[]{"Mick Jagger", "Vocalista", "1962", "N/A", "Activo"},
            new String[]{"Keith Richards", "Guitarrista", "1962", "N/A", "Activo"},
            new String[]{"Ronnie Wood", "Guitarrista", "1975", "N/A", "Activo"},
            new String[]{"Charlie Watts", "Baterista", "1963", "2021", "Inactivo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Aerosmith", "url_aerosmith.jpg", "Hard Rock", Arrays.asList(
            new String[]{"Steven Tyler", "Vocalista", "1970", "N/A", "Activo"},
            new String[]{"Joe Perry", "Guitarrista", "1970", "N/A", "Activo"},
            new String[]{"Tom Hamilton", "Bajista", "1970", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "AC/DC", "url_acdc.jpg", "Hard Rock", Arrays.asList(
            new String[]{"Angus Young", "Guitarrista", "1973", "N/A", "Activo"},
            new String[]{"Brian Johnson", "Vocalista", "1980", "N/A", "Activo"},
            new String[]{"Phil Rudd", "Baterista", "1975", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Guns N' Roses", "url_gnr.jpg", "Hard Rock", Arrays.asList(
            new String[]{"Axl Rose", "Vocalista", "1985", "N/A", "Activo"},
            new String[]{"Slash", "Guitarrista", "1985", "1996", "Inactivo"},
            new String[]{"Duff McKagan", "Bajista", "1985", "1997", "Inactivo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Radiohead", "url_radiohead.jpg", "Rock Alternativo", Arrays.asList(
            new String[]{"Thom Yorke", "Vocalista", "1985", "N/A", "Activo"},
            new String[]{"Jonny Greenwood", "Guitarrista", "1985", "N/A", "Activo"},
            new String[]{"Colin Greenwood", "Bajista", "1985", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Green Day", "url_greenday.jpg", "Punk Rock", Arrays.asList(
            new String[]{"Billie Joe Armstrong", "Vocalista/Guitarra", "1986", "N/A", "Activo"},
            new String[]{"Mike Dirnt", "Bajista", "1986", "N/A", "Activo"},
            new String[]{"Tré Cool", "Baterista", "1990", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "The Who", "url_thewho.jpg", "Rock", Arrays.asList(
            new String[]{"Roger Daltrey", "Vocalista", "1964", "N/A", "Activo"},
            new String[]{"Pete Townshend", "Guitarrista", "1964", "N/A", "Activo"},
            new String[]{"John Entwistle", "Bajista", "1964", "2002", "Inactivo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Fleetwood Mac", "url_fleetwood.jpg", "Rock", Arrays.asList(
            new String[]{"Stevie Nicks", "Vocalista", "1975", "N/A", "Activo"},
            new String[]{"Lindsey Buckingham", "Guitarrista", "1975", "2018", "Inactivo"},
            new String[]{"Mick Fleetwood", "Baterista", "1967", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Black Sabbath", "url_blacksabbath.jpg", "Heavy Metal", Arrays.asList(
            new String[]{"Ozzy Osbourne", "Vocalista", "1968", "1979", "Inactivo"},
            new String[]{"Tony Iommi", "Guitarrista", "1968", "N/A", "Activo"},
            new String[]{"Geezer Butler", "Bajista", "1968", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Def Leppard", "url_defleppard.jpg", "Hard Rock", Arrays.asList(
            new String[]{"Joe Elliott", "Vocalista", "1977", "N/A", "Activo"},
            new String[]{"Phil Collen", "Guitarrista", "1982", "N/A", "Activo"},
            new String[]{"Rick Allen", "Baterista", "1978", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "System of a Down", "url_soad.jpg", "Nu Metal", Arrays.asList(
            new String[]{"Serj Tankian", "Vocalista", "1994", "N/A", "Activo"},
            new String[]{"Daron Malakian", "Guitarrista", "1994", "N/A", "Activo"},
            new String[]{"Shavo Odadjian", "Bajista", "1994", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Slipknot", "url_slipknot.jpg", "Nu Metal", Arrays.asList(
            new String[]{"Corey Taylor", "Vocalista", "1997", "N/A", "Activo"},
            new String[]{"Mick Thomson", "Guitarrista", "1998", "N/A", "Activo"},
            new String[]{"Joey Jordison", "Baterista", "1995", "2013", "Inactivo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Panic! At The Disco", "url_panic.jpg", "Pop Rock", Arrays.asList(
            new String[]{"Brendon Urie", "Vocalista", "2004", "2023", "Inactivo"},
            new String[]{"Spencer Smith", "Baterista", "2004", "2015", "Inactivo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Fall Out Boy", "url_falloutboy.jpg", "Pop Punk", Arrays.asList(
            new String[]{"Patrick Stump", "Vocalista/Guitarra", "2001", "N/A", "Activo"},
            new String[]{"Pete Wentz", "Bajista", "2001", "N/A", "Activo"},
            new String[]{"Andy Hurley", "Baterista", "2003", "N/A", "Activo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "My Chemical Romance", "url_mcr.jpg", "Rock Alternativo", Arrays.asList(
            new String[]{"Gerard Way", "Vocalista", "2001", "2013", "Inactivo"},
            new String[]{"Ray Toro", "Guitarrista", "2001", "2013", "Inactivo"},
            new String[]{"Mikey Way", "Bajista", "2001", "2013", "Inactivo"}
        )));
        bandas.add(crearBanda(conexion, personaDAO, "Twenty One Pilots", "url_top.jpg", "Indie Pop", Arrays.asList(
            new String[]{"Tyler Joseph", "Vocalista", "2009", "N/A", "Activo"},
            new String[]{"Josh Dun", "Baterista", "2011", "N/A", "Activo"}
        )));

        return bandas;
    }

    private static Artista crearSolista(String nombre, String imagen, String genero, Persona persona) {
        Artista artista = new Artista();
        artista.setTipo("Solista");
        artista.setNombre(nombre);
        artista.setImagen(imagen);
        artista.setGenero(genero);
        artista.setPersonaId(persona.getId());
        artista.setAlbumes(generarAlbumesDePrueba(nombre, genero));
        return artista;
    }

    private static Artista crearBanda(IConexion conexion, PersonaDAO personaDAO, String nombre, String imagen,
            String genero, List<String[]> integrantesDatos) throws PersistenciaException {
        List<Integrante> integrantes = new ArrayList<>();
        for (String[] fila : integrantesDatos) {
            Persona persona = personaDAO.guardar(partirNombre(fila[0]));
            integrantes.add(new Integrante(persona.getId(), fila[1], fila[2], fila[3], fila[4]));
        }

        Artista banda = new Artista();
        banda.setTipo("Banda");
        banda.setNombre(nombre);
        banda.setImagen(imagen);
        banda.setGenero(genero);
        banda.setIntegrantes(integrantes);
        banda.setAlbumes(generarAlbumesDePrueba(nombre, genero));
        return banda;
    }

    private static Persona partirNombre(String nombreCompleto) {
        String[] partes = nombreCompleto.trim().split(" ", 2);
        String nombres = partes[0];
        String apellidoPaterno = partes.length > 1 ? partes[1] : null;
        return new Persona(nombres, apellidoPaterno, null);
    }

    private static List<Album> generarAlbumesDePrueba(String nombreArtista, String genero) {
        List<Cancion> cancionesAlbum1 = Arrays.asList(
                new Cancion("Éxito 1 de " + nombreArtista, "3:15"),
                new Cancion("Éxito 2 de " + nombreArtista, "4:02"),
                new Cancion("Éxito 3 de " + nombreArtista, "2:50")
        );

        List<Cancion> cancionesAlbum2 = Arrays.asList(
                new Cancion("Clásico 1 de " + nombreArtista, "3:40"),
                new Cancion("Clásico 2 de " + nombreArtista, "3:10"),
                new Cancion("Clásico 3 de " + nombreArtista, "4:20")
        );

        return Arrays.asList(
                new Album("Primer Álbum de " + nombreArtista, "2015-05-10", genero,
                        "portada1_" + nombreArtista.replace(" ", "") + ".jpg", cancionesAlbum1),
                new Album("Segundo Álbum de " + nombreArtista, "2020-11-22", genero,
                        "portada2_" + nombreArtista.replace(" ", "") + ".jpg", cancionesAlbum2)
        );
    }
}
