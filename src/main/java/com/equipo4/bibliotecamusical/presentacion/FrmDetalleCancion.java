package com.equipo4.bibliotecamusical.presentacion;

import com.equipo4.bibliotecamusical.entidades.Cancion;
import com.equipo4.bibliotecamusical.excepciones.ElementoNoEncontradoException;
import com.equipo4.bibliotecamusical.implementaciones.ConexionDAO;
import com.equipo4.bibliotecamusical.persistencia.ArtistaDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FrmDetalleCancion extends javax.swing.JFrame {

    private final String correoUsuarioActual;
    private final ArtistaDAO artistaDAO = new ArtistaDAO(new ConexionDAO());
    private Cancion cancion;
    private Set<String> favoritoKeys;
    private JButton btnFavorito;

    public FrmDetalleCancion(String correo, String idCancion) {
        this.correoUsuarioActual = correo;
        try {
            this.cancion = artistaDAO.buscarCancionPorId(idCancion);
        } catch (ElementoNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            new FrmCatalogoCanciones(correo).setVisible(true);
            this.dispose();
            return;
        }
        this.favoritoKeys = FavoritosHelper.cargarClaves(correo);
        initComponents();
        this.getContentPane().setBackground(new Color(25, 25, 25));
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(460, 400);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(25, 25, 25));
        JButton btnVolver = new JButton("< Álbum");
        btnVolver.addActionListener(e -> {
            if (cancion.getAlbumId() != null) {
                new FrmDetalleAlbum(correoUsuarioActual, cancion.getAlbumId().toHexString()).setVisible(true);
            } else {
                new FrmCatalogoCanciones(correoUsuarioActual).setVisible(true);
            }
            this.dispose();
        });
        panelSuperior.add(btnVolver, BorderLayout.WEST);

        JPanel panelCabecera = new JPanel();
        panelCabecera.setLayout(new javax.swing.BoxLayout(panelCabecera, javax.swing.BoxLayout.Y_AXIS));
        panelCabecera.setBackground(new Color(25, 25, 25));
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblIcono = new JLabel("🎵", SwingConstants.CENTER);
        lblIcono.setFont(lblIcono.getFont().deriveFont(56f));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel(cancion.getTitulo(), SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 20f));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        String artistaAlbum = "";
        if (cancion.getNombreArtista() != null) artistaAlbum += "por " + cancion.getNombreArtista();
        if (cancion.getNombreAlbum() != null) artistaAlbum += "  ·  " + cancion.getNombreAlbum();
        JLabel lblContexto = new JLabel(artistaAlbum, SwingConstants.CENTER);
        lblContexto.setForeground(new Color(180, 180, 180));
        lblContexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblInfo = new JLabel("Género: " + cancion.getGenero() + "  ·  Duración: " + cancion.getDuracion(), SwingConstants.CENTER);
        lblInfo.setForeground(new Color(180, 180, 180));
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnFavorito = new JButton();
        btnFavorito.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFavorito.addActionListener(e -> {
            FavoritosHelper.alternar(this, favoritoKeys, correoUsuarioActual, "cancion",
                    cancion.getId().toHexString(), cancion.getGenero());
            actualizarBotonFavorito();
        });

        panelCabecera.add(lblIcono);
        panelCabecera.add(lblTitulo);
        panelCabecera.add(lblContexto);
        panelCabecera.add(lblInfo);
        panelCabecera.add(btnFavorito);

        getContentPane().add(panelSuperior, BorderLayout.NORTH);
        getContentPane().add(panelCabecera, BorderLayout.CENTER);

        actualizarBotonFavorito();
    }

    private void actualizarBotonFavorito() {
        boolean esFav = FavoritosHelper.esFavorito(favoritoKeys, "cancion", cancion.getId().toHexString());
        btnFavorito.setText(esFav ? "♥ En favoritos" : "♡ Agregar a favoritos");
        btnFavorito.setForeground(esFav ? FavoritosHelper.MORADO : FavoritosHelper.GRIS);
    }
}
