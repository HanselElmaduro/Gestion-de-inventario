package com.almacen.view;

import com.almacen.dao.ProductoDAO;
import com.almacen.model.Producto;
import com.almacen.util.Estilos;

import javax.swing.*;
import java.awt.*;

public class NuevoProductoFrame extends JDialog {

    private JTextField txtNombre, txtMarca, txtCategoria, txtPrecio, txtStock;
    private JButton btnGuardar, btnCancelar;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ProductosFrame parentFrame;

    public NuevoProductoFrame(JFrame owner, ProductosFrame parentFrame) {
        super(owner, "Nuevo Producto", true);
        this.parentFrame = parentFrame;
        setSize(420, 460);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        txtNombre    = Estilos.crearCampo("");
        txtMarca     = Estilos.crearCampo("");
        txtCategoria = Estilos.crearCampo("");
        txtPrecio    = Estilos.crearCampo("");
        txtStock     = Estilos.crearCampo("");

        btnGuardar  = Estilos.crearBotonExito("Guardar");
        btnCancelar = Estilos.crearBotonSecundario("Cancelar");
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        JLabel titulo = new JLabel("  \u002B  Registrar Nuevo Producto");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(Color.WHITE);
        titulo.setPreferredSize(new Dimension(420, 52));
        titulo.setOpaque(true);
        titulo.setBackground(Estilos.PRIMARIO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.gridwidth = 2;

        Object[][] rows = {
            {"Nombre del Producto *", txtNombre},
            {"Marca *",               txtMarca},
            {"Categoria *",           txtCategoria},
            {"Precio (RD$) *",        txtPrecio},
            {"Cantidad Disponible *", txtStock}
        };
        for (int i = 0; i < rows.length; i++) {
            g.gridy = i * 2;
            g.insets = new Insets(6, 0, 2, 0);
            JLabel lbl = new JLabel((String) rows[i][0]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            form.add(lbl, g);
            g.gridy = i * 2 + 1;
            g.insets = new Insets(2, 0, 4, 0);
            JComponent c = (JComponent) rows[i][1];
            c.setPreferredSize(new Dimension(350, Estilos.ALTO_CAMPO));
            form.add(c, g);
        }

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        botones.setBackground(Color.WHITE);
        btnGuardar.setPreferredSize(new Dimension(130, Estilos.ALTO_BOTON));
        btnCancelar.setPreferredSize(new Dimension(110, Estilos.ALTO_BOTON));
        botones.add(btnCancelar);
        botones.add(btnGuardar);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Color.WHITE);
        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(form,   BorderLayout.CENTER);
        contenedor.add(botones, BorderLayout.SOUTH);
        setContentPane(contenedor);
    }

    private void guardar() {
        String nom = txtNombre.getText().trim();
        String mar = txtMarca.getText().trim();
        String cat = txtCategoria.getText().trim();
        String pre = txtPrecio.getText().trim();
        String sto = txtStock.getText().trim();

        // Validar vacíos
        if (nom.isEmpty() || mar.isEmpty() || cat.isEmpty() || pre.isEmpty() || sto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar precio
        int precio;
        try {
            // Aceptar "1500", "1.500", "1,500" → quitar puntos/comas y parsear
            String preClean = pre.replace(".", "").replace(",", "");
            precio = Integer.parseInt(preClean);
            if (precio < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El precio debe ser un numero entero positivo (ej: 1500).",
                "Error", JOptionPane.ERROR_MESSAGE);
            txtPrecio.requestFocus();
            return;
        }

        // Validar stock
        int stock;
        try {
            stock = Integer.parseInt(sto);
            if (stock < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "La cantidad disponible debe ser un numero entero positivo.",
                "Error", JOptionPane.ERROR_MESSAGE);
            txtStock.requestFocus();
            return;
        }

        // Guardar
        try {
            boolean ok = productoDAO.insertar(new Producto(nom, mar, cat, precio, stock));
            if (ok) {
                JOptionPane.showMessageDialog(this,
                    "Producto registrado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                parentFrame.cargarDatos();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el producto (0 filas afectadas).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RuntimeException ex) {
            // Muestra el error real de SQL o conexion
            JOptionPane.showMessageDialog(this,
                ex.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
