package com.reservas.juegos.entities;

public class ReservaConFecha extends Reserva {
    private String fecha;

    public ReservaConFecha(Long id, String detalles, Usuario usuario, Producto producto, String fecha) {
        super(id, detalles, usuario, producto);
        this.fecha = fecha;
    }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
