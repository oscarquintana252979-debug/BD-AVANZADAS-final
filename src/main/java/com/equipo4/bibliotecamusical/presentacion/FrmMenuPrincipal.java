package com.equipo4.bibliotecamusical.presentacion;

import com.equipo4.bibliotecamusical.entidades.Album;
import com.equipo4.bibliotecamusical.entidades.Artista;
import com.equipo4.bibliotecamusical.entidades.Cancion;
import com.equipo4.bibliotecamusical.implementaciones.ConexionDAO;
import com.equipo4.bibliotecamusical.persistencia.ArtistaDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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

public class FrmMenuPrincipal extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmMenuPrincipal.class.getName());

    private final String correoUsuarioActual;
    private final ArtistaDAO artistaDAO = new ArtistaDAO(new ConexionDAO());
    private final DefaultListModel<Object> modeloResultados = new DefaultListModel<>();
    private final Set<String> favoritoKeys;

    private JButton btnIconoPerfilMenu;
    private JButton btnIconoPerfil;
    private JTextField txtBuscarGlobal;
    private JCheckBox chkNombreGlobal;
    private JCheckBox chkGeneroGlobal;
    private JList<Object> lstResultados;

    public FrmMenuPrincipal(String correo) {
        this.correoUsuarioActual = correo;
        this.favoritoKeys = FavoritosHelper.cargarClaves(correo);
        initComponents();

        btnIconoPerfilMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIconoPerfil.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        this.getContentPane().setBackground(new Color(25, 25, 25));

        try {
            java.net.URL urlLogo = getClass().getResource("/imagenes/logo_musica.png");
            if (urlLogo != null) {
                ImageIcon iconOriginal = new ImageIcon(urlLogo);
                java.awt.Image imgEscalada = iconOriginal.getImage().getScaledInstance(
                        btnIconoPerfilMenu.getWidth(), btnIconoPerfilMenu.getHeight(), java.awt.Image.SCALE_SMOOTH);
                btnIconoPerfilMenu.setIcon(new ImageIcon(imgEscalada));
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la foto de perfil en el menú: " + e.getMessage());
        }
        try {
            java.net.URL urlPerfil = getClass().getResource("/imagenes/imagen-de-perfil2.png");
            if (urlPerfil != null) {
                ImageIcon iconOriginal = new ImageIcon(urlPerfil);
                java.awt.Image imgEscalada = iconOriginal.getImage().getScaledInstance(
                        btnIconoPerfil.getWidth(), btnIconoPerfil.getHeight(), java.awt.Image.SCALE_SMOOTH);
                btnIconoPerfil.setIcon(new ImageIcon(imgEscalada));
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la foto de perfil: " + e.getMessage());
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(760, 620);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        btnIconoPerfilMenu = new JButton();
        btnIconoPerfilMenu.setBorderPainted(false);
        btnIconoPerfilMenu.setContentAreaFilled(false);
        btnIconoPerfilMenu.setPreferredSize(new Dimension(55, 55));
        btnIconoPerfilMenu.addActionListener(e -> {
            new FrmMenuPrincipal(correoUsuarioActual).setVisible(true);
            this.dispose();
        });

        btnIconoPerfil = new JButton();
        btnIconoPerfil.setBorderPainted(false);
        btnIconoPerfil.setContentAreaFilled(false);
        btnIconoPerfil.setPreferredSize(new Dimension(55, 55));
        btnIconoPerfil.addActionListener(e -> {
            new FrmPerfil(correoUsuarioActual).setVisible(true);
            this.dispose();
        });

        JLabel jLabel1 = new JLabel("CATALOGO MUSICA", SwingConstants.CENTER);
        jLabel1.setForeground(Color.WHITE);
        jLabel1.setFont(jLabel1.getFont().deriveFont(Font.BOLD, 16f));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(25, 25, 25));
        panelSuperior.add(btnIconoPerfilMenu, BorderLayout.WEST);
        panelSuperior.add(jLabel1, BorderLayout.CENTER);
        panelSuperior.add(btnIconoPerfil, BorderLayout.EAST);

        getContentPane().add(panelSuperior, BorderLayout.NORTH);
        getContentPane().add(construirPanelNav(), BorderLayout.WEST);
        getContentPane().add(construirPanelBusquedaGlobal(), BorderLayout.CENTER);
    }

    private JPanel construirPanelNav() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(25, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JButton btnCatalogoArtistas = crearBotonNav("Artistas");
        btnCatalogoArtistas.addActionListener(e -> {
            new FrmCatalogoArtistas(correoUsuarioActual).setVisible(true);
            this.dispose();
        });

        JButton btnCatalogoAlbumes = crearBotonNav("Álbumes");
        btnCatalogoAlbumes.addActionListener(e -> {
            new FrmCatalogoAlbumes(correoUsuarioActual).setVisible(true);
            this.dispose();
        });

        JButton btnCatalogoCanciones = crearBotonNav("Canciones");
        btnCatalogoCanciones.addActionListener(e -> {
            new FrmCatalogoCanciones(correoUsuarioActual).setVisible(true);
            this.dispose();
        });

        JButton btnPoblarArtistas = crearBotonNav("Poblar Artistas (demo)");
        btnPoblarArtistas.setBackground(new Color(60, 60, 60));
        btnPoblarArtistas.addActionListener(this::btnPoblarArtistasActionPerformed);

        panel.add(btnCatalogoArtistas);
        panel.add(javax.swing.Box.createVerticalStrut(10));
        panel.add(btnCatalogoAlbumes);
        panel.add(javax.swing.Box.createVerticalStrut(10));
        panel.add(btnCatalogoCanciones);
        panel.add(javax.swing.Box.createVerticalStrut(30));
        panel.add(btnPoblarArtistas);

        return panel;
    }

    private JButton crearBotonNav(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(255, 0, 255));
        boton.setForeground(Color.WHITE);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(180, 32));
        boton.setPreferredSize(new Dimension(180, 32));
        return boton;
    }

    private JPanel construirPanelBusquedaGlobal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(new Color(25, 25, 25));

        txtBuscarGlobal = new JTextField(18);
        chkNombreGlobal = new JCheckBox("Nombre");
        chkNombreGlobal.setForeground(Color.WHITE);
        chkNombreGlobal.setOpaque(false);
        chkGeneroGlobal = new JCheckBox("Género");
        chkGeneroGlobal.setForeground(Color.WHITE);
        chkGeneroGlobal.setOpaque(false);
        JButton btnBuscarGlobal = new JButton("Buscar");
        btnBuscarGlobal.addActionListener(e -> buscarGlobal());

        JLabel lblBuscar = new JLabel("Buscar artistas, álbumes o canciones:");
        lblBuscar.setForeground(Color.WHITE);

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscarGlobal);
        panelBusqueda.add(chkNombreGlobal);
        panelBusqueda.add(chkGeneroGlobal);
        panelBusqueda.add(btnBuscarGlobal);

        lstResultados = new JList<>(modeloResultados);
        lstResultados.setCellRenderer(new ResultadoCellRenderer(favoritoKeys));
        lstResultados.setBackground(new Color(25, 25, 25));
        lstResultados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = lstResultados.locationToIndex(e.getPoint());
                if (index < 0) return;
                Rectangle bounds = lstResultados.getCellBounds(index, index);
                Object item = modeloResultados.get(index);

                if (e.getX() > bounds.width - 44) {
                    alternarFavorito(item);
                    lstResultados.repaint();
                } else if (e.getClickCount() == 2) {
                    abrirDetalle(item);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(lstResultados);

        panel.add(panelBusqueda, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void buscarGlobal() {
        String texto = txtBuscarGlobal.getText().trim();
        boolean porNombre = chkNombreGlobal.isSelected();
        boolean porGenero = chkGeneroGlobal.isSelected();

        modeloResultados.clear();
        for (Artista a : artistaDAO.buscarArtistas(texto, porNombre, porGenero)) {
            modeloResultados.addElement(a);
        }
        for (Album a : artistaDAO.buscarAlbumes(texto, porNombre, porGenero, false)) {
            modeloResultados.addElement(a);
        }
        for (Cancion c : artistaDAO.buscarCanciones(texto, porNombre, porGenero)) {
            modeloResultados.addElement(c);
        }
    }

    private void abrirDetalle(Object item) {
        if (item instanceof Artista a) {
            new FrmDetalleArtista(correoUsuarioActual, a.getId().toHexString()).setVisible(true);
        } else if (item instanceof Album a) {
            new FrmDetalleAlbum(correoUsuarioActual, a.getId().toHexString()).setVisible(true);
        } else if (item instanceof Cancion c) {
            new FrmDetalleCancion(correoUsuarioActual, c.getId().toHexString()).setVisible(true);
        }
        this.dispose();
    }

    private void alternarFavorito(Object item) {
        if (item instanceof Artista a) {
            FavoritosHelper.alternar(this, favoritoKeys, correoUsuarioActual, "artista", a.getId().toHexString(), a.getGenero());
        } else if (item instanceof Album a) {
            FavoritosHelper.alternar(this, favoritoKeys, correoUsuarioActual, "album", a.getId().toHexString(), a.getGenero());
        } else if (item instanceof Cancion c) {
            FavoritosHelper.alternar(this, favoritoKeys, correoUsuarioActual, "cancion", c.getId().toHexString(), c.getGenero());
        }
    }

    private void btnPoblarArtistasActionPerformed(java.awt.event.ActionEvent evt) {
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(this,
                "Esto BORRARÁ la colección 'artistas' actual y la reemplazará con 30 solistas + 30 bandas de prueba. ¿Continuar?",
                "Confirmar poblado de datos", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
        if (confirmacion != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }
        try {
            artistaDAO.poblarArtistasDePrueba();
            javax.swing.JOptionPane.showMessageDialog(this, "Se insertaron 60 artistas, 120 álbumes y 360 canciones.", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (com.equipo4.bibliotecamusical.excepciones.PersistenciaException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al poblar datos: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class ResultadoCellRenderer extends JPanel implements ListCellRenderer<Object> {
        private final Set<String> favoritoKeys;
        private final JLabel lblTipo = new JLabel();
        private final JLabel lblNombre = new JLabel();
        private final JLabel lblInfo = new JLabel();
        private final JLabel lblCorazon = new JLabel("♥");

        ResultadoCellRenderer(Set<String> favoritoKeys) {
            this.favoritoKeys = favoritoKeys;
            setLayout(new BorderLayout(10, 0));
            setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

            lblTipo.setPreferredSize(new Dimension(70, 30));
            lblTipo.setForeground(new Color(180, 180, 180));
            lblTipo.setFont(lblTipo.getFont().deriveFont(Font.ITALIC, 11f));

            JPanel textos = new JPanel(new GridLayout(2, 1));
            textos.setOpaque(false);
            lblNombre.setForeground(Color.WHITE);
            lblNombre.setFont(lblNombre.getFont().deriveFont(Font.BOLD));
            lblInfo.setForeground(new Color(180, 180, 180));
            textos.add(lblNombre);
            textos.add(lblInfo);

            lblCorazon.setFont(lblCorazon.getFont().deriveFont(20f));
            lblCorazon.setHorizontalAlignment(SwingConstants.CENTER);
            lblCorazon.setPreferredSize(new Dimension(36, 36));

            add(lblTipo, BorderLayout.WEST);
            add(textos, BorderLayout.CENTER);
            add(lblCorazon, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object item, int index,
                boolean isSelected, boolean cellHasFocus) {
            String tipo;
            String elementoId;
            String genero;

            if (item instanceof Artista a) {
                tipo = "artista";
                elementoId = a.getId().toHexString();
                genero = a.getGenero();
                lblTipo.setText("Artista");
                lblNombre.setText(a.getNombre());
                lblInfo.setText(a.getTipo() + " · " + a.getGenero());
            } else if (item instanceof Album a) {
                tipo = "album";
                elementoId = a.getId().toHexString();
                genero = a.getGenero();
                lblTipo.setText("Álbum");
                lblNombre.setText(a.getNombre());
                String artista = a.getNombreArtista() != null ? "por " + a.getNombreArtista() + " · " : "";
                lblInfo.setText(artista + a.getGenero());
            } else if (item instanceof Cancion c) {
                tipo = "cancion";
                elementoId = c.getId().toHexString();
                genero = c.getGenero();
                lblTipo.setText("Canción");
                lblNombre.setText(c.getTitulo());
                String artista = c.getNombreArtista() != null ? "por " + c.getNombreArtista() + " · " : "";
                lblInfo.setText(artista + c.getDuracion());
            } else {
                tipo = null;
                elementoId = null;
            }

            if (tipo != null) {
                boolean esFav = FavoritosHelper.esFavorito(favoritoKeys, tipo, elementoId);
                lblCorazon.setForeground(esFav ? FavoritosHelper.MORADO : FavoritosHelper.GRIS);
            }

            setBackground(isSelected ? new Color(60, 60, 60) : new Color(25, 25, 25));
            setOpaque(true);
            return this;
        }
    }
}
