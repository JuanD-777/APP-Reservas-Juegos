package com.reservas.juegos.factory;

import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.entities.Reserva;
import com.reservas.juegos.entities.ReservaConFecha;
import com.reservas.juegos.entities.ReservaSinFecha;
import com.reservas.juegos.entities.Usuario;

public class ReservaFactory {
    private static Long contador = 1L;

    // Crear reserva con fecha
    public static Reserva crearReservaConFecha(String detalles, Usuario usuario, Producto producto, String fecha) {
        return new ReservaConFecha(
                contador++,
                detalles,
                usuario,
                producto,
                fecha
        );
    }

    // Crear reserva sin fecha
    public static Reserva crearReservaSinFecha(String detalles, Usuario usuario, Producto producto) {
        return new ReservaSinFecha(
                contador++,
                detalles,
                usuario,
                producto
        );
    }
}
