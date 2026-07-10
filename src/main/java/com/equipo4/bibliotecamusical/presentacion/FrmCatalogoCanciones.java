package com.equipo4.bibliotecamusical.presentacion;

import com.equipo4.bibliotecamusical.entidades.Cancion;
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

public class FrmCatalogoCanciones extends javax.swing.JFrame {

    private final String correoUsuarioActual;
    private final ArtistaDAO artistaDAO = new ArtistaDAO(new ConexionDAO());
    private final DefaultListModel<Cancion> modeloLista = new DefaultListModel<>();
    private final Set<String> favoritoKeys;
    private JList<Cancion> lstCanciones;
    private JTextField txtBuscar;
    private JCheckBox chkNombre;
    private JCheckBox chkGenero;

    public FrmCatalogoCanciones(String correo) {
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
        JLabel lblTitulo = new JLabel("Catálogo de Canciones", SwingConstants.CENTER);
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

        lstCanciones = new JList<>(modeloLista);
        lstCanciones.setCellRenderer(new CancionCellRenderer(favoritoKeys));
        lstCanciones.setBackground(new Color(25, 25, 25));
        lstCanciones.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = lstCanciones.locationToIndex(e.getPoint());
                if (index < 0) return;
                Rectangle bounds = lstCanciones.getCellBounds(index, index);
                Cancion cancion = modeloLista.get(index);

                if (e.getX() > bounds.width - 44) {
                    FavoritosHelper.alternar(FrmCatalogoCanciones.this, favoritoKeys, correoUsuarioActual, "cancion",
                            cancion.getId().toHexString(), cancion.getGenero());
                    lstCanciones.repaint();
                } else if (e.getClickCount() == 2) {
                    new FrmDetalleCancion(correoUsuarioActual, cancion.getId().toHexString()).setVisible(true);
                    FrmCatalogoCanciones.this.dispose();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(lstCanciones);

        getContentPane().add(panelNorte, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim();
        List<Cancion> resultado = artistaDAO.buscarCanciones(texto, chkNombre.isSelected(), chkGenero.isSelected());
        
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
        
        for (Cancion c : resultado) {
            boolean estaBloqueado = false;
            String generoCancion = c.getGenero() != null ? c.getGenero().toLowerCase().trim() : "";
            for (String gRestringido : generosRestringidos) {
                if (generoCancion.contains(gRestringido.toLowerCase().trim())) {
                    estaBloqueado = true;
                    break;
                }
            }
            if (!estaBloqueado) {
                modeloLista.addElement(c);
            } else {
                restriccionAplicada = true;
            }
        }
        
        if (modeloLista.isEmpty() && restriccionAplicada && !texto.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "El contenido ha sido ocultado.\nRestringido por usuario.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    static class CancionCellRenderer extends JPanel implements ListCellRenderer<Cancion> {
        private final Set<String> favoritoKeys;
        private final JLabel lblTitulo = new JLabel();
        private final JLabel lblInfo = new JLabel();
        private final JLabel lblCorazon = new JLabel("♥");

        CancionCellRenderer(Set<String> favoritoKeys) {
            this.favoritoKeys = favoritoKeys;
            setLayout(new BorderLayout(10, 0));
            setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

            JPanel textos = new JPanel(new GridLayout(2, 1));
            textos.setOpaque(false);
            lblTitulo.setForeground(Color.WHITE);
            lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD));
            lblInfo.setForeground(new Color(180, 180, 180));
            textos.add(lblTitulo);
            textos.add(lblInfo);

            lblCorazon.setFont(lblCorazon.getFont().deriveFont(20f));
            lblCorazon.setHorizontalAlignment(SwingConstants.CENTER);
            lblCorazon.setPreferredSize(new java.awt.Dimension(36, 36));

            add(textos, BorderLayout.CENTER);
            add(lblCorazon, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Cancion> list, Cancion cancion, int index,
                boolean isSelected, boolean cellHasFocus) {
            lblTitulo.setText(cancion.getTitulo());
            String contexto = cancion.getNombreArtista() != null ? "por " + cancion.getNombreArtista() : "";
            if (cancion.getNombreAlbum() != null) contexto += " · " + cancion.getNombreAlbum();
            lblInfo.setText(contexto + " · " + cancion.getDuracion());

            boolean esFav = FavoritosHelper.esFavorito(favoritoKeys, "cancion", cancion.getId().toHexString());
            lblCorazon.setForeground(esFav ? FavoritosHelper.MORADO : FavoritosHelper.GRIS);

            setBackground(isSelected ? new Color(60, 60, 60) : new Color(25, 25, 25));
            setOpaque(true);
            return this;
        }
    }
}
