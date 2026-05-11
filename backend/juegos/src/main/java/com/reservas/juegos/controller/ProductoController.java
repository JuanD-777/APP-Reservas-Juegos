package com.reservas.juegos.controller;

import com.reservas.juegos.dto.ProductoDTO;
import com.reservas.juegos.entities.Caracteristica;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.service.CaracteristicaService;
import com.reservas.juegos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CaracteristicaService caracteristicaService;

    // ── GET /api/productos ──────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<?> listarTodos(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page == null || size == null) {
            List<Producto> productos = productoService.listarTodos();
            Map<String, Object> response = new HashMap<>();
            response.put("message", productos.isEmpty() ? "No hay productos registrados" : "Listado de productos");
            response.put("cantidad", productos.size());
            response.put("productos", productos);
            return ResponseEntity.ok(response);
        }

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("La página debe ser mayor o igual a 0 y el tamaño mayor a 0");
        }

        Page<Producto> productosPage = productoService.listarPaginado(page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Listado paginado de productos");
        response.put("contenido", productosPage.getContent());
        response.put("paginaActual", productosPage.getNumber());
        response.put("totalElementos", productosPage.getTotalElements());
        response.put("totalPaginas", productosPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // ── GET /api/productos/{id} ─────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número válido mayor a 0");
        }

        return productoService.buscarPorId(id)
                .map(producto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Producto encontrado");
                    response.put("producto", producto);
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Producto con id " + id + " no encontrado"));
    }

    // ── POST /api/productos ─────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("Los datos del producto son requeridos");
        }
        if (producto.getTitulo() == null || producto.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título del producto es obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor a 0");
        }

        Producto creado = productoService.crear(producto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Producto creado exitosamente");
        response.put("producto", creado);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ── PUT /api/productos/{id} ─────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Producto datos) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número válido mayor a 0");
        }
        if (datos == null) {
            throw new IllegalArgumentException("Los datos para actualizar son requeridos");
        }

        return productoService.actualizar(id, datos)
                .map(producto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Producto actualizado exitosamente");
                    response.put("producto", producto);
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Producto con id " + id + " no encontrado"));
    }

    // ── DELETE /api/productos/{id} ──────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número válido mayor a 0");
        }

        boolean eliminado = productoService.eliminar(id);
        
        if (eliminado) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Producto eliminado exitosamente");
            response.put("id_eliminado", id);
            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("Producto con id " + id + " no encontrado");
        }
    }

    // ── POST /api/productos/{id}/categorias/{categoriaId} ──────────────────────
    @PostMapping("/{id}/categorias/{categoriaId}")
    public ResponseEntity<?> asignarCategoria(
            @PathVariable("id") Long productoId,
            @PathVariable Long categoriaId) {
        
        if (productoId == null || productoId <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser mayor a 0");
        }
        if (categoriaId == null || categoriaId <= 0) {
            throw new IllegalArgumentException("El ID de la categoría debe ser mayor a 0");
        }

        return productoService.asignarCategoria(productoId, categoriaId)
                .map(producto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Categoría asignada exitosamente");
                    response.put("producto", producto);
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Producto o categoría no encontrado"));
    }

    // ── DELETE /api/productos/{id}/categorias/{categoriaId} ────────────────────
    @DeleteMapping("/{id}/categorias/{categoriaId}")
    public ResponseEntity<?> quitarCategoria(
            @PathVariable("id") Long productoId,
            @PathVariable Long categoriaId) {
        
        if (productoId == null || productoId <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser mayor a 0");
        }
        if (categoriaId == null || categoriaId <= 0) {
            throw new IllegalArgumentException("El ID de la categoría debe ser mayor a 0");
        }

        return productoService.quitarCategoria(productoId, categoriaId)
                .map(producto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Categoría removida exitosamente");
                    response.put("producto", producto);
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // ── GET /api/productos/{id}/caracteristicas ─────────────────────────────────
    @GetMapping("/{id}/caracteristicas")
    public ResponseEntity<?> verCaracteristicas(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }

        return caracteristicaService.verCaracteristicasDeProducto(id)
                .map(caracteristicas -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Características del producto");
                    response.put("productoId", id);
                    response.put("cantidad", caracteristicas.size());
                    response.put("caracteristicas", caracteristicas);
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Producto con id " + id + " no encontrado"));
    }

    // ── GET /api/productos/{id}/politicas ───────────────────────────────────────
    @GetMapping("/{id}/politicas")
    public ResponseEntity<?> verPoliticas(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }

        return productoService.obtenerPoliticas(id)
                .map(politicas -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Políticas del producto");
                    response.put("productoId", id);
                    response.put("politicas", politicas != null ? politicas : "Sin políticas definidas");
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Producto con id " + id + " no encontrado"));
    }

    // ── GET /api/productos/{id}/compartir ───────────────────────────────────────
    @GetMapping("/{id}/compartir")
    public ResponseEntity<?> compartir(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }

        return productoService.obtenerDatosCompartir(id)
                .map(datos -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Datos para compartir");
                    response.put("productoId", id);
                    response.put("datos", datos);
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Producto con id " + id + " no encontrado"));
    }

    // ── POST /api/productos/{id}/puntuar ────────────────────────────────────────
    @PostMapping("/{id}/puntuar")
    public ResponseEntity<?> puntuar(@PathVariable Long id, @RequestBody Map<String, Double> body) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }

        Double puntuacion = body.get("puntuacion");
        if (puntuacion == null) {
            throw new IllegalArgumentException("El campo 'puntuacion' es obligatorio");
        }
        if (puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");
        }

        return productoService.puntuar(id, puntuacion)
                .map(producto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Producto puntuado exitosamente");
                    response.put("productoId", id);
                    response.put("puntuacion", puntuacion);
                    response.put("promedio", producto.getRating());
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Producto con id " + id + " no encontrado"));
    }

    // ── POST /api/productos/importarRawg ────────────────────────────────────────
    @PostMapping("/importarRawg")
    public ResponseEntity<?> importarRawg(@RequestBody ProductoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos para importar son requeridos");
        }
        if (dto.getRawgId() == null || dto.getRawgId() <= 0) {
            throw new IllegalArgumentException("El ID de RAWG es obligatorio");
        }
        if (dto.getPrecio() == null || dto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio es obligatorio y debe ser mayor a 0");
        }

        Producto nuevo = productoService.importarDesdeRawg(
            dto.getRawgId(),
            dto.getPrecio(),
            dto.getStock(),
            dto.getPlataforma()
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Producto importado desde RAWG exitosamente");
        response.put("producto", nuevo);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}