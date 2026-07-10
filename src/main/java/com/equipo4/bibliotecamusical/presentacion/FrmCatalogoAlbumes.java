package com.equipo4.bibliotecamusical.presentacion;

import com.equipo4.bibliotecamusical.entidades.Album;
import com.equipo4.bibliotecamusical.implementaciones.ConexionDAO;
import com.equipo4.bibliotecamusical.persistencia.ArtistaDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

public class FrmCatalogoAlbumes extends javax.swing.JFrame {

    private final String correoUsuarioActual;
    private final ArtistaDAO artistaDAO = new ArtistaDAO(new ConexionDAO());
    private final DefaultListModel<Album> modeloLista = new DefaultListModel<>();
    private final Set<String> favoritoKeys;
    private JList<Album> lstAlbumes;
    private JTextField txtBuscar;
    private JCheckBox chkNombre;
    private JCheckBox chkGenero;
    private JCheckBox chkFecha;

    public FrmCatalogoAlbumes(String correo) {
        this.correoUsuarioActual = correo;
        this.favoritoKeys = FavoritosHelper.cargarClaves(correo);
        initComponents();
        this.getContentPane().setBackground(new Color(25, 25, 25));
        buscar();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(520, 620);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(25, 25, 25));
        JButton btnVolver = new JButton("< Menú");
        btnVolver.addActionListener(e -> {
            new FrmMenuPrincipal(correoUsuarioActual).setVisible(true);
            this.dispose();
        });
        JLabel lblTitulo = new JLabel("Catálogo de Álbumes", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        panelSuperior.add(btnVolver, BorderLayout.WEST);
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(new Color(25, 25, 25));
        txtBuscar = new JTextField(16);
        chkNombre = new JCheckBox("Nombre");
        chkNombre.setForeground(Color.WHITE);
        chkNombre.setOpaque(false);
        chkGenero = new JCheckBox("Género");
        chkGenero.setForeground(Color.WHITE);
        chkGenero.setOpaque(false);
        chkFecha = new JCheckBox("Fecha lanzamiento");
        chkFecha.setForeground(Color.WHITE);
        chkFecha.setOpaque(false);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar());

        panelBusqueda.add(new JLabel("Buscar:") {{ setForeground(Color.WHITE); }});
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(chkNombre);
        panelBusqueda.add(chkGenero);
        panelBusqueda.add(chkFecha);
        panelBusqueda.add(btnBuscar);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(25, 25, 25));
        panelNorte.add(panelSuperior, BorderLayout.NORTH);
        panelNorte.add(panelBusqueda, BorderLayout.SOUTH);

        lstAlbumes = new JList<>(modeloLista);
        lstAlbumes.setCellRenderer(new AlbumCellRenderer(favoritoKeys));
        lstAlbumes.setBackground(new Color(25, 25, 25));
        lstAlbumes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = lstAlbumes.locationToIndex(e.getPoint());
                if (index < 0) return;
                Rectangle bounds = lstAlbumes.getCellBounds(index, index);
                boolean clicEnCorazon = e.getX() > bounds.width - 44;
                Album album = modeloLista.get(index);

                if (clicEnCorazon) {
                    FavoritosHelper.alternar(FrmCatalogoAlbumes.this, favoritoKeys, correoUsuarioActual,
                            "album", album.getId().toHexString(), album.getGenero());
                    lstAlbumes.repaint();
                } else if (e.getClickCount() == 2) {
                    new FrmDetalleAlbum(correoUsuarioActual, album.getId().toHexString()).setVisible(true);
                    FrmCatalogoAlbumes.this.dispose();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(lstAlbumes);

        getContentPane().add(panelNorte, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim();
        List<Album> resultado = artistaDAO.buscarAlbumes(texto, chkNombre.isSelected(), chkGenero.isSelected(), chkFecha.isSelected());
        modeloLista.clear();
        for (Album a : resultado) {
            modeloLista.addElement(a);
        }
    }

    private static class AlbumCellRenderer extends JPanel implements ListCellRenderer<Album> {
        private final Set<String> favoritoKeys;
        private final JLabel lblImagen = new JLabel();
        private final JLabel lblNombre = new JLabel();
        private final JLabel lblInfo = new JLabel();
        private final JLabel lblCorazon = new JLabel("♥");

        AlbumCellRenderer(Set<String> favoritoKeys) {
            this.favoritoKeys = favoritoKeys;
            setLayout(new BorderLayout(10, 0));
            setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
            lblImagen.setPreferredSize(new java.awt.Dimension(48, 48));
            lblImagen.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel textos = new JPanel(new GridLayout(2, 1));
            textos.setOpaque(false);
            lblNombre.setForeground(Color.WHITE);
            lblNombre.setFont(lblNombre.getFont().deriveFont(Font.BOLD));
            lblInfo.setForeground(new Color(180, 180, 180));
            textos.add(lblNombre);
            textos.add(lblInfo);

            lblCorazon.setFont(lblCorazon.getFont().deriveFont(20f));
            lblCorazon.setHorizontalAlignment(SwingConstants.CENTER);
            lblCorazon.setPreferredSize(new java.awt.Dimension(36, 36));

            add(lblImagen, BorderLayout.WEST);
            add(textos, BorderLayout.CENTER);
            add(lblCorazon, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Album> list, Album album, int index,
                boolean isSelected, boolean cellHasFocus) {
            lblNombre.setText(album.getNombre());
            String artistaTxt = album.getNombreArtista() != null ? "por " + album.getNombreArtista() + " · " : "";
            lblInfo.setText(artistaTxt + album.getGenero() + " · " + album.getFechaLanzamiento());

            ImageIcon icono = FrmCatalogoArtistas.cargarIcono(album.getImagenPortada(), 48, 48);
            if (icono != null) {
                lblImagen.setIcon(icono);
                lblImagen.setText(null);
            } else {
                lblImagen.setIcon(null);
                lblImagen.setText("💿");
            }

            boolean esFav = FavoritosHelper.esFavorito(favoritoKeys, "album", album.getId().toHexString());
            lblCorazon.setForeground(esFav ? FavoritosHelper.MORADO : FavoritosHelper.GRIS);

            setBackground(isSelected ? new Color(60, 60, 60) : new Color(25, 25, 25));
            setOpaque(true);
            return this;
        }
    }
}
