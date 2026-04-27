package com.reservas.juegos.dto;

import java.time.LocalDate;

public class ReservaDTO {
    private String nombreCliente;
    private String emailCliente;
    private Long productoId;
    private LocalDate fechaReserva;
    private LocalDate fechaDevolucion;
    private String tipo;

    public ReservaDTO() {}

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public String getTipo() {
    return tipo;
    }

    public void setTipo(String tipo) {
    this.tipo = tipo;
    }
}