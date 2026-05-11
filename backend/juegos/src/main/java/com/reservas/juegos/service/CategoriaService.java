package com.reservas.juegos.service;

import com.reservas.juegos.dto.CategoriaDTO;
import com.reservas.juegos.entities.Categoria;
import com.reservas.juegos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public Categoria crear(CategoriaDTO dto) {
        // Validación para evitar duplicados
        if (categoriaRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + dto.getNombre());
        }
        
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> actualizar(Long id, CategoriaDTO dto) {
        return categoriaRepository.findById(id).map(categoria -> {
            // Validación para evitar duplicados en actualización
            if (categoriaRepository.existsByNombre(dto.getNombre()) && 
                !categoria.getNombre().equalsIgnoreCase(dto.getNombre())) {
                throw new RuntimeException("Ya existe una categoría con el nombre: " + dto.getNombre());
            }
            categoria.setNombre(dto.getNombre());
            return categoriaRepository.save(categoria);
        });
    }

    public boolean eliminar(Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}