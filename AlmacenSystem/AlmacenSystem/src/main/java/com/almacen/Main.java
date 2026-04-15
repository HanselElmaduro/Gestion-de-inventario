package com.almacen;

import com.almacen.view.LoginFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Punto de entrada principal del Sistema de Gestión de Almacén.
 *
 * PATRONES APLICADOS EN ESTE PROYECTO:
 * ─────────────────────────────────────────────────────────────────────────────
 * 1. SINGLETON   → ConexionDB (una sola instancia de la BD)
 * 2. DAO         → UsuarioDAO, ProductoDAO (acceso a datos separado de la vista)
 * 3. MVC         → Separación model/dao/view
 * 4. OBSERVER    → cargarDatos() notifica la vista tras cada cambio en BD
 *
 * PILARES OOP APLICADOS:
 * ─────────────────────────────────────────────────────────────────────────────
 * 1. ABSTRACCIÓN    → BaseFrame, interfaces de alto nivel en DAOs
 * 2. ENCAPSULAMIENTO→ Getters/setters en Usuario y Producto
 * 3. HERENCIA       → LoginFrame, MainFrame, ProductosFrame, etc. extienden BaseFrame
 * 4. POLIMORFISMO   → initComponents(), configurarLayout(), renderers de tabla
 */
public class Main {

    public static void main(String[] args) {
        // Configurar Look and Feel profesional (Nimbus)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    // Personalizar colores Nimbus
                    UIManager.put("nimbusBase",     new Color(13, 71, 161));
                    UIManager.put("nimbusBlueGrey", new Color(100, 130, 165));
                    UIManager.put("control",        new Color(240, 244, 248));
                    UIManager.put("text",           new Color(33, 33, 33));
                    break;
                }
            }
        } catch (Exception e) {
            // Si Nimbus no está disponible, usar System L&F
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ex) { ex.printStackTrace(); }
        }

        // Habilitar anti-aliasing de texto
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Lanzar en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
