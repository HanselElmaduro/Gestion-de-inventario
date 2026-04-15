package com.almacen.model;

/**
 * PILAR OOP: ENCAPSULAMIENTO
 * Atributos privados con acceso controlado mediante getters y setters.
 *
 * PILAR OOP: ABSTRACCIÓN
 * Representa la entidad "Producto" del almacén con solo los campos necesarios.
 */
public class Producto {

    private int    idProducto;
    private String nombreProducto;
    private String marcaProducto;
    private String categoriaProducto;
    private double precioProducto;
    private int    stockProducto;

    public Producto() {}

    public Producto(int idProducto, String nombreProducto, String marcaProducto,
                    String categoriaProducto, double precioProducto, int stockProducto) {
        this.idProducto        = idProducto;
        this.nombreProducto    = nombreProducto;
        this.marcaProducto     = marcaProducto;
        this.categoriaProducto = categoriaProducto;
        this.precioProducto    = precioProducto;
        this.stockProducto     = stockProducto;
    }

    public Producto(String nombreProducto, String marcaProducto,
                    String categoriaProducto, double precioProducto, int stockProducto) {
        this.nombreProducto    = nombreProducto;
        this.marcaProducto     = marcaProducto;
        this.categoriaProducto = categoriaProducto;
        this.precioProducto    = precioProducto;
        this.stockProducto     = stockProducto;
    }

    // ─── Getters y Setters ────────────────────────────────────────────────────

    public int getIdProducto()                  { return idProducto; }
    public void setIdProducto(int idProducto)   { this.idProducto = idProducto; }

    public String getNombreProducto()                       { return nombreProducto; }
    public void setNombreProducto(String nombreProducto)    { this.nombreProducto = nombreProducto; }

    public String getMarcaProducto()                        { return marcaProducto; }
    public void setMarcaProducto(String marcaProducto)      { this.marcaProducto = marcaProducto; }

    public String getCategoriaProducto()                            { return categoriaProducto; }
    public void setCategoriaProducto(String categoriaProducto)      { this.categoriaProducto = categoriaProducto; }

    public double getPrecioProducto()                       { return precioProducto; }
    public void setPrecioProducto(double precioProducto)    { this.precioProducto = precioProducto; }

    public int getStockProducto()                       { return stockProducto; }
    public void setStockProducto(int stockProducto)     { this.stockProducto = stockProducto; }

    @Override
    public String toString() {
        return nombreProducto + " - " + marcaProducto;
    }
}
