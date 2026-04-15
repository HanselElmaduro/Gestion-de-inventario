package com.almacen.view;

import com.almacen.dao.UsuarioDAO;
import com.almacen.model.Usuario;
import com.almacen.util.Estilos;

import javax.swing.*;
import java.awt.*;

/**
 * PILAR OOP: HERENCIA
 * RegistroFrame extiende JDialog (no BaseFrame) para ser modal sobre el login.
 * Demuestra que la herencia se aplica según el contexto adecuado.
 */
public class RegistroFrame extends JDialog {

    private JTextField txtNombre, txtApellido, txtUserName, txtTelefono, txtEmail;
    private JPasswordField txtPassword, txtConfirmarPassword;
    private JButton btnRegistrar, btnCancelar;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public RegistroFrame(JFrame parent) {
        super(parent, "Registro de Nuevo Usuario", true);
        setSize(460, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        initComponents();
        configurarLayout();
    }

    private void initComponents() {
        txtNombre            = Estilos.crearCampo("Nombre");
        txtApellido          = Estilos.crearCampo("Apellido");
        txtUserName          = Estilos.crearCampo("Nombre de usuario");
        txtTelefono          = Estilos.crearCampo("Teléfono");
        txtEmail             = Estilos.crearCampo("Correo electrónico");
        txtPassword          = Estilos.crearCampoPassword();
        txtConfirmarPassword = Estilos.crearCampoPassword();

        btnRegistrar = Estilos.crearBotonExito("Registrar");
        btnCancelar  = Estilos.crearBotonSecundario("Cancelar");

        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void configurarLayout() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Estilos.FONDO);

        // Header
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
        header.setPreferredSize(new Dimension(460, 70));
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));

        JLabel lblTitulo = new JLabel("📋  Crear Cuenta Nueva");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.CENTER);

        // Formulario
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Dos columnas: nombre y apellido
        gbc.gridy = 0; gbc.gridx = 0; gbc.gridwidth = 1;
        gbc.insets = new Insets(4, 4, 2, 4);
        form.add(labelBold("Nombre *"), gbc);
        gbc.gridx = 1;
        form.add(labelBold("Apellido *"), gbc);

        gbc.gridy = 1; gbc.gridx = 0;
        gbc.insets = new Insets(2, 4, 8, 4);
        txtNombre.setPreferredSize(new Dimension(170, Estilos.ALTO_CAMPO));
        form.add(txtNombre, gbc);
        gbc.gridx = 1;
        txtApellido.setPreferredSize(new Dimension(170, Estilos.ALTO_CAMPO));
        form.add(txtApellido, gbc);

        // Fila: usuario
        agregarFila(form, gbc, 2, "Nombre de Usuario *", txtUserName);
        agregarFila(form, gbc, 4, "Teléfono *", txtTelefono);
        agregarFila(form, gbc, 6, "Correo Electrónico *", txtEmail);
        agregarFila(form, gbc, 8, "Contraseña *", txtPassword);
        agregarFila(form, gbc, 10, "Confirmar Contraseña *", txtConfirmarPassword);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 16));
        panelBotones.setBackground(Color.WHITE);
        btnRegistrar.setPreferredSize(new Dimension(160, Estilos.ALTO_BOTON));
        btnCancelar.setPreferredSize(new Dimension(120, Estilos.ALTO_BOTON));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnRegistrar);

        contenedor.add(header, BorderLayout.NORTH);
        contenedor.add(form, BorderLayout.CENTER);
        contenedor.add(panelBotones, BorderLayout.SOUTH);

        setContentPane(contenedor);
    }

    private void agregarFila(JPanel form, GridBagConstraints gbc, int row, String lbl, JComponent campo) {
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(4, 4, 2, 4);
        form.add(labelBold(lbl), gbc);

        gbc.gridy = row + 1;
        gbc.insets = new Insets(2, 4, 8, 4);
        ((JComponent) campo).setPreferredSize(new Dimension(360, Estilos.ALTO_CAMPO));
        form.add(campo, gbc);
    }

    private JLabel labelBold(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Estilos.TEXTO);
        return lbl;
    }

    // ─── Validación y registro ────────────────────────────────────────────────

    private void registrar() {
        // Validar campos vacíos
        if (txtNombre.getText().trim().isEmpty()) {
            error("El campo 'Nombre' es obligatorio.");
            txtNombre.requestFocus(); return;
        }
        if (txtApellido.getText().trim().isEmpty()) {
            error("El campo 'Apellido' es obligatorio.");
            txtApellido.requestFocus(); return;
        }
        if (txtUserName.getText().trim().isEmpty()) {
            error("El campo 'Nombre de Usuario' es obligatorio.");
            txtUserName.requestFocus(); return;
        }
        if (txtTelefono.getText().trim().isEmpty()) {
            error("El campo 'Teléfono' es obligatorio.");
            txtTelefono.requestFocus(); return;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            error("El campo 'Correo Electrónico' es obligatorio.");
            txtEmail.requestFocus(); return;
        }
        String pass    = new String(txtPassword.getPassword()).trim();
        String confirm = new String(txtConfirmarPassword.getPassword()).trim();
        if (pass.isEmpty()) {
            error("El campo 'Contraseña' es obligatorio.");
            txtPassword.requestFocus(); return;
        }
        if (!pass.equals(confirm)) {
            error("Las contraseñas no coinciden. Verifique e intente de nuevo.");
            txtConfirmarPassword.setText("");
            txtConfirmarPassword.requestFocus(); return;
        }

        // Verificar usuario único
        if (usuarioDAO.existeUserName(txtUserName.getText().trim())) {
            error("El nombre de usuario '" + txtUserName.getText().trim() + "' ya está en uso. Elija otro.");
            txtUserName.requestFocus(); return;
        }

        // Crear y guardar
        Usuario nuevo = new Usuario(
            txtUserName.getText().trim(),
            txtNombre.getText().trim(),
            txtApellido.getText().trim(),
            txtTelefono.getText().trim(),
            txtEmail.getText().trim(),
            pass
        );

        if (usuarioDAO.insertar(nuevo)) {
            JOptionPane.showMessageDialog(this,
                "¡Cuenta creada exitosamente!\nYa puedes iniciar sesión con tu usuario.",
                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            error("Error al registrar el usuario. Intente de nuevo.");
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error de Validación", JOptionPane.ERROR_MESSAGE);
    }
}
