package com.almacen.view;

import com.almacen.dao.ProductoDAO;
import com.almacen.model.Producto;
import com.almacen.util.Estilos;

import javax.swing.*;
import java.awt.*;

/**
 * PILAR OOP: HERENCIA + POLIMORFISMO
 * Extiende JDialog, sobrescribe el comportamiento del formulario
 * para manejar edición en lugar de creación.
 */
public class EditarProductoFrame extends JDialog {

    private JTextField txtNombre, txtMarca, txtCategoria, txtPrecio, txtStock;
    private JButton btnGuardar, btnEliminar, btnCancelar;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ProductosFrame parentFrame;
    private final Producto producto;

    public EditarProductoFrame(JFrame owner, ProductosFrame parentFrame, Producto producto) {
        super(owner, "Editar Producto", true);
        this.parentFrame = parentFrame;
        this.producto    = producto;
        setSize(420, 450);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        buildUI();
        cargarDatos();
    }

    private void buildUI() {
        txtNombre    = Estilos.crearCampo(""); txtMarca     = Estilos.crearCampo("");
        txtCategoria = Estilos.crearCampo(""); txtPrecio    = Estilos.crearCampo("");
        txtStock     = Estilos.crearCampo("");

        btnGuardar  = Estilos.crearBotonExito("💾  Guardar");
        btnEliminar = Estilos.crearBotonPeligro("✕  Eliminar");
        btnCancelar = Estilos.crearBotonSecundario("← Cancelar");

        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnCancelar.addActionListener(e -> dispose());

        JLabel titulo = new JLabel("  ✎  Editando: " + producto.getNombreProducto());
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titulo.setForeground(Color.WHITE);
        titulo.setPreferredSize(new Dimension(420, 52));
        titulo.setOpaque(true); titulo.setBackground(Estilos.ADVERTENCIA);
        titulo.setBorder(BorderFactory.createEmptyBorder(0,16,0,0));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0; g.gridwidth = 2;

        Object[][] rows = {
            {"Nombre del Producto *", txtNombre},
            {"Marca *", txtMarca},
            {"Categoría *", txtCategoria},
            {"Precio (RD$) *", txtPrecio},
            {"Cantidad Disponible *", txtStock}
        };
        for (int i = 0; i < rows.length; i++) {
            g.gridy = i*2; g.insets = new Insets(6,0,2,0);
            JLabel lbl = new JLabel((String) rows[i][0]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            form.add(lbl, g);
            g.gridy = i*2+1; g.insets = new Insets(2,0,4,0);
            JComponent c = (JComponent) rows[i][1];
            c.setPreferredSize(new Dimension(350, Estilos.ALTO_CAMPO));
            form.add(c, g);
        }

        // Botones: Cancelar | Eliminar | Guardar
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        botones.setBackground(Color.WHITE);
        botones.setBorder(BorderFactory.createMatteBorder(1,0,0,0, Estilos.BORDE));
        btnCancelar.setPreferredSize(new Dimension(110, Estilos.ALTO_BOTON));
        btnEliminar.setPreferredSize(new Dimension(120, Estilos.ALTO_BOTON));
        btnGuardar.setPreferredSize(new Dimension(120, Estilos.ALTO_BOTON));
        botones.add(btnCancelar); botones.add(btnEliminar); botones.add(btnGuardar);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Color.WHITE);
        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(form, BorderLayout.CENTER);
        contenedor.add(botones, BorderLayout.SOUTH);
        setContentPane(contenedor);
    }

    private void cargarDatos() {
        txtNombre.setText(producto.getNombreProducto());
        txtMarca.setText(producto.getMarcaProducto());
        txtCategoria.setText(producto.getCategoriaProducto());
        txtPrecio.setText(String.valueOf(producto.getPrecioProducto()));
        txtStock.setText(String.valueOf(producto.getStockProducto()));
    }

    private void guardar() {
        String nom = txtNombre.getText().trim(), mar = txtMarca.getText().trim(),
               cat = txtCategoria.getText().trim(), pre = txtPrecio.getText().trim(),
               sto = txtStock.getText().trim();

        if (nom.isEmpty()||mar.isEmpty()||cat.isEmpty()||pre.isEmpty()||sto.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Todos los campos son obligatorios.","Error",JOptionPane.ERROR_MESSAGE); return;
        }

        int precio; int stock;
        try {
            String preClean = pre.replace(".", "").replace(",", "");
            precio = Integer.parseInt(preClean);
            if (precio < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,"El precio debe ser un número entero positivo (ej: 1500).","Error",JOptionPane.ERROR_MESSAGE);
            txtPrecio.requestFocus(); return;
        }
        try {
            stock = Integer.parseInt(sto);
            if (stock < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,"La cantidad debe ser un número entero positivo.","Error",JOptionPane.ERROR_MESSAGE);
            txtStock.requestFocus(); return;
        }

        producto.setNombreProducto(nom);
        producto.setMarcaProducto(mar);
        producto.setCategoriaProducto(cat);
        producto.setPrecioProducto(precio);
        producto.setStockProducto(stock);

        try {
            if (productoDAO.actualizar(producto)) {
                JOptionPane.showMessageDialog(this,"Producto actualizado exitosamente.","Éxito",JOptionPane.INFORMATION_MESSAGE);
                parentFrame.cargarDatos();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,"No se pudo actualizar (0 filas afectadas).","Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al actualizar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int c = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el producto '" + producto.getNombreProducto() + "'?\nEsta acción no se puede deshacer.",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            try {
                if (productoDAO.eliminar(producto.getIdProducto())) {
                    JOptionPane.showMessageDialog(this,"Producto eliminado.","Éxito",JOptionPane.INFORMATION_MESSAGE);
                    parentFrame.cargarDatos();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,"No se pudo eliminar.","Error",JOptionPane.ERROR_MESSAGE);
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al eliminar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
