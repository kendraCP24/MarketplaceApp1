package com.example.marketplaceapp;

public class Producto {
    private String id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String precio;
    private String imagenUrl;
    private String usuarioId; // 🔹 Usuario que publicó el producto
    private long fechaPublicacion; // 🔹 Para ordenar si quieres

    //  Constructor vacío requerido por Firebase
    public Producto() {}

    //  Constructor con todos los campos
    public Producto(String id, String nombre, String descripcion, String categoria,
                    String precio, String imagenUrl, String usuarioId, long fechaPublicacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.usuarioId = usuarioId;
        this.fechaPublicacion = fechaPublicacion;
    }

    // 🔹 Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPrecio() { return precio; }
    public void setPrecio(String precio) { this.precio = precio; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public long getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(long fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
}

