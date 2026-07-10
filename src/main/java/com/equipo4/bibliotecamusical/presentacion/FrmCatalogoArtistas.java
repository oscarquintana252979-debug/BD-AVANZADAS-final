package com.equipo4.bibliotecamusical.presentacion;

import com.equipo4.bibliotecamusical.entidades.Artista;
import com.equipo4.bibliotecamusical.implementaciones.ConexionDAO;
import com.equipo4.bibliotecamusical.persistencia.ArtistaDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
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

public class FrmCatalogoArtistas extends javax.swing.JFrame {

    private final String correoUsuarioActual;
    private final ArtistaDAO artistaDAO = new ArtistaDAO(new ConexionDAO());
    private final DefaultListModel<Artista> modeloLista = new DefaultListModel<>();
    private final Set<String> favoritoKeys;
    private JList<Artista> lstArtistas;
    private JTextField txtBuscar;
    private JCheckBox chkNombre;
    private JCheckBox chkGenero;

    public FrmCatalogoArtistas(String correo) {
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

        JLabel lblTitulo = new JLabel("Catálogo de Artistas", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));

        panelSuperior.add(btnVolver, BorderLayout.WEST);
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(new Color(25, 25, 25));
        txtBuscar = new JTextField(18);
        chkNombre = new JCheckBox("Nombre");
        chkNombre.setForeground(Color.WHITE);
        chkNombre.setOpaque(false);
        chkGenero = new JCheckBox("Género");
        chkGenero.setForeground(Color.WHITE);
        chkGenero.setOpaque(false);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar());

        panelBusqueda.add(new JLabel("Buscar:") {{ setForeground(Color.WHITE); }});
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(chkNombre);
        panelBusqueda.add(chkGenero);
        panelBusqueda.add(btnBuscar);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(25, 25, 25));
        panelNorte.add(panelSuperior, BorderLayout.NORTH);
        panelNorte.add(panelBusqueda, BorderLayout.SOUTH);

        lstArtistas = new JList<>(modeloLista);
        lstArtistas.setCellRenderer(new ArtistaCellRenderer(favoritoKeys));
        lstArtistas.setBackground(new Color(25, 25, 25));
        lstArtistas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = lstArtistas.locationToIndex(e.getPoint());
                if (index < 0) return;
                Rectangle bounds = lstArtistas.getCellBounds(index, index);
                boolean clicEnCorazon = e.getX() > bounds.width - 44;
                Artista artista = modeloLista.get(index);

                if (clicEnCorazon) {
                    FavoritosHelper.alternar(FrmCatalogoArtistas.this, favoritoKeys, correoUsuarioActual,
                            "artista", artista.getId().toHexString(), artista.getGenero());
                    lstArtistas.repaint();
                } else if (e.getClickCount() == 2) {
                    abrirDetalle(artista);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(lstArtistas);

        getContentPane().add(panelNorte, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);
    }

    private void buscar() {
       String texto = txtBuscar.getText().trim();
        List<Artista> resultado = artistaDAO.buscarArtistas(texto, chkNombre.isSelected(), chkGenero.isSelected());
        
        java.util.List<String> generosRestringidos = new java.util.ArrayList<>();
        try {
            com.equipo4.bibliotecamusical.persistencia.UsuarioDAO usuarioDAO = new com.equipo4.bibliotecamusical.persistencia.UsuarioDAO(new com.equipo4.bibliotecamusical.implementaciones.ConexionDAO());
            com.equipo4.bibliotecamusical.entidades.Usuario usuarioBD = usuarioDAO.consultarPerfilPorCorreo(correoUsuarioActual);
            if (usuarioBD != null && usuarioBD.getGenerosNoDeseados() != null) {
                generosRestringidos = usuarioBD.getGenerosNoDeseados();
            }
        } catch (Exception e) {
            System.out.println("Error al cargar restricciones: " + e.getMessage());
        }

        modeloLista.clear();
        boolean restriccionAplicada = false;
        
        for (Artista a : resultado) {
            boolean estaBloqueado = false;
            String generoArtista = a.getGenero() != null ? a.getGenero().toLowerCase().trim() : "";
            
            for (String gRestringido : generosRestringidos) {
                if (generoArtista.contains(gRestringido.toLowerCase().trim())) {
                    estaBloqueado = true;
                    break;
                }
            }
            
            if (!estaBloqueado) {
                modeloLista.addElement(a);
            } else {
                restriccionAplicada = true;
            }
        }
        
        if (modeloLista.isEmpty() && restriccionAplicada && !texto.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "El contenido ha sido ocultado.\nRestringido por usuario.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarArtistas(List<Artista> artistas) {
        modeloLista.clear();
        for (Artista a : artistas) {
            modeloLista.addElement(a);
        }
    }

    private void abrirDetalle(Artista artista) {
        new FrmDetalleArtista(correoUsuarioActual, artista.getId().toHexString()).setVisible(true);
        this.dispose();
    }

    static ImageIcon cargarIcono(String nombreArchivo, int ancho, int alto) {
        if (nombreArchivo == null) return null;
        java.net.URL url = FrmCatalogoArtistas.class.getResource("/imagenes/" + nombreArchivo);
        if (url == null) return null;
        Image img = new ImageIcon(url).getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private static class ArtistaCellRenderer extends JPanel implements ListCellRenderer<Artista> {
        private final Set<String> favoritoKeys;
        private final JLabel lblImagen = new JLabel();
        private final JLabel lblNombre = new JLabel();
        private final JLabel lblGenero = new JLabel();
        private final JLabel lblCorazon = new JLabel("♥");

        ArtistaCellRenderer(Set<String> favoritoKeys) {
            this.favoritoKeys = favoritoKeys;
            setLayout(new BorderLayout(10, 0));
            setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

            lblImagen.setPreferredSize(new java.awt.Dimension(48, 48));
            lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
            lblImagen.setText("🎵");

            JPanel textos = new JPanel(new java.awt.GridLayout(2, 1));
            textos.setOpaque(false);
            lblNombre.setForeground(Color.WHITE);
            lblNombre.setFont(lblNombre.getFont().deriveFont(Font.BOLD));
            lblGenero.setForeground(new Color(180, 180, 180));
            textos.add(lblNombre);
            textos.add(lblGenero);

            lblCorazon.setFont(lblCorazon.getFont().deriveFont(20f));
            lblCorazon.setHorizontalAlignment(SwingConstants.CENTER);
            lblCorazon.setPreferredSize(new java.awt.Dimension(36, 36));

            add(lblImagen, BorderLayout.WEST);
            add(textos, BorderLayout.CENTER);
            add(lblCorazon, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Artista> list, Artista artista, int index,
                boolean isSelected, boolean cellHasFocus) {
            lblNombre.setText(artista.getNombre());
            lblGenero.setText((artista.getTipo() != null ? artista.getTipo() + " · " : "") + artista.getGenero());

            ImageIcon icono = cargarIcono(artista.getImagen(), 48, 48);
            if (icono != null) {
                lblImagen.setIcon(icono);
                lblImagen.setText(null);
            } else {
                lblImagen.setIcon(null);
                lblImagen.setText("🎵");
            }

            boolean esFav = FavoritosHelper.esFavorito(favoritoKeys, "artista", artista.getId().toHexString());
            lblCorazon.setForeground(esFav ? FavoritosHelper.MORADO : FavoritosHelper.GRIS);

            setBackground(isSelected ? new Color(60, 60, 60) : new Color(25, 25, 25));
            setOpaque(true);
            return this;
        }
    }
}
