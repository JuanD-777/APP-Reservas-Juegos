package com.reservas.juegos.controller;

import com.reservas.juegos.service.DisponibilidadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/disponibilidad")
@CrossOrigin(origins = "*")
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    // Verificar disponibilidad por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> verificar(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número válido mayor a 0");
        }
        
        boolean disponible = disponibilidadService.verificarDisponibilidad(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", disponible ? "Disponible" : "No disponible");
        response.put("id", id);
        response.put("disponible", disponible);
        
        return ResponseEntity.ok(response);
    }

    // Cambiar disponibilidad
    @PutMapping("/{id}")
    public ResponseEntity<?> cambiar(@PathVariable Long id, @RequestParam boolean disponible) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número válido mayor a 0");
        }
        
        boolean actualizado = disponibilidadService.cambiarDisponibilidad(id, disponible);
        
        if (actualizado) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Disponibilidad actualizada exitosamente");
            response.put("id", id);
            response.put("disponible", disponible);
            response.put("estado", disponible ? "Disponible" : "No disponible");
            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("Recurso con id " + id + " no encontrado");
        }
    }
}