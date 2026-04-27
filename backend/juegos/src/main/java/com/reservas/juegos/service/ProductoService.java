package com.reservas.juegos.service;

import com.reservas.juegos.dto.ProductoDTO;
import com.reservas.juegos.entities.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService {
    private final List<Producto> productos = new ArrayList<>();
    private static Long contador = 1L;

    public Producto crearProducto(ProductoDTO dto) {
        Producto producto = new Producto(
                contador++,
                dto.getNombre(),
                dto.getCategoria(),
                dto.getDescripcion(),
                dto.getPrecio(),
                true // disponible por defecto
        );
        productos.add(producto);
        return producto;
    }

    public List<Producto> listarProductos() {
        return productos;
    }

    public Producto obtenerProducto(Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
