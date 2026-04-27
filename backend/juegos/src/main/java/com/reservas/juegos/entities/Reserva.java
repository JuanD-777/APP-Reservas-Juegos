package com.reservas.juegos.entities;

public abstract class Reserva {
    private Long id;
    private String detalles;
    private Usuario usuario;
    private Producto producto;

    public Reserva(Long id, String detalles, Usuario usuario, Producto producto) {
        this.id = id;
        this.detalles = detalles;
        this.usuario = usuario;
        this.producto = producto;
    }

    public Long getId() { return id; }
    public String getDetalles() { return detalles; }
    public Usuario getUsuario() { return usuario; }
    public Producto getProducto() { return producto; }
}
