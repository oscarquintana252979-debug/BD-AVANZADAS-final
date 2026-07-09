/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.equipo4.bibliotecamusical.persistencia;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author oscar
 */
public class PoblarArtistas {

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            
            MongoDatabase database = mongoClient.getDatabase("bibliotecaMusical4");
            MongoCollection<Document> coleccionArtistas = database.getCollection("artistas");

            coleccionArtistas.drop();

            List<Document> artistasParaInsertar = new ArrayList<>();

            System.out.println("Generando 15 Solistas...");
            artistasParaInsertar.addAll(generarSolistas());

            System.out.println("Generando 15 Bandas con sus integrantes...");
            artistasParaInsertar.addAll(generarBandas());

            coleccionArtistas.insertMany(artistasParaInsertar);
            
            System.out.println("¡Éxito! Se insertaron " + artistasParaInsertar.size() + " registros correctamente.");

        } catch (Exception e) {
            System.err.println("Error en la inserción masiva: " + e.getMessage());
        }
    }

    private static List<Document> generarSolistas() {
        return Arrays.asList(
            crearSolista("Bad Bunny", "url_badbunny.jpg", "Reggaeton"),
            crearSolista("The Weeknd", "url_weeknd.jpg", "R&B"),
            crearSolista("Taylor Swift", "url_taylor.jpg", "Pop"),
            crearSolista("Luis Miguel", "url_luismi.jpg", "Pop Latino"),
            crearSolista("Dua Lipa", "url_dualipa.jpg", "Pop"),
            crearSolista("Ed Sheeran", "url_ed.jpg", "Pop"),
            crearSolista("Billie Eilish", "url_billie.jpg", "Pop Alternativo"),
            crearSolista("Bruno Mars", "url_bruno.jpg", "R&B/Pop"),
            crearSolista("Shakira", "url_shakira.jpg", "Pop Latino"),
            crearSolista("Post Malone", "url_postmalone.jpg", "Hip Hop"),
            crearSolista("Ariana Grande", "url_ariana.jpg", "Pop"),
            crearSolista("Drake", "url_drake.jpg", "Rap"),
            crearSolista("Eminem", "url_eminem.jpg", "Rap"),
            crearSolista("Rosalía", "url_rosalia.jpg", "Flamenco Pop"),
            crearSolista("Harry Styles", "url_harry.jpg", "Pop")
        );
    }

    private static List<Document> generarBandas() {
        List<Document> bandas = new ArrayList<>();

        // 1. Metallica
        bandas.add(crearBanda("Metallica", "url_metallica.jpg", "Heavy Metal", Arrays.asList(
            crearIntegrante("James Hetfield", "Vocalista/Guitarra", "1981", "N/A", "Activo"),
            crearIntegrante("Lars Ulrich", "Baterista", "1981", "N/A", "Activo"),
            crearIntegrante("Kirk Hammett", "Guitarrista", "1983", "N/A", "Activo"),
            crearIntegrante("Cliff Burton", "Bajista", "1982", "1986", "Inactivo") // Inactivo para la rúbrica
        )));

        // 2. The Beatles
        bandas.add(crearBanda("The Beatles", "url_beatles.jpg", "Rock/Pop", Arrays.asList(
            crearIntegrante("Paul McCartney", "Vocalista/Bajista", "1960", "1970", "Inactivo"),
            crearIntegrante("John Lennon", "Vocalista/Guitarra", "1960", "1970", "Inactivo"),
            crearIntegrante("George Harrison", "Guitarrista", "1960", "1970", "Inactivo"),
            crearIntegrante("Ringo Starr", "Baterista", "1962", "1970", "Inactivo")
        )));

        // 3. Coldplay
        bandas.add(crearBanda("Coldplay", "url_coldplay.jpg", "Pop Rock", Arrays.asList(
            crearIntegrante("Chris Martin", "Vocalista", "1996", "N/A", "Activo"),
            crearIntegrante("Jonny Buckland", "Guitarrista", "1996", "N/A", "Activo"),
            crearIntegrante("Guy Berryman", "Bajista", "1997", "N/A", "Activo"),
            crearIntegrante("Will Champion", "Baterista", "1997", "N/A", "Activo")
        )));

        // 4. Queen
        bandas.add(crearBanda("Queen", "url_queen.jpg", "Rock", Arrays.asList(
            crearIntegrante("Freddie Mercury", "Vocalista", "1970", "1991", "Inactivo"),
            crearIntegrante("Brian May", "Guitarrista", "1970", "N/A", "Activo"),
            crearIntegrante("Roger Taylor", "Baterista", "1970", "N/A", "Activo"),
            crearIntegrante("John Deacon", "Bajista", "1971", "1997", "Inactivo")
        )));

        // 5. Linkin Park
        bandas.add(crearBanda("Linkin Park", "url_linkin.jpg", "Nu Metal", Arrays.asList(
            crearIntegrante("Mike Shinoda", "Vocalista/Rap", "1996", "N/A", "Activo"),
            crearIntegrante("Brad Delson", "Guitarrista", "1996", "N/A", "Activo"),
            crearIntegrante("Chester Bennington", "Vocalista", "1999", "2017", "Inactivo"),
            crearIntegrante("Joe Hahn", "DJ", "1996", "N/A", "Activo")
        )));

        // 6. Arctic Monkeys
        bandas.add(crearBanda("Arctic Monkeys", "url_arctic.jpg", "Indie Rock", Arrays.asList(
            crearIntegrante("Alex Turner", "Vocalista", "2002", "N/A", "Activo"),
            crearIntegrante("Jamie Cook", "Guitarrista", "2002", "N/A", "Activo"),
            crearIntegrante("Matt Helders", "Baterista", "2002", "N/A", "Activo"),
            crearIntegrante("Nick O'Malley", "Bajista", "2006", "N/A", "Activo")
        )));

        // 7. Imagine Dragons
        bandas.add(crearBanda("Imagine Dragons", "url_imagine.jpg", "Pop Rock", Arrays.asList(
            crearIntegrante("Dan Reynolds", "Vocalista", "2008", "N/A", "Activo"),
            crearIntegrante("Wayne Sermon", "Guitarrista", "2009", "N/A", "Activo"),
            crearIntegrante("Ben McKee", "Bajista", "2009", "N/A", "Activo"),
            crearIntegrante("Daniel Platzman", "Baterista", "2011", "N/A", "Activo")
        )));

        // 8. Red Hot Chili Peppers
        bandas.add(crearBanda("Red Hot Chili Peppers", "url_rhcp.jpg", "Funk Rock", Arrays.asList(
            crearIntegrante("Anthony Kiedis", "Vocalista", "1983", "N/A", "Activo"),
            crearIntegrante("Flea", "Bajista", "1983", "N/A", "Activo"),
            crearIntegrante("Chad Smith", "Baterista", "1988", "N/A", "Activo"),
            crearIntegrante("John Frusciante", "Guitarrista", "2019", "N/A", "Activo")
        )));

        // 9. Foo Fighters
        bandas.add(crearBanda("Foo Fighters", "url_foo.jpg", "Rock", Arrays.asList(
            crearIntegrante("Dave Grohl", "Vocalista/Guitarra", "1994", "N/A", "Activo"),
            crearIntegrante("Nate Mendel", "Bajista", "1995", "N/A", "Activo"),
            crearIntegrante("Pat Smear", "Guitarrista", "2010", "N/A", "Activo"),
            crearIntegrante("Taylor Hawkins", "Baterista", "1997", "2022", "Inactivo")
        )));

        // 10. Maroon 5
        bandas.add(crearBanda("Maroon 5", "url_maroon.jpg", "Pop", Arrays.asList(
            crearIntegrante("Adam Levine", "Vocalista", "1994", "N/A", "Activo"),
            crearIntegrante("Jesse Carmichael", "Tecladista", "1994", "N/A", "Activo"),
            crearIntegrante("James Valentine", "Guitarrista", "2001", "N/A", "Activo"),
            crearIntegrante("Matt Flynn", "Baterista", "2006", "N/A", "Activo")
        )));

        // 11. Nirvana
        bandas.add(crearBanda("Nirvana", "url_nirvana.jpg", "Grunge", Arrays.asList(
            crearIntegrante("Kurt Cobain", "Vocalista/Guitarra", "1987", "1994", "Inactivo"),
            crearIntegrante("Krist Novoselic", "Bajista", "1987", "1994", "Inactivo"),
            crearIntegrante("Dave Grohl", "Baterista", "1990", "1994", "Inactivo")
        )));

        // 12. Pink Floyd
        bandas.add(crearBanda("Pink Floyd", "url_pink.jpg", "Rock Progresivo", Arrays.asList(
            crearIntegrante("David Gilmour", "Vocalista/Guitarra", "1967", "2014", "Inactivo"),
            crearIntegrante("Roger Waters", "Bajista/Vocalista", "1965", "1985", "Inactivo"),
            crearIntegrante("Richard Wright", "Tecladista", "1965", "2008", "Inactivo"),
            crearIntegrante("Nick Mason", "Baterista", "1965", "2014", "Inactivo")
        )));

        // 13. The Strokes
        bandas.add(crearBanda("The Strokes", "url_strokes.jpg", "Indie Rock", Arrays.asList(
            crearIntegrante("Julian Casablancas", "Vocalista", "1998", "N/A", "Activo"),
            crearIntegrante("Albert Hammond Jr.", "Guitarrista", "1998", "N/A", "Activo"),
            crearIntegrante("Nikolai Fraiture", "Bajista", "1998", "N/A", "Activo"),
            crearIntegrante("Fabrizio Moretti", "Baterista", "1998", "N/A", "Activo")
        )));

        // 14. Muse
        bandas.add(crearBanda("Muse", "url_muse.jpg", "Rock Alternativo", Arrays.asList(
            crearIntegrante("Matt Bellamy", "Vocalista/Guitarra", "1994", "N/A", "Activo"),
            crearIntegrante("Chris Wolstenholme", "Bajista", "1994", "N/A", "Activo"),
            crearIntegrante("Dominic Howard", "Baterista", "1994", "N/A", "Activo")
        )));

        // 15. The Rolling Stones
        bandas.add(crearBanda("The Rolling Stones", "url_stones.jpg", "Rock", Arrays.asList(
            crearIntegrante("Mick Jagger", "Vocalista", "1962", "N/A", "Activo"),
            crearIntegrante("Keith Richards", "Guitarrista", "1962", "N/A", "Activo"),
            crearIntegrante("Ronnie Wood", "Guitarrista", "1975", "N/A", "Activo"),
            crearIntegrante("Charlie Watts", "Baterista", "1963", "2021", "Inactivo")
        )));

        return bandas;
    }


    private static Document crearSolista(String nombre, String imagen, String genero) {
        return new Document("tipo", "Solista")
                .append("nombre", nombre)
                .append("imagen", imagen)
                .append("genero", genero);
    }

    private static Document crearBanda(String nombre, String imagen, String genero, List<Document> integrantes) {
        return new Document("tipo", "Banda")
                .append("nombre", nombre)
                .append("imagen", imagen)
                .append("genero", genero)
                .append("integrantes", integrantes);
    }

    private static Document crearIntegrante(String nombreCompleto, String rol, String fechaIngreso, String fechaSalida, String estado) {
        return new Document("nombreCompleto", nombreCompleto)
                .append("rol", rol)
                .append("fechaIngreso", fechaIngreso)
                .append("fechaSalida", fechaSalida)
                .append("estadoActividad", estado);
    }
}