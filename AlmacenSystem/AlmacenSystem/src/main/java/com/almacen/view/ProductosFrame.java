package com.almacen.view;

import com.almacen.dao.ProductoDAO;
import com.almacen.model.Producto;
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
 * cargarDatos() mantiene la vista sincronizada con el estado de la BD.
 */
public class ProductosFrame extends JDialog {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevo, btnVolver;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private List<Producto> productos;

    public ProductosFrame(JFrame parent) {
        super(parent, "Gestión de Productos", true);
        setSize(900, 560);
        setLocationRelativeTo(parent);
        setResizable(false);
        initComponents();
        configurarLayout();
        cargarDatos();
    }

    private void initComponents() {
        String[] cols = {"ID", "Nombre", "Marca", "Categoría", "Precio (RD$)", "Disponible"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        Estilos.estilizarTabla(tabla);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(4).setCellRenderer(new PrecioRenderer());
        tabla.getColumnModel().getColumn(5).setCellRenderer(new StockRenderer());
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Clic en fila abre editor
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 1 && tabla.getSelectedRow() >= 0) {
                    abrirEditarProducto();
                }
            }
        });

        btnNuevo  = Estilos.crearBotonPrimario("＋  Nuevo");
        btnVolver = Estilos.crearBotonSecundario("← Volver");

        btnNuevo.addActionListener(e -> abrirNuevoProducto());
        btnVolver.addActionListener(e -> dispose());
    }

    private void configurarLayout() {
        JPanel contenedor = new JPanel(new BorderLayout());
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
        header.setPreferredSize(new Dimension(900, 70));
        header.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));
        header.setLayout(new BorderLayout());
        JPanel tx = new JPanel(); tx.setLayout(new BoxLayout(tx, BoxLayout.Y_AXIS)); tx.setOpaque(false);
        JLabel h1 = new JLabel("📦  Productos de Almacén");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 18)); h1.setForeground(Color.WHITE);
        JLabel h2 = new JLabel("Haz clic en un producto para editarlo");
        h2.setFont(new Font("Segoe UI", Font.PLAIN, 12)); h2.setForeground(new Color(200, 220, 255));
        tx.add(h1); tx.add(h2);
        header.add(tx, BorderLayout.CENTER);

        // Tabla
        JScrollPane scroll = Estilos.crearScrollPane(tabla);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        panelTabla.add(scroll, BorderLayout.CENTER);

        // Botones inferiores
        JPanel botones = new JPanel(new BorderLayout());
        botones.setBackground(Color.WHITE);
        botones.setBorder(BorderFactory.createMatteBorder(1,0,0,0, Estilos.BORDE));

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        izq.setBackground(Color.WHITE);
        btnNuevo.setPreferredSize(new Dimension(140, Estilos.ALTO_BOTON));
        izq.add(btnNuevo);

        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        der.setBackground(Color.WHITE);
        btnVolver.setPreferredSize(new Dimension(110, Estilos.ALTO_BOTON));
        der.add(btnVolver);

        botones.add(izq, BorderLayout.WEST);
        botones.add(der, BorderLayout.EAST);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.add(panelTabla, BorderLayout.CENTER);
        card.add(botones, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Estilos.FONDO);
        wrapper.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        wrapper.add(card, BorderLayout.CENTER);

        contenedor.add(header, BorderLayout.NORTH);
        contenedor.add(wrapper, BorderLayout.CENTER);
        setContentPane(contenedor);
    }

    /** PATRÓN OBSERVER: sincroniza la vista con la BD. */
    public void cargarDatos() {
        modeloTabla.setRowCount(0);
        productos = productoDAO.listar();
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombreProducto(),
                p.getMarcaProducto(),
                p.getCategoriaProducto(),
                String.format("RD$ %,.2f", p.getPrecioProducto()),
                p.getStockProducto()
            });
        }
    }

    private void abrirNuevoProducto() {
        new NuevoProductoFrame(null, this).setVisible(true);
    }

    private void abrirEditarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            new EditarProductoFrame(null, this, productos.get(fila)).setVisible(true);
        }
    }

    // ─── Renderers personalizados ─────────────────────────────────────────────

    /** PILAR OOP: HERENCIA — extiende DefaultTableCellRenderer para colorear precios */
    static class PrecioRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val,
                boolean sel, boolean focus, int row, int col) {
            super.getTableCellRendererComponent(t, val, sel, focus, row, col);
            setHorizontalAlignment(SwingConstants.RIGHT);
            if (!sel) setForeground(new Color(27, 94, 32));
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            return this;
        }
    }

    /** PILAR OOP: POLIMORFISMO — mismo método, comportamiento diferente según stock */
    static class StockRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val,
                boolean sel, boolean focus, int row, int col) {
            super.getTableCellRendererComponent(t, val, sel, focus, row, col);
            setHorizontalAlignment(SwingConstants.CENTER);
            if (!sel && val != null) {
                int stock = Integer.parseInt(val.toString());
                setForeground(stock < 5 ? Estilos.PELIGRO : stock < 15 ? Estilos.ADVERTENCIA : new Color(27,94,32));
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            }
            return this;
        }
    }
}
