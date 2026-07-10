package com.equipo4.bibliotecamusical.presentacion;

import com.equipo4.bibliotecamusical.entidades.Album;
import com.equipo4.bibliotecamusical.entidades.Artista;
import com.equipo4.bibliotecamusical.entidades.Integrante;
import com.equipo4.bibliotecamusical.entidades.Persona;
import com.equipo4.bibliotecamusical.excepciones.ElementoNoEncontradoException;
import com.equipo4.bibliotecamusical.excepciones.GeneroRestringidoException;
import com.equipo4.bibliotecamusical.implementaciones.ConexionDAO;
import com.equipo4.bibliotecamusical.persistencia.ArtistaDAO;
import com.equipo4.bibliotecamusical.persistencia.PersonaDAO;
import com.equipo4.bibliotecamusical.persistencia.UsuarioDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class FrmDetalleArtista extends javax.swing.JFrame {

    private final String correoUsuarioActual;
    private final ArtistaDAO artistaDAO = new ArtistaDAO(new ConexionDAO());
    private final PersonaDAO personaDAO = new PersonaDAO(new ConexionDAO());
    private Artista artista;
    private DefaultTableModel modeloIntegrantes;
    private JCheckBox chkMostrarInactivos;
    private DefaultListModel<Album> modeloAlbumes = new DefaultListModel<>();

    public FrmDetalleArtista(String correo, String idArtista) {
        this.correoUsuarioActual = correo;
        try {
            this.artista = artistaDAO.buscarPorId(idArtista);
        } catch (ElementoNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            new FrmCatalogoArtistas(correo).setVisible(true);
            this.dispose();
            return;
        }
        initComponents();
        this.getContentPane().setBackground(new Color(25, 25, 25));
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(620, 700);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(25, 25, 25));
        JButton btnVolver = new JButton("< Catálogo");
        btnVolver.addActionListener(e -> {
            new FrmCatalogoArtistas(correoUsuarioActual).setVisible(true);
            this.dispose();
        });
        panelSuperior.add(btnVolver, BorderLayout.WEST);

        JPanel panelCabecera = new JPanel();
        panelCabecera.setLayout(new javax.swing.BoxLayout(panelCabecera, javax.swing.BoxLayout.Y_AXIS));
        panelCabecera.setBackground(new Color(25, 25, 25));
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblNombre = new JLabel(artista.getNombre(), SwingConstants.CENTER);
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(lblNombre.getFont().deriveFont(Font.BOLD, 22f));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblGenero = new JLabel("Género: " + artista.getGenero(), SwingConstants.CENTER);
        lblGenero.setForeground(new Color(180, 180, 180));
        lblGenero.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCabecera.add(lblNombre);
        panelCabecera.add(lblGenero);

        if ("Solista".equals(artista.getTipo()) && artista.getPersonaId() != null) {
            try {
                Persona persona = personaDAO.buscarPorId(artista.getPersonaId().toHexString());
                JLabel lblPersona = new JLabel("Nombre real: " + persona.getNombreCompleto(), SwingConstants.CENTER);
                lblPersona.setForeground(new Color(180, 180, 180));
                lblPersona.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelCabecera.add(lblPersona);
            } catch (ElementoNoEncontradoException ignored) {
            }
        }

        JButton btnFavArtista = new JButton("♥ Agregar a favoritos");
        btnFavArtista.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFavArtista.addActionListener(e -> favoritearArtista());
        panelCabecera.add(btnFavArtista);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(new Color(25, 25, 25));

        if ("Banda".equals(artista.getTipo())) {
            panelCentro.add(construirPanelIntegrantes(), BorderLayout.NORTH);
        }
        panelCentro.add(construirPanelAlbumes(), BorderLayout.CENTER);

        JScrollPane scrollCentro = new JScrollPane(panelCentro);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(25, 25, 25));
        panelNorte.add(panelSuperior, BorderLayout.NORTH);
        panelNorte.add(panelCabecera, BorderLayout.SOUTH);

        getContentPane().add(panelNorte, BorderLayout.NORTH);
        getContentPane().add(scrollCentro, BorderLayout.CENTER);
    }

    private JPanel construirPanelIntegrantes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Integrantes");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));

        chkMostrarInactivos = new JCheckBox("Mostrar inactivos");
        chkMostrarInactivos.setForeground(Color.WHITE);
        chkMostrarInactivos.setOpaque(false);
        chkMostrarInactivos.addActionListener(e -> cargarIntegrantes());

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.add(titulo, BorderLayout.WEST);
        panelTitulo.add(chkMostrarInactivos, BorderLayout.EAST);

        modeloIntegrantes = new DefaultTableModel(new Object[]{"Nombre", "Rol", "Ingreso", "Salida", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabla = new JTable(modeloIntegrantes);
        tabla.setRowHeight(24);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setPreferredSize(new Dimension(580, 160));

        panel.add(panelTitulo, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);

        cargarIntegrantes();
        return panel;
    }

    private void cargarIntegrantes() {
        modeloIntegrantes.setRowCount(0);
        boolean mostrarInactivos = chkMostrarInactivos.isSelected();
        for (Integrante integrante : artista.getIntegrantes()) {
            boolean activo = "Activo".equalsIgnoreCase(integrante.getEstadoActividad());
            if (!activo && !mostrarInactivos) {
                continue;
            }
            String nombreCompleto = integrante.getPersonaId().toHexString();
            try {
                Persona persona = personaDAO.buscarPorId(integrante.getPersonaId().toHexString());
                nombreCompleto = persona.getNombreCompleto();
            } catch (ElementoNoEncontradoException ignored) {
            }
            modeloIntegrantes.addRow(new Object[]{
                nombreCompleto, integrante.getRol(), integrante.getFechaIngreso(),
                integrante.getFechaSalida(), integrante.getEstadoActividad()
            });
        }
    }

    private JPanel construirPanelAlbumes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Álbumes");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));

        for (Album album : artista.getAlbumes()) {
            modeloAlbumes.addElement(album);
        }

        JList<Album> lstAlbumes = new JList<>(modeloAlbumes);
        lstAlbumes.setCellRenderer(new AlbumCellRenderer());
        lstAlbumes.setBackground(new Color(25, 25, 25));
        lstAlbumes.setVisibleRowCount(Math.max(1, modeloAlbumes.size()));
        lstAlbumes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = lstAlbumes.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Album album = modeloAlbumes.get(index);
                        new FrmDetalleAlbum(correoUsuarioActual, album.getId().toHexString()).setVisible(true);
                        FrmDetalleArtista.this.dispose();
                    }
                }
            }
        });

        JScrollPane scrollAlbumes = new JScrollPane(lstAlbumes);
        scrollAlbumes.setPreferredSize(new Dimension(580, 220));

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scrollAlbumes, BorderLayout.CENTER);
        return panel;
    }

    private void favoritearArtista() {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO(new ConexionDAO());
            usuarioDAO.agregarAFavoritos(correoUsuarioActual, "artista", artista.getId().toHexString(), artista.getGenero());
            JOptionPane.showMessageDialog(this, "'" + artista.getNombre() + "' agregado a favoritos.");
        } catch (GeneroRestringidoException | ElementoNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private static class AlbumCellRenderer extends JPanel implements ListCellRenderer<Album> {
        private final JLabel lblImagen = new JLabel();
        private final JLabel lblNombre = new JLabel();
        private final JLabel lblFecha = new JLabel();

        AlbumCellRenderer() {
            setLayout(new BorderLayout(10, 0));
            setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
            lblImagen.setPreferredSize(new Dimension(40, 40));
            lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
            lblImagen.setText("💿");

            JPanel textos = new JPanel(new GridLayout(2, 1));
            textos.setOpaque(false);
            lblNombre.setForeground(Color.WHITE);
            lblNombre.setFont(lblNombre.getFont().deriveFont(Font.BOLD));
            lblFecha.setForeground(new Color(180, 180, 180));
            textos.add(lblNombre);
            textos.add(lblFecha);

            add(lblImagen, BorderLayout.WEST);
            add(textos, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Album> list, Album album, int index,
                boolean isSelected, boolean cellHasFocus) {
            lblNombre.setText(album.getNombre());
            lblFecha.setText(album.getFechaLanzamiento() + " · " + album.getGenero());

            ImageIcon icono = FrmCatalogoArtistas.cargarIcono(album.getImagenPortada(), 40, 40);
            if (icono != null) {
                lblImagen.setIcon(icono);
                lblImagen.setText(null);
            } else {
                lblImagen.setIcon(null);
                lblImagen.setText("💿");
            }

            setBackground(isSelected ? new Color(60, 60, 60) : new Color(25, 25, 25));
            setOpaque(true);
            return this;
        }
    }
}
