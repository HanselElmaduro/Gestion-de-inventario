package com.almacen.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * PATRÓN DE DISEÑO: SINGLETON
 * ─────────────────────────────────────────────────────────────────────────────
 * Garantiza que exista una única instancia de la conexión a la base de datos
 * durante todo el ciclo de vida de la aplicación. Esto evita abrir múltiples
 * conexiones innecesarias y centraliza la gestión de la BD.
 *
 * PILAR OOP: ENCAPSULAMIENTO
 * El constructor es privado; la única vía de acceso es getInstancia().
 */
public class ConexionDB {

    // Instancia única - Singleton
    private static ConexionDB instancia;
    private Connection conexion;

    // ─── Datos de conexión remota ─────────────────────────────────────────────
    private static final String URL      = "jdbc:mysql://almacenitla-db-itla-3837.e.aivencloud.com:25037/almacenitlafinal"
                                         + "?useSSL=true&requireSSL=true&serverTimezone=UTC"
                                         + "&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8"
                                         + "&connectTimeout=10000&socketTimeout=30000";
    private static final String USUARIO  = "avnadmin";
    private static final String PASSWORD = "AVNS_pPa2xcIg1UbjOzcsoMg";

    // Constructor privado — nadie puede instanciar esta clase directamente
    private ConexionDB() {
        conectar();
    }

    /** Devuelve la única instancia (Lazy Initialization). */
    public static synchronized ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    /** Retorna la conexión activa, reconectando si es necesario. */
    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conexion;
    }

    private void conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("✓ Conexión a la base de datos establecida.");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Driver MySQL no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("✗ Error de conexión SQL: " + e.getMessage());
        }
    }

    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
