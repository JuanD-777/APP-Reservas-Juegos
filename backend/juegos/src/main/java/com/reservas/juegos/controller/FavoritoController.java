package com.reservas.juegos.controller;

import com.reservas.juegos.dto.FavoritoDTO;
import com.reservas.juegos.entities.Favorito;
import com.reservas.juegos.service.FavoritoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = "*")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    // Crear favorito
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody FavoritoDTO dto) {
        // Validaciones
        if (dto == null) {
            throw new IllegalArgumentException("Los datos del favorito son requeridos");
        }
        if (dto.getUsuarioId() == null || dto.getUsuarioId() <= 0) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio y debe ser mayor a 0");
        }
        if (dto.getProductoId() == null || dto.getProductoId() <= 0) {
            throw new IllegalArgumentException("El ID del producto es obligatorio y debe ser mayor a 0");
        }
        
        Favorito favorito = favoritoService.crearFavorito(dto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Favorito agregado exitosamente");
        response.put("favorito", favorito);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar todos los favoritos
    @GetMapping
    public ResponseEntity<?> listar() {
        List<Favorito> favoritos = favoritoService.listarFavoritos();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", favoritos.isEmpty() ? "No hay favoritos registrados" : "Listado de favoritos");
        response.put("cantidad", favoritos.size());
        response.put("favoritos", favoritos);
        
        return ResponseEntity.ok(response);
    }

    // Listar favoritos por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser mayor a 0");
        }
        
        List<Favorito> favoritos = favoritoService.listarPorUsuario(usuarioId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", favoritos.isEmpty() ? "No hay favoritos para este usuario" : "Favoritos del usuario");
        response.put("usuarioId", usuarioId);
        response.put("cantidad", favoritos.size());
        response.put("favoritos", favoritos);
        
        return ResponseEntity.ok(response);
    }

    // Eliminar favorito por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del favorito debe ser mayor a 0");
        }
        
        boolean eliminado = favoritoService.eliminarFavorito(id);
        
        if (eliminado) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Favorito eliminado exitosamente");
            response.put("id_eliminado", id);
            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("Favorito con id " + id + " no encontrado");
        }
    }

    // Eliminar favorito por usuario y producto
    @DeleteMapping("/usuario/{usuarioId}/producto/{productoId}")
    public ResponseEntity<?> eliminarPorUsuarioYProducto(
            @PathVariable Long usuarioId, 
            @PathVariable Long productoId) {
        
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser mayor a 0");
        }
        if (productoId == null || productoId <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser mayor a 0");
        }
        
        boolean eliminado = favoritoService.eliminarFavoritoPorUsuarioYProducto(usuarioId, productoId);
        
        if (eliminado) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Favorito eliminado exitosamente");
            response.put("usuarioId", usuarioId);
            response.put("productoId", productoId);
            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("Favorito no encontrado para el usuario " + usuarioId + " y producto " + productoId);
        }
    }

    // Verificar si un producto es favorito de un usuario
    @GetMapping("/verificar")
    public ResponseEntity<?> esFavorito(
            @RequestParam Long usuarioId,
            @RequestParam Long productoId) {
        
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser mayor a 0");
        }
        if (productoId == null || productoId <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser mayor a 0");
        }
        
        boolean esFavorito = favoritoService.esFavorito(usuarioId, productoId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("usuarioId", usuarioId);
        response.put("productoId", productoId);
        response.put("esFavorito", esFavorito);
        
        return ResponseEntity.ok(response);
    }
}