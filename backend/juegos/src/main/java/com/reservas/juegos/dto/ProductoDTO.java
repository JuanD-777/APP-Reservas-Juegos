package com.reservas.juegos.dto;

public class ProductoDTO {
    private String nombre;
    private String categoria;
    private String descripcion;
    private double precio;

    public ProductoDTO() {}

    public ProductoDTO(String nombre, String categoria, String descripcion, double precio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
}
