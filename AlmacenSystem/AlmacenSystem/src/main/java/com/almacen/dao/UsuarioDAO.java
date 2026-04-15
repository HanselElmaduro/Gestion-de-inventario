package com.almacen.dao;

import com.almacen.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN DE DISEÑO: DAO (Data Access Object)
 * CORRECCIÓN: Conexión obtenida fresca en cada operación.
 */
public class UsuarioDAO {

    private Connection getConn() {
        return ConexionDB.getInstancia().getConexion();
    }

    public Usuario login(String userName, String password) {
        String sql = "SELECT * FROM usuarios WHERE UserName=? AND Password=?";
        Connection conn = getConn();
        if (conn == null) return null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userName);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearUsuario(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY Nombre";
        Connection conn = getConn();
        if (conn == null) return lista;
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearUsuario(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (UserName,Nombre,Apellido,Telefono,Email,Password) VALUES (?,?,?,?,?,?)";
        Connection conn = getConn();
        if (conn == null) throw new RuntimeException("Sin conexión a la base de datos.");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUserName());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getEmail());
            ps.setString(6, u.getPassword());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL:\n" + e.getMessage(), e);
        }
    }

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET UserName=?,Nombre=?,Apellido=?,Telefono=?,Email=?,Password=? WHERE idUser=?";
        Connection conn = getConn();
        if (conn == null) throw new RuntimeException("Sin conexión a la base de datos.");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUserName());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getEmail());
            ps.setString(6, u.getPassword());
            ps.setInt(7, u.getIdUser());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL:\n" + e.getMessage(), e);
        }
    }

    public boolean eliminar(int idUser) {
        String sql = "DELETE FROM usuarios WHERE idUser=?";
        Connection conn = getConn();
        if (conn == null) throw new RuntimeException("Sin conexión a la base de datos.");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL:\n" + e.getMessage(), e);
        }
    }

    public boolean existeUserName(String userName) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE UserName=?";
        Connection conn = getConn();
        if (conn == null) return false;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("idUser"), rs.getString("UserName"),
            rs.getString("Nombre"), rs.getString("Apellido"),
            rs.getString("Telefono"), rs.getString("Email"),
            rs.getString("Password")
        );
    }
}
