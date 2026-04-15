package com.almacen.view;

import com.almacen.dao.UsuarioDAO;
import com.almacen.model.Usuario;
import com.almacen.util.Estilos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * PILAR OOP: HERENCIA
 * LoginFrame extiende BaseFrame, heredando configuración de ventana,
 * helpers de diálogos y el patrón de inicialización.
 *
 * PILAR OOP: POLIMORFISMO
 * Sobreescribe initComponents() y configurarLayout() con su lógica propia.
 */
public class LoginFrame extends BaseFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnEntrar;
    private JButton btnRegistrarse;
    private JLabel lblError;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginFrame() {
        super("Sistema de Gestión de Almacén — Login", 420, 560);
    }

    @Override
    protected void initComponents() {
        txtUsuario   = Estilos.crearCampo("Nombre de Usuario");
        txtPassword  = Estilos.crearCampoPassword();
        btnEntrar    = Estilos.crearBotonPrimario("Entrar");
        btnRegistrarse = Estilos.crearBotonSecundario("Registrarse");

        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setForeground(Estilos.PELIGRO);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);

        // Acciones
        btnEntrar.addActionListener(e -> iniciarSesion());
        btnRegistrarse.addActionListener(e -> abrirRegistro());

        // Enter para login
        ActionListener loginAction = e -> iniciarSesion();
        txtUsuario.addActionListener(loginAction);
        txtPassword.addActionListener(loginAction);
    }

    @Override
    protected void configurarLayout() {
        panelPrincipal.setBackground(Estilos.FONDO);

        // ─── Panel central (tarjeta) ───────────────────────────────────────
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra
                g2.setColor(new Color(0,0,0,30));
                g2.fillRoundRect(6, 6, getWidth()-6, getHeight()-6, 20, 20);
                // Fondo blanco
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth()-6, getHeight()-6, 20, 20);
                g2.dispose();
            }
        };
        card.setLayout(new GridBagLayout());
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(340, 420));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 30, 8, 30);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.gridx   = 0;
        gbc.weightx = 1.0;

        // Logo / Ícono
        JLabel lblIco = new JLabel("🏭", SwingConstants.CENTER);
        lblIco.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        gbc.gridy  = 0; gbc.insets = new Insets(28, 30, 4, 30);
        card.add(lblIco, gbc);

        // Título
        JLabel lblTitulo = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Estilos.PRIMARIO);
        gbc.gridy  = 1; gbc.insets = new Insets(0, 30, 2, 30);
        card.add(lblTitulo, gbc);

        JLabel lblSub = new JLabel("Gestión de Almacén", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(Estilos.TEXTO_SEC);
        gbc.gridy  = 2; gbc.insets = new Insets(0, 30, 20, 30);
        card.add(lblSub, gbc);

        // Labels
        gbc.insets = new Insets(4, 30, 2, 30);
        JLabel lUsuario = Estilos.crearLabel("Usuario");
        lUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 3; card.add(lUsuario, gbc);

        // Campo usuario
        txtUsuario.setPreferredSize(new Dimension(260, Estilos.ALTO_CAMPO));
        gbc.gridy = 4; gbc.insets = new Insets(2, 30, 8, 30);
        card.add(txtUsuario, gbc);

        // Label password
        JLabel lPass = Estilos.crearLabel("Contraseña");
        lPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 5; gbc.insets = new Insets(4, 30, 2, 30);
        card.add(lPass, gbc);

        // Campo password
        txtPassword.setPreferredSize(new Dimension(260, Estilos.ALTO_CAMPO));
        gbc.gridy = 6; gbc.insets = new Insets(2, 30, 8, 30);
        card.add(txtPassword, gbc);

        // Error
        gbc.gridy = 7; gbc.insets = new Insets(0, 30, 4, 30);
        card.add(lblError, gbc);

        // Botón entrar
        btnEntrar.setPreferredSize(new Dimension(260, Estilos.ALTO_BOTON));
        gbc.gridy = 8; gbc.insets = new Insets(4, 30, 8, 30);
        card.add(btnEntrar, gbc);

        // Link registro
        JPanel panelReg = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        panelReg.setOpaque(false);
        JLabel lblNoReg = new JLabel("¿No tienes cuenta?");
        lblNoReg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNoReg.setForeground(Estilos.TEXTO_SEC);
        btnRegistrarse.setPreferredSize(new Dimension(110, 30));
        btnRegistrarse.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelReg.add(lblNoReg);
        panelReg.add(btnRegistrarse);
        gbc.gridy = 9; gbc.insets = new Insets(0, 30, 24, 30);
        card.add(panelReg, gbc);

        // Ensamblar
        panelPrincipal.setLayout(new GridBagLayout());
        panelPrincipal.add(card);
    }

    // ─── Lógica ───────────────────────────────────────────────────────────────

    private void iniciarSesion() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            lblError.setText("Debe ingresar su usuario y contraseña. Si no está registrado, regístrese.");
            return;
        }

        Usuario usuario = usuarioDAO.login(user, pass);
        if (usuario != null) {
            lblError.setText(" ");
            dispose();
            SwingUtilities.invokeLater(() -> new MainFrame(usuario).setVisible(true));
        } else {
            lblError.setText("Usuario o contraseña incorrectos.");
            txtPassword.setText("");
        }
    }

    private void abrirRegistro() {
        new RegistroFrame(this).setVisible(true);
    }
}
