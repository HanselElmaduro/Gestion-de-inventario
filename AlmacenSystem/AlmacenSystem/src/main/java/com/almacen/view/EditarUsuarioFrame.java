package com.almacen.view;

import com.almacen.dao.UsuarioDAO;
import com.almacen.model.Usuario;
import com.almacen.util.Estilos;

import javax.swing.*;
import java.awt.*;

/**
 * PILAR OOP: HERENCIA
 * Extiende JDialog. Reutiliza la estructura visual de NuevoUsuarioFrame
 * pero pre-carga los datos del usuario seleccionado.
 */
public class EditarUsuarioFrame extends JDialog {

    private JTextField txtNombre, txtApellido, txtUserName, txtTelefono, txtEmail;
    private JPasswordField txtPassword;
    private JButton btnGuardar, btnEliminar, btnCancelar;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final UsuariosFrame parentFrame;
    private final Usuario usuario;

    public EditarUsuarioFrame(JFrame owner, UsuariosFrame parentFrame, Usuario usuario) {
        super(owner, "Editar Usuario", true);
        this.parentFrame = parentFrame;
        this.usuario     = usuario;
        setSize(440, 500);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        buildUI();
        cargarDatos();
    }

    private void buildUI() {
        txtNombre   = Estilos.crearCampo(""); txtApellido = Estilos.crearCampo("");
        txtUserName = Estilos.crearCampo(""); txtTelefono = Estilos.crearCampo("");
        txtEmail    = Estilos.crearCampo(""); txtPassword = Estilos.crearCampoPassword();

        btnGuardar  = Estilos.crearBotonExito("Guardar");
        btnEliminar = Estilos.crearBotonPeligro("Eliminar");
        btnCancelar = Estilos.crearBotonSecundario("Cancelar");

        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnCancelar.addActionListener(e -> dispose());

        JPanel form = buildForm();

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        botones.setBackground(Color.WHITE);
        btnGuardar.setPreferredSize(new Dimension(120, Estilos.ALTO_BOTON));
        btnEliminar.setPreferredSize(new Dimension(120, Estilos.ALTO_BOTON));
        btnCancelar.setPreferredSize(new Dimension(100, Estilos.ALTO_BOTON));
        botones.add(btnCancelar); botones.add(btnEliminar); botones.add(btnGuardar);

        JLabel titulo = new JLabel("  ✎  Editar Usuario: " + usuario.getNombre());
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(Color.WHITE);
        titulo.setPreferredSize(new Dimension(440, 52));
        titulo.setOpaque(true);
        titulo.setBackground(Estilos.ADVERTENCIA);
        titulo.setBorder(BorderFactory.createEmptyBorder(0,16,0,0));

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Color.WHITE);
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

        Object[][] rows = {
            {"Nombre *", txtNombre}, {"Apellido *", txtApellido},
            {"Usuario *", txtUserName}, {"Teléfono *", txtTelefono},
            {"Email *", txtEmail}, {"Nueva Contraseña (dejar vacío para no cambiar)", txtPassword}
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

    private void cargarDatos() {
        txtNombre.setText(usuario.getNombre());
        txtApellido.setText(usuario.getApellido());
        txtUserName.setText(usuario.getUserName());
        txtTelefono.setText(usuario.getTelefono());
        txtEmail.setText(usuario.getEmail());
    }

    private void guardar() {
        String nom = txtNombre.getText().trim(), ape = txtApellido.getText().trim(),
               usr = txtUserName.getText().trim(), tel = txtTelefono.getText().trim(),
               eml = txtEmail.getText().trim();
        String nuePass = new String(txtPassword.getPassword()).trim();

        if (nom.isEmpty()||ape.isEmpty()||usr.isEmpty()||tel.isEmpty()||eml.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Todos los campos son obligatorios.","Error",JOptionPane.ERROR_MESSAGE); return;
        }

        usuario.setNombre(nom);
        usuario.setApellido(ape);
        usuario.setUserName(usr);
        usuario.setTelefono(tel);
        usuario.setEmail(eml);
        if (!nuePass.isEmpty()) usuario.setPassword(nuePass);

        if (usuarioDAO.actualizar(usuario)) {
            JOptionPane.showMessageDialog(this,"Usuario actualizado exitosamente.","Éxito",JOptionPane.INFORMATION_MESSAGE);
            parentFrame.cargarDatos(); // OBSERVER: actualización automática
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,"Error al actualizar.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int c = JOptionPane.showConfirmDialog(this,
            "¿Eliminar al usuario '" + usuario.getNombre() + "'?",
            "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            if (usuarioDAO.eliminar(usuario.getIdUser())) {
                JOptionPane.showMessageDialog(this,"Usuario eliminado.","Éxito",JOptionPane.INFORMATION_MESSAGE);
                parentFrame.cargarDatos();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,"Error al eliminar.","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
