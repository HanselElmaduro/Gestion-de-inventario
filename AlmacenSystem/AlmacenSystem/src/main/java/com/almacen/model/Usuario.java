package com.almacen.model;

/**
 * PILAR OOP: ENCAPSULAMIENTO
 * Los atributos son privados y se acceden mediante getters y setters,
 * protegiendo la integridad de los datos del usuario.
 *
 * PILAR OOP: ABSTRACCIÓN
 * Esta clase abstrae la entidad "Usuario" del sistema real,
 * exponiéndo solo los datos relevantes para la aplicación.
 */
public class Usuario {

    // Atributos privados - ENCAPSULAMIENTO
    private int idUser;
    private String userName;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String password;

    // Constructor vacío
    public Usuario() {}

    // Constructor completo
    public Usuario(int idUser, String userName, String nombre, String apellido,
                   String telefono, String email, String password) {
        this.idUser   = idUser;
        this.userName = userName;
        this.nombre   = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email    = email;
        this.password = password;
    }

    // Constructor sin ID (para nuevos registros)
    public Usuario(String userName, String nombre, String apellido,
                   String telefono, String email, String password) {
        this.userName = userName;
        this.nombre   = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email    = email;
        this.password = password;
    }

    // ─── Getters y Setters ────────────────────────────────────────────────────

    public int getIdUser()              { return idUser; }
    public void setIdUser(int idUser)   { this.idUser = idUser; }

    public String getUserName()                 { return userName; }
    public void setUserName(String userName)    { this.userName = userName; }

    public String getNombre()               { return nombre; }
    public void setNombre(String nombre)    { this.nombre = nombre; }

    public String getApellido()                 { return apellido; }
    public void setApellido(String apellido)    { this.apellido = apellido; }

    public String getTelefono()                 { return telefono; }
    public void setTelefono(String telefono)    { this.telefono = telefono; }

    public String getEmail()                { return email; }
    public void setEmail(String email)      { this.email = email; }

    public String getPassword()                 { return password; }
    public void setPassword(String password)    { this.password = password; }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (@" + userName + ")";
    }
}
