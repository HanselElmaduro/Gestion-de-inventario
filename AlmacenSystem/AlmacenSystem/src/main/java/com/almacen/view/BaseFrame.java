package com.almacen.view;

import com.almacen.util.Estilos;

import javax.swing.*;
import java.awt.*;

/**
 * PILAR OOP: ABSTRACCIÓN + HERENCIA
 * ─────────────────────────────────────────────────────────────────────────────
 * Clase base abstracta para todas las ventanas de la aplicación.
 * Define el comportamiento común (centrado, ícono, configuración) y obliga
 * a las subclases a implementar initComponents() y configurarLayout().
 *
 * PILAR OOP: POLIMORFISMO
 * Cada subclase sobreescribe initComponents() con su propia lógica de UI,
 * pero todas comparten la inicialización de ventana de BaseFrame.
 */
public abstract class BaseFrame extends JFrame {

    protected JPanel panelPrincipal;

    public BaseFrame(String titulo, int ancho, int alto) {
        this(titulo, ancho, alto, true);
    }

    /**
     * Constructor que permite diferir la inicialización de componentes.
     * Si autoInit es false, la subclase debe llamar a init() manualmente
     * después de asignar sus propios campos.
     */
    protected BaseFrame(String titulo, int ancho, int alto, boolean autoInit) {
        super(titulo);
        setSize(ancho, alto);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con fondo
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Estilos.FONDO);
        setContentPane(panelPrincipal);

        if (autoInit) {
            // Métodos abstractos — polimorfismo en acción
            initComponents();
            configurarLayout();
        }
    }

    /** Inicializa componentes y layout. Llamar manualmente si autoInit=false. */
    protected void init() {
        initComponents();
        configurarLayout();
    }

    /**
     * PILAR OOP: POLIMORFISMO (método abstracto)
     * Cada subclase crea sus propios componentes aquí.
     */
    protected abstract void initComponents();

    /**
     * PILAR OOP: POLIMORFISMO (método abstracto)
     * Cada subclase organiza el layout aquí.
     */
    protected abstract void configurarLayout();

    /** Crea una barra de encabezado con título y degradado. */
    protected JPanel crearEncabezado(String titulo, String subtitulo) {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint grad = new GradientPaint(
                    0, 0, Estilos.PRIMARIO,
                    getWidth(), 0, new Color(21, 101, 192)
                );
                g2.setPaint(grad);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(getWidth(), 75));
        header.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(200, 220, 255));

        textos.add(lblTitulo);
        textos.add(lblSubtitulo);
        header.add(textos, BorderLayout.CENTER);

        return header;
    }

    /** Muestra un mensaje de error con ícono. */
    protected void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Muestra un mensaje de éxito. */
    protected void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Pregunta confirmación al usuario. */
    protected boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmación",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
