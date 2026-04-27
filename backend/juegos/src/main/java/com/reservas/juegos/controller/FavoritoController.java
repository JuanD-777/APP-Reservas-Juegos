package com.reservas.juegos.controller;

import com.reservas.juegos.dto.FavoritoDTO;
import com.reservas.juegos.entities.Favorito;
import com.reservas.juegos.service.FavoritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
public class FavoritoController {
    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @PostMapping("/crear")
    public ResponseEntity<Favorito> crear(@RequestBody FavoritoDTO dto) {
        Favorito favorito = favoritoService.crearFavorito(dto);
        return ResponseEntity.ok(favorito);
    }

    @GetMapping
    public ResponseEntity<List<Favorito>> listar() {
        return ResponseEntity.ok(favoritoService.listarFavoritos());
    }
}
