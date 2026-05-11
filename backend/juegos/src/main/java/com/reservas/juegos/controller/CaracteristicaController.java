package com.reservas.juegos.controller;

import com.reservas.juegos.dto.CaracteristicaDTO;
import com.reservas.juegos.entities.Caracteristica;
import com.reservas.juegos.service.CaracteristicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/caracteristicas")
@CrossOrigin(origins = "*")
public class CaracteristicaController {

    @Autowired
    private CaracteristicaService caracteristicaService;

    @GetMapping
    public ResponseEntity<?> listarTodas() {
        List<Caracteristica> caracteristicas = caracteristicaService.listarTodas();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", caracteristicas.isEmpty() ? "No hay características registradas" : "Listado de características");
        response.put("cantidad", caracteristicas.size());
        response.put("caracteristicas", caracteristicas);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número válido mayor a 0");
        }
        
        return caracteristicaService.buscarPorId(id)
                .map(caracteristica -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Característica encontrada");
                    response.put("caracteristica", caracteristica);
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Característica con id " + id + " no encontrada"));
    }

    @PostMapping
    public ResponseEntity<?> agregar(@RequestBody CaracteristicaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de la característica son requeridos");
        }
        if (dto.getClave() == null || dto.getClave().isBlank()) {
            throw new IllegalArgumentException("La clave de la característica es obligatoria");
        }
        if (dto.getValor() == null || dto.getValor().isBlank()) {
            throw new IllegalArgumentException("El valor de la característica es obligatorio");
        }
        
        return caracteristicaService.crear(dto)
                .map(caracteristica -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Característica creada exitosamente");
                    response.put("caracteristica", caracteristica);
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado para asociar la característica"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody CaracteristicaDTO dto) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número válido mayor a 0");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Los datos para actualizar son requeridos");
        }
        
        return caracteristicaService.actualizar(id, dto)
                .map(caracteristica -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Característica actualizada exitosamente");
                    response.put("caracteristica", caracteristica);
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Característica con id " + id + " no encontrada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número válido mayor a 0");
        }
        
        boolean eliminado = caracteristicaService.eliminar(id);
        
        if (eliminado) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Característica eliminada exitosamente");
            response.put("id_eliminado", id);
            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("Característica con id " + id + " no encontrada");
        }
    }
}