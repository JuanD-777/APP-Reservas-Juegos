package com.reservas.juegos.controller;

import com.reservas.juegos.dto.ProductoDTO;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping("/crear")
    public ResponseEntity<Producto> crear(@RequestBody ProductoDTO dto) {
        Producto producto = productoService.crearProducto(dto);
        return ResponseEntity.ok(producto);
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable Long id) {
        Producto producto = productoService.obtenerProducto(id);
        return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
    }
}
