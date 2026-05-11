package com.reservas.juegos.controller;

import com.reservas.juegos.dto.CategoriaDTO;
import com.reservas.juegos.entities.Categoria;
import com.reservas.juegos.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<?> listarTodas() {
        List<Categoria> categorias = categoriaService.listarTodas();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", categorias.isEmpty() ? "No hay categorías registradas" : "Listado de categorías");
        response.put("cantidad", categorias.size());
        response.put("categorias", categorias);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número válido mayor a 0");
        }
        
        return categoriaService.buscarPorId(id)
                .map(categoria -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Categoría encontrada");
                    response.put("categoria", categoria);
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Categoría con id " + id + " no encontrada"));
    }

    @PostMapping
    public ResponseEntity<?> agregar(@RequestBody CategoriaDTO dto) {
        // Validaciones
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de la categoría son requeridos");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        if (dto.getNombre().length() < 3) {
            throw new IllegalArgumentException("El nombre de la categoría debe tener al menos 3 caracteres");
        }
        
        // Esto ahora validará duplicados y lanzará excepción
        Categoria nueva = categoriaService.crear(dto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Categoría creada exitosamente");
        response.put("categoria", nueva);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody CategoriaDTO dto) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número válido mayor a 0");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Los datos para actualizar son requeridos");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        
        return categoriaService.actualizar(id, dto)
                .map(categoria -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Categoría actualizada exitosamente");
                    response.put("categoria", categoria);
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Categoría con id " + id + " no encontrada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número válido mayor a 0");
        }
        
        boolean eliminada = categoriaService.eliminar(id);
        
        if (eliminada) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Categoría eliminada exitosamente");
            response.put("id_eliminado", id);
            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("Categoría con id " + id + " no encontrada");
        }
    }
}