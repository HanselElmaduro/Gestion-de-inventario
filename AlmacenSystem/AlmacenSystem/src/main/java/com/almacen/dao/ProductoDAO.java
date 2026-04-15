package com.almacen.dao;

import com.almacen.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN DE DISEÑO: DAO (Data Access Object)
 * CORRECCIÓN: La conexión se obtiene fresca en cada operación para evitar
 * NullPointerException cuando la conexión expira o falla al inicio.
 */
public class ProductoDAO {

    // Obtiene conexión activa en cada llamada
    private Connection getConn() {
        return ConexionDB.getInstancia().getConexion();
    }

    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY NombreProducto";
        Connection conn = getConn();
        if (conn == null) return lista;
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearProducto(rs));
        } catch (SQLException e) {
            System.err.println("Error listando productos: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Producto p) {
        String sql = "INSERT INTO productos (NombreProducto, MarcaProducto, CategoriaProducto, PrecioProducto, StockProducto) VALUES (?,?,?,?,?)";
        Connection conn = getConn();
        if (conn == null) throw new RuntimeException("Sin conexión a la base de datos. Verifique su internet.");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombreProducto());
            ps.setString(2, p.getMarcaProducto());
            ps.setString(3, p.getCategoriaProducto());
            ps.setInt(4, (int) p.getPrecioProducto()); // BD define PrecioProducto como INT
            ps.setInt(5, p.getStockProducto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al guardar:\n" + e.getMessage(), e);
        }
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET NombreProducto=?,MarcaProducto=?,CategoriaProducto=?,PrecioProducto=?,StockProducto=? WHERE idProducto=?";
        Connection conn = getConn();
        if (conn == null) throw new RuntimeException("Sin conexión a la base de datos.");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombreProducto());
            ps.setString(2, p.getMarcaProducto());
            ps.setString(3, p.getCategoriaProducto());
            ps.setInt(4, (int) p.getPrecioProducto());
            ps.setInt(5, p.getStockProducto());
            ps.setInt(6, p.getIdProducto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al actualizar:\n" + e.getMessage(), e);
        }
    }

    public boolean eliminar(int idProducto) {
        String sql = "DELETE FROM productos WHERE idProducto=?";
        Connection conn = getConn();
        if (conn == null) throw new RuntimeException("Sin conexión a la base de datos.");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al eliminar:\n" + e.getMessage(), e);
        }
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getInt("idProducto"),
            rs.getString("NombreProducto"),
            rs.getString("MarcaProducto"),
            rs.getString("CategoriaProducto"),
            rs.getInt("PrecioProducto"),
            rs.getInt("StockProducto")
        );
    }
}
