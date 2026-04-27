package com.reservas.juegos.entities;

public class ReservaSinFecha extends Reserva {
    public ReservaSinFecha(Long id, String detalles, Usuario usuario, Producto producto) {
        super(id, detalles, usuario, producto);
    }
}
