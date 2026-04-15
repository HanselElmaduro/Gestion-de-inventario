package com.almacen.view;

import com.almacen.dao.UsuarioDAO;
import com.almacen.model.Usuario;
import com.almacen.util.Estilos;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * PILAR OOP: HERENCIA
 * Extiende JDialog para ser modal sobre MainFrame.
 *
 * PATRÓN DE DISEÑO: OBSERVER (simplificado)
 * ─────────────────────────────────────────────────────────────────────────────
 * El método cargarDatos() actúa como "actualización del observador":
 * cada vez que hay un cambio (insertar, actualizar, eliminar), se llama
 * cargarDatos() para reflejar el estado actual de la base de datos en la tabla,
 * manteniendo la vista siempre sincronizada con el modelo.
 */
public class UsuariosFrame extends JDialog {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevo, btnActualizar, btnEliminar, btnVolver;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private List<Usuario> usuarios;

    public UsuariosFrame(JFrame parent) {
        super(parent, "Gestión de Usuarios", true);
        setSize(850, 560);
        setLocationRelativeTo(parent);
        setResizable(false);
        initComponents();
        configurarLayout();
        cargarDatos(); // PATRÓN OBSERVER: carga inicial
    }

    private void initComponents() {
        String[] columnas = {"ID", "Nombre", "Apellido", "Teléfono", "Correo Electrónico", "Usuario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        Estilos.estilizarTabla(tabla);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnNuevo     = Estilos.crearBotonPrimario("＋  Nuevo");
        btnActualizar = Estilos.crearBoton("✎  Editar", Estilos.ADVERTENCIA);
        btnEliminar  = Estilos.crearBotonPeligro("✕  Eliminar");
        btnVolver    = Estilos.crearBotonSecundario("← Volver");

        btnNuevo.addActionListener(e -> abrirNuevoUsuario());
        btnActualizar.addActionListener(e -> abrirEditarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnVolver.addActionListener(e -> dispose());
    }

    private void configurarLayout() {
        JPanel contenedor = new JPanel(new BorderLayout(0, 0));
        contenedor.setBackground(Estilos.FONDO);

        // Header
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint grad = new GradientPaint(0,0,Estilos.PRIMARIO, getWidth(),0, new Color(21,101,192));
                g2.setPaint(grad); g2.fillRect(0,0,getWidth(),getHeight()); g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(850, 70));
        header.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));
        header.setLayout(new BorderLayout());
        JLabel lblH = new JLabel("👥  Clientes Registrados");
        lblH.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblH.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("Administra los usuarios del sistema");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(200, 220, 255));
        JPanel tx = new JPanel(); tx.setLayout(new BoxLayout(tx, BoxLayout.Y_AXIS)); tx.setOpaque(false);
        tx.add(lblH); tx.add(lblSub);
        header.add(tx, BorderLayout.CENTER);

        // Tabla
        JScrollPane scroll = Estilos.crearScrollPane(tabla);
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        panelTabla.add(scroll, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Estilos.BORDE));
        btnNuevo.setPreferredSize(new Dimension(130, Estilos.ALTO_BOTON));
        btnActualizar.setPreferredSize(new Dimension(130, Estilos.ALTO_BOTON));
        btnEliminar.setPreferredSize(new Dimension(130, Estilos.ALTO_BOTON));
        btnVolver.setPreferredSize(new Dimension(110, Estilos.ALTO_BOTON));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        JPanel pVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        pVolver.setBackground(Color.WHITE);
        pVolver.add(btnVolver);

        JPanel barraInferior = new JPanel(new BorderLayout());
        barraInferior.setBackground(Color.WHITE);
        barraInferior.add(panelBotones, BorderLayout.WEST);
        barraInferior.add(pVolver, BorderLayout.EAST);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.add(panelTabla, BorderLayout.CENTER);
        card.add(barraInferior, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout(0, 0));
        wrapper.setBackground(Estilos.FONDO);
        wrapper.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        wrapper.add(card, BorderLayout.CENTER);

        contenedor.add(header, BorderLayout.NORTH);
        contenedor.add(wrapper, BorderLayout.CENTER);
        setContentPane(contenedor);
    }

    // ─── PATRÓN OBSERVER: actualiza la tabla con datos frescos de la BD ───────
    public void cargarDatos() {
        modeloTabla.setRowCount(0);
        usuarios = usuarioDAO.listar();
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{
                u.getIdUser(), u.getNombre(), u.getApellido(),
                u.getTelefono(), u.getEmail(), u.getUserName()
            });
        }
    }

    private Usuario getUsuarioSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla primero.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return usuarios.get(fila);
    }

    private void abrirNuevoUsuario() {
        NuevoUsuarioFrame dialog = new NuevoUsuarioFrame(null, this);
        dialog.setVisible(true);
    }

    private void abrirEditarUsuario() {
        Usuario u = getUsuarioSeleccionado();
        if (u != null) {
            new EditarUsuarioFrame(null, this, u).setVisible(true);
        }
    }

    private void eliminarUsuario() {
        Usuario u = getUsuarioSeleccionado();
        if (u == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Eliminar al usuario '" + u.getNombre() + " " + u.getApellido() + "'?\nEsta acción no se puede deshacer.",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (usuarioDAO.eliminar(u.getIdUser())) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos(); // OBSERVER: actualización automática
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
