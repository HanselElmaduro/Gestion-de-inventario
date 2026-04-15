package com.almacen.util;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * PATRÓN DE DISEÑO: UTILITY / HELPER
 * ─────────────────────────────────────────────────────────────────────────────
 * Centraliza toda la lógica de diseño visual de la aplicación.
 * Un único lugar para cambiar la paleta de colores o tipografía.
 *
 * PILAR OOP: ABSTRACCIÓN
 * Expone métodos de alto nivel para crear componentes estilizados,
 * ocultando los detalles de renderizado.
 */
public class Estilos {

    // ─── Paleta de Colores ────────────────────────────────────────────────────
    public static final Color PRIMARIO        = new Color(13, 71, 161);   // Azul oscuro
    public static final Color PRIMARIO_CLARO  = new Color(25, 118, 210);  // Azul medio
    public static final Color ACENTO          = new Color(0, 176, 255);   // Azul claro
    public static final Color PELIGRO         = new Color(198, 40, 40);   // Rojo
    public static final Color EXITO           = new Color(46, 125, 50);   // Verde
    public static final Color ADVERTENCIA     = new Color(245, 124, 0);   // Naranja
    public static final Color FONDO           = new Color(240, 244, 248); // Gris azulado claro
    public static final Color SUPERFICIE      = Color.WHITE;
    public static final Color TEXTO           = new Color(33, 33, 33);
    public static final Color TEXTO_SEC       = new Color(117, 117, 117);
    public static final Color BORDE           = new Color(200, 212, 227);
    public static final Color TABLA_HEADER    = new Color(13, 71, 161);
    public static final Color TABLA_FILA_PAR  = new Color(232, 240, 254);
    public static final Color TABLA_FILA_IMPAR= Color.WHITE;
    public static final Color TABLA_SELECCION = new Color(66, 133, 244);

    // ─── Tipografía ───────────────────────────────────────────────────────────
    public static final Font FUENTE_TITULO    = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FUENTE_NORMAL    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_BOLD      = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_BOTON     = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FUENTE_TABLA     = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FUENTE_HEADER    = new Font("Segoe UI", Font.BOLD, 12);

    // ─── Dimensiones ─────────────────────────────────────────────────────────
    public static final int ALTO_CAMPO  = 38;
    public static final int ALTO_BOTON  = 38;
    public static final int RADIO_BORDE = 8;

    // ─── Métodos de construcción ──────────────────────────────────────────────

    /** Crea un campo de texto estilizado. */
    public static JTextField crearCampo(String placeholder) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIO_BORDE, RADIO_BORDE);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        campo.setFont(FUENTE_NORMAL);
        campo.setForeground(TEXTO);
        campo.setBackground(Color.WHITE);
        campo.setPreferredSize(new Dimension(200, ALTO_CAMPO));
        campo.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(BORDE, RADIO_BORDE),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        campo.setOpaque(false);
        return campo;
    }

    /** Crea un campo de contraseña estilizado. */
    public static JPasswordField crearCampoPassword() {
        JPasswordField campo = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIO_BORDE, RADIO_BORDE);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        campo.setFont(FUENTE_NORMAL);
        campo.setForeground(TEXTO);
        campo.setBackground(Color.WHITE);
        campo.setPreferredSize(new Dimension(200, ALTO_CAMPO));
        campo.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(BORDE, RADIO_BORDE),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        campo.setOpaque(false);
        return campo;
    }

    /** Crea un botón estilizado con color de fondo personalizado. */
    public static JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(colorFondo.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(colorFondo.brighter());
                } else {
                    g2.setColor(colorFondo);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIO_BORDE, RADIO_BORDE);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        boton.setFont(FUENTE_BOTON);
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorFondo);
        boton.setPreferredSize(new Dimension(130, ALTO_BOTON));
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /** Crea un botón primario (azul). */
    public static JButton crearBotonPrimario(String texto) {
        return crearBoton(texto, PRIMARIO_CLARO);
    }

    /** Crea un botón de peligro (rojo). */
    public static JButton crearBotonPeligro(String texto) {
        return crearBoton(texto, PELIGRO);
    }

    /** Crea un botón de éxito (verde). */
    public static JButton crearBotonExito(String texto) {
        return crearBoton(texto, EXITO);
    }

    /** Crea un botón secundario (gris). */
    public static JButton crearBotonSecundario(String texto) {
        return crearBoton(texto, new Color(96, 125, 139));
    }

    /** Aplica estilo profesional a una JTable. */
    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(FUENTE_TABLA);
        tabla.setRowHeight(36);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(BORDE);
        tabla.setSelectionBackground(TABLA_SELECCION);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFillsViewportHeight(true);
        tabla.setIntercellSpacing(new Dimension(0, 1));

        // Header
        JTableHeader header = tabla.getTableHeader();
        header.setFont(FUENTE_HEADER);
        header.setBackground(TABLA_HEADER);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setReorderingAllowed(false);

        // Renderer alternado
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? TABLA_FILA_PAR : TABLA_FILA_IMPAR);
                    c.setForeground(TEXTO);
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }

    /** Crea un JScrollPane sin borde. */
    public static JScrollPane crearScrollPane(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createLineBorder(BORDE));
        sp.getViewport().setBackground(Color.WHITE);
        return sp;
    }

    /** Crea un label de título. */
    public static JLabel crearLabelTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_TITULO);
        lbl.setForeground(PRIMARIO);
        return lbl;
    }

    /** Crea un label normal. */
    public static JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_NORMAL);
        lbl.setForeground(TEXTO);
        return lbl;
    }

    /** Crea un separador horizontal decorativo. */
    public static JSeparator crearSeparador() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDE);
        return sep;
    }

    /** Panel con fondo blanco y bordes redondeados (tarjeta). */
    public static JPanel crearPanelTarjeta() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    // ─── Borde Redondeado ─────────────────────────────────────────────────────

    /**
     * PILAR OOP: HERENCIA
     * RoundBorder extiende AbstractBorder para proveer bordes redondeados
     * reutilizables en cualquier componente.
     */
    public static class RoundBorder extends AbstractBorder {
        private final Color color;
        private final int radio;

        public RoundBorder(Color color, int radio) {
            this.color = color;
            this.radio = radio;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w-1, h-1, radio, radio);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) { return new Insets(2, 2, 2, 2); }
    }
}
