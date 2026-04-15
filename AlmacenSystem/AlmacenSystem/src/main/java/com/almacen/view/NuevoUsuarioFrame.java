package com.almacen.view;

import com.almacen.dao.UsuarioDAO;
import com.almacen.model.Usuario;
import com.almacen.util.Estilos;

import javax.swing.*;
import java.awt.*;

public class NuevoUsuarioFrame extends JDialog {

    private JTextField txtNombre, txtApellido, txtUserName, txtTelefono, txtEmail;
    private JPasswordField txtPassword, txtConfirmar;
    private JButton btnGuardar, btnCancelar;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final UsuariosFrame parent;

    public NuevoUsuarioFrame(JFrame owner, UsuariosFrame parent) {
        super(owner, "Nuevo Usuario", true);
        this.parent = parent;
        setSize(440, 540);
        setLocationRelativeTo(parent);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        txtNombre   = Estilos.crearCampo(""); txtApellido = Estilos.crearCampo("");
        txtUserName = Estilos.crearCampo(""); txtTelefono = Estilos.crearCampo("");
        txtEmail    = Estilos.crearCampo(""); txtPassword = Estilos.crearCampoPassword();
        txtConfirmar= Estilos.crearCampoPassword();

        btnGuardar  = Estilos.crearBotonExito("Guardar");
        btnCancelar = Estilos.crearBotonSecundario("Cancelar");

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        JPanel form = buildForm();
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        botones.setBackground(Color.WHITE);
        btnGuardar.setPreferredSize(new Dimension(140, Estilos.ALTO_BOTON));
        btnCancelar.setPreferredSize(new Dimension(110, Estilos.ALTO_BOTON));
        botones.add(btnCancelar); botones.add(btnGuardar);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Color.WHITE);
        JLabel titulo = new JLabel("  ➕  Registrar Nuevo Usuario");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(Color.WHITE);
        titulo.setPreferredSize(new Dimension(440, 52));
        titulo.setOpaque(true);
        titulo.setBackground(Estilos.PRIMARIO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0,16,0,0));

        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(form, BorderLayout.CENTER);
        contenedor.add(botones, BorderLayout.SOUTH);
        setContentPane(contenedor);
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0; g.gridwidth = 2;

        int[][] campos = {};
        Object[][] rows = {
            {"Nombre *", txtNombre}, {"Apellido *", txtApellido},
            {"Usuario *", txtUserName}, {"Teléfono *", txtTelefono},
            {"Email *", txtEmail}, {"Contraseña *", txtPassword},
            {"Confirmar Contraseña *", txtConfirmar}
        };
        for (int i = 0; i < rows.length; i++) {
            g.gridy = i*2; g.gridx = 0; g.insets = new Insets(4,0,2,0);
            JLabel lbl = new JLabel((String)rows[i][0]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            p.add(lbl, g);
            g.gridy = i*2+1; g.insets = new Insets(2,0,6,0);
            JComponent comp = (JComponent)rows[i][1];
            comp.setPreferredSize(new Dimension(370, Estilos.ALTO_CAMPO));
            p.add(comp, g);
        }
        return p;
    }

    private void guardar() {
        String nom = txtNombre.getText().trim(), ape = txtApellido.getText().trim(),
               usr = txtUserName.getText().trim(), tel = txtTelefono.getText().trim(),
               eml = txtEmail.getText().trim();
        String pas = new String(txtPassword.getPassword()).trim();
        String con = new String(txtConfirmar.getPassword()).trim();

        if (nom.isEmpty()||ape.isEmpty()||usr.isEmpty()||tel.isEmpty()||eml.isEmpty()||pas.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Todos los campos son obligatorios.","Error",JOptionPane.ERROR_MESSAGE); return;
        }
        if (!pas.equals(con)) {
            JOptionPane.showMessageDialog(this,"Las contraseñas no coinciden.","Error",JOptionPane.ERROR_MESSAGE);
            txtPassword.setText(""); txtConfirmar.setText(""); return;
        }
        if (usuarioDAO.existeUserName(usr)) {
            JOptionPane.showMessageDialog(this,"El nombre de usuario ya está en uso.","Error",JOptionPane.ERROR_MESSAGE); return;
        }
        if (usuarioDAO.insertar(new Usuario(usr,nom,ape,tel,eml,pas))) {
            JOptionPane.showMessageDialog(this,"Usuario registrado exitosamente.","Éxito",JOptionPane.INFORMATION_MESSAGE);
            parent.cargarDatos(); // OBSERVER: actualización automática
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,"Error al guardar.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
