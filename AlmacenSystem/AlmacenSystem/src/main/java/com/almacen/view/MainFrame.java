package com.almacen.view;

import com.almacen.model.Usuario;
import com.almacen.util.Estilos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * PILAR OOP: HERENCIA
 * MainFrame extiende BaseFrame heredando toda la infraestructura de ventana.
 *
 * PILAR OOP: POLIMORFISMO
 * Implementa su propia versión de initComponents() y configurarLayout().
 */
public class MainFrame extends BaseFrame {

    private Usuario usuarioActual;
    private JButton btnUsuarios;
    private JButton btnProductos;
    private JButton btnCerrarSesion;

    public MainFrame(Usuario usuario) {
        // autoInit=false para poder asignar usuarioActual ANTES de initComponents/configurarLayout
        super("Sistema de Gestión de Almacén", 700, 460, false);
        this.usuarioActual = usuario;
        init(); // Ahora sí inicializar componentes (usuarioActual ya no es null)
    }

    @Override
    protected void initComponents() {
        btnUsuarios      = crearBotonMenu("👤", "Usuarios", "Gestionar cuentas de usuario");
        btnProductos     = crearBotonMenu("📦", "Productos", "Gestionar productos del almacén");
        btnCerrarSesion  = Estilos.crearBotonPeligro("⎋  Cerrar Sesión");

        btnUsuarios.addActionListener(e -> abrirGestionUsuarios());
        btnProductos.addActionListener(e -> abrirGestionProductos());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    @Override
    protected void configurarLayout() {
        panelPrincipal.setLayout(new BorderLayout());

        // ─── Header ───────────────────────────────────────────────────────────
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint grad = new GradientPaint(0,0, Estilos.PRIMARIO, getWidth(),0, new Color(21,101,192));
                g2.setPaint(grad);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(700, 80));
        header.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        JLabel lTitle = new JLabel("🏭  Sistema de Gestión de Almacén");
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lTitle.setForeground(Color.WHITE);
        JLabel lSub = new JLabel("Bienvenido, " + usuarioActual.getNombre() + " " + usuarioActual.getApellido());
        lSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lSub.setForeground(new Color(200, 220, 255));
        textos.add(lTitle);
        textos.add(lSub);
        header.add(textos, BorderLayout.CENTER);

        JPanel panelCerrar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelCerrar.setOpaque(false);
        btnCerrarSesion.setPreferredSize(new Dimension(150, 36));
        panelCerrar.add(btnCerrarSesion);
        header.add(panelCerrar, BorderLayout.EAST);

        // ─── Centro: botones de módulo ────────────────────────────────────────
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(Estilos.FONDO);

        JPanel contenedorBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        contenedorBotones.setBackground(Estilos.FONDO);
        contenedorBotones.add(btnUsuarios);
        contenedorBotones.add(btnProductos);

        // Subtítulo central
        JLabel lblMod = new JLabel("Seleccione un módulo para comenzar");
        lblMod.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMod.setForeground(Estilos.TEXTO_SEC);
        lblMod.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panelCentro = new JPanel(new BorderLayout(0, 10));
        panelCentro.setBackground(Estilos.FONDO);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelCentro.add(lblMod, BorderLayout.NORTH);
        panelCentro.add(contenedorBotones, BorderLayout.CENTER);

        panelPrincipal.add(header, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // Footer
        JLabel lblFooter = new JLabel("Sistema de Gestión de Almacén  •  v1.0", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(Estilos.TEXTO_SEC);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        panelPrincipal.add(lblFooter, BorderLayout.SOUTH);
    }

    // ─── Botón de módulo grande ────────────────────────────────────────────────

    private JButton crearBotonMenu(String emoji, String titulo, String desc) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sombra
                g2.setColor(new Color(0,0,0,25));
                g2.fillRoundRect(5, 5, getWidth()-5, getHeight()-5, 18, 18);

                // Fondo con hover
                Color bg = getModel().isRollover()
                    ? new Color(25, 118, 210)
                    : Estilos.PRIMARIO;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth()-5, getHeight()-5, 18, 18);

                // Emoji
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(emoji, (getWidth() - fm.stringWidth(emoji)) / 2 - 2, 90);

                // Título
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                fm = g2.getFontMetrics();
                g2.drawString(titulo, (getWidth() - fm.stringWidth(titulo)) / 2, 118);

                // Descripción
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.setColor(new Color(200, 220, 255));
                fm = g2.getFontMetrics();
                g2.drawString(desc, (getWidth() - fm.stringWidth(desc)) / 2, 138);

                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(230, 160));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ─── Navegación ───────────────────────────────────────────────────────────

    private void abrirGestionUsuarios() {
        new UsuariosFrame(this).setVisible(true);
    }

    private void abrirGestionProductos() {
        new ProductosFrame(this).setVisible(true);
    }

    private void cerrarSesion() {
        if (confirmar("¿Estás seguro de que deseas cerrar sesión?")) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }
}
