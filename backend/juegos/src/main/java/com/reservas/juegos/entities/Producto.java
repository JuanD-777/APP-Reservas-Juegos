package com.reservas.juegos.entities;

public class Producto {
    private Long id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private double precio;
    private boolean disponible;

    public Producto(Long id, String nombre, String categoria, String descripcion, double precio, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = disponible;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
