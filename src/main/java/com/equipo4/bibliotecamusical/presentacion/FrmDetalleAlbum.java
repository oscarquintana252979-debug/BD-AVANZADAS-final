package com.equipo4.bibliotecamusical.presentacion;

import com.equipo4.bibliotecamusical.entidades.Album;
import com.equipo4.bibliotecamusical.entidades.Cancion;
import com.equipo4.bibliotecamusical.excepciones.ElementoNoEncontradoException;
import com.equipo4.bibliotecamusical.implementaciones.ConexionDAO;
import com.equipo4.bibliotecamusical.persistencia.ArtistaDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

public class FrmDetalleAlbum extends javax.swing.JFrame {

    private final String correoUsuarioActual;
    private final ArtistaDAO artistaDAO = new ArtistaDAO(new ConexionDAO());
    private Album album;
    private final DefaultListModel<Cancion> modeloCanciones = new DefaultListModel<>();
    private Set<String> favoritoKeys;
    private JButton btnFavAlbum;

    public FrmDetalleAlbum(String correo, String idAlbum) {
        this.correoUsuarioActual = correo;
        try {
            this.album = artistaDAO.buscarAlbumPorId(idAlbum);
        } catch (ElementoNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            new FrmCatalogoAlbumes(correo).setVisible(true);
            this.dispose();
            return;
        }
        this.favoritoKeys = FavoritosHelper.cargarClaves(correo);
        initComponents();
        this.getContentPane().setBackground(new Color(25, 25, 25));
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(520, 620);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(25, 25, 25));
        JButton btnVolver = new JButton("< Álbumes");
        btnVolver.addActionListener(e -> {
            new FrmCatalogoAlbumes(correoUsuarioActual).setVisible(true);
            this.dispose();
        });
        panelSuperior.add(btnVolver, BorderLayout.WEST);

        JPanel panelCabecera = new JPanel();
        panelCabecera.setLayout(new javax.swing.BoxLayout(panelCabecera, javax.swing.BoxLayout.Y_AXIS));
        panelCabecera.setBackground(new Color(25, 25, 25));
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ImageIcon portada = FrmCatalogoArtistas.cargarIcono(album.getImagenPortada(), 120, 120);
        JLabel lblPortada = new JLabel(portada != null ? portada : null);
        if (portada == null) {
            lblPortada.setText("💿");
            lblPortada.setFont(lblPortada.getFont().deriveFont(64f));
        }
        lblPortada.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPortada.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblNombre = new JLabel(album.getNombre(), SwingConstants.CENTER);
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(lblNombre.getFont().deriveFont(Font.BOLD, 20f));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        String artistaTxt = album.getNombreArtista() != null ? "por " + album.getNombreArtista() : "";
        JLabel lblArtista = new JLabel(artistaTxt, SwingConstants.CENTER);
        lblArtista.setForeground(new Color(180, 180, 180));
        lblArtista.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblInfo = new JLabel("Género: " + album.getGenero() + "  ·  Lanzamiento: " + album.getFechaLanzamiento(), SwingConstants.CENTER);
        lblInfo.setForeground(new Color(180, 180, 180));
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnFavAlbum = new JButton();
        btnFavAlbum.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFavAlbum.addActionListener(e -> {
            FavoritosHelper.alternar(this, favoritoKeys, correoUsuarioActual, "album", album.getId().toHexString(), album.getGenero());
            actualizarBotonFavorito();
        });
        actualizarBotonFavorito();

        panelCabecera.add(lblPortada);
        panelCabecera.add(lblNombre);
        panelCabecera.add(lblArtista);
        panelCabecera.add(lblInfo);
        panelCabecera.add(btnFavAlbum);

        JLabel lblCanciones = new JLabel("Lista de canciones");
        lblCanciones.setForeground(Color.WHITE);
        lblCanciones.setFont(lblCanciones.getFont().deriveFont(Font.BOLD, 16f));
        lblCanciones.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        for (Cancion c : album.getCanciones()) {
            modeloCanciones.addElement(c);
        }

        JList<Cancion> lstCanciones = new JList<>(modeloCanciones);
        lstCanciones.setCellRenderer(new CancionCellRenderer(favoritoKeys));
        lstCanciones.setBackground(new Color(25, 25, 25));
        lstCanciones.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = lstCanciones.locationToIndex(e.getPoint());
                if (index < 0) return;
                Rectangle bounds = lstCanciones.getCellBounds(index, index);
                Cancion cancion = modeloCanciones.get(index);

                if (e.getX() > bounds.width - 44) {
                    FavoritosHelper.alternar(FrmDetalleAlbum.this, favoritoKeys, correoUsuarioActual,
                            "cancion", cancion.getId().toHexString(), album.getGenero());
                    lstCanciones.repaint();
                } else if (e.getClickCount() == 2) {
                    new FrmDetalleCancion(correoUsuarioActual, cancion.getId().toHexString()).setVisible(true);
                    FrmDetalleAlbum.this.dispose();
                }
            }
        });
        JScrollPane scrollCanciones = new JScrollPane(lstCanciones);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(25, 25, 25));
        panelNorte.add(panelSuperior, BorderLayout.NORTH);
        panelNorte.add(panelCabecera, BorderLayout.SOUTH);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(new Color(25, 25, 25));
        panelCentro.add(lblCanciones, BorderLayout.NORTH);
        panelCentro.add(scrollCanciones, BorderLayout.CENTER);

        getContentPane().add(panelNorte, BorderLayout.NORTH);
        getContentPane().add(panelCentro, BorderLayout.CENTER);
    }

    private void actualizarBotonFavorito() {
        boolean esFav = FavoritosHelper.esFavorito(favoritoKeys, "album", album.getId().toHexString());
        btnFavAlbum.setText(esFav ? "♥ En favoritos" : "♡ Agregar a favoritos");
        btnFavAlbum.setForeground(esFav ? FavoritosHelper.MORADO : FavoritosHelper.GRIS);
    }

    private static class CancionCellRenderer extends JPanel implements ListCellRenderer<Cancion> {
        private final Set<String> favoritoKeys;
        private final JLabel lblTitulo = new JLabel();
        private final JLabel lblDuracion = new JLabel();
        private final JLabel lblCorazon = new JLabel("♥");

        CancionCellRenderer(Set<String> favoritoKeys) {
            this.favoritoKeys = favoritoKeys;
            setLayout(new BorderLayout(10, 0));
            setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 8));
            lblTitulo.setForeground(Color.WHITE);
            lblDuracion.setForeground(new Color(180, 180, 180));
            lblCorazon.setFont(lblCorazon.getFont().deriveFont(18f));
            lblCorazon.setHorizontalAlignment(SwingConstants.CENTER);
            lblCorazon.setPreferredSize(new Dimension(36, 30));

            JPanel centro = new JPanel(new BorderLayout());
            centro.setOpaque(false);
            centro.add(lblTitulo, BorderLayout.WEST);
            centro.add(lblDuracion, BorderLayout.EAST);

            add(centro, BorderLayout.CENTER);
            add(lblCorazon, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Cancion> list, Cancion cancion, int index,
                boolean isSelected, boolean cellHasFocus) {
            lblTitulo.setText(cancion.getTitulo());
            lblDuracion.setText(cancion.getDuracion());

            boolean esFav = FavoritosHelper.esFavorito(favoritoKeys, "cancion", cancion.getId().toHexString());
            lblCorazon.setForeground(esFav ? FavoritosHelper.MORADO : FavoritosHelper.GRIS);

            setBackground(isSelected ? new Color(60, 60, 60) : new Color(25, 25, 25));
            setOpaque(true);
            return this;
        }
    }
}
