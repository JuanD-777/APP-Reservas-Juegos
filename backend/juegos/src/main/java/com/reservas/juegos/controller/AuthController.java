package com.reservas.juegos.controller;

import com.reservas.juegos.dto.UsuarioDTO;
import com.reservas.juegos.entities.Usuario;
import com.reservas.juegos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Map<String, String> body) throws Exception {
        String email = body.get("email");
        String password = body.get("password");
        String nombre = body.get("nombre");

        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        Usuario usuario = usuarioService.registrar(email, password, nombre);
        UsuarioDTO responseDTO = new UsuarioDTO(usuario.getNombre(), usuario.getEmail());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) throws Exception {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // Verificar admin hardcodeado
        if (usuarioService.esAdmin(email, password)) {
            UsuarioDTO adminDTO = new UsuarioDTO("Administrador", email);
            Map<String, Object> response = new HashMap<>();
            response.put("usuario", adminDTO);
            response.put("rol", "ADMIN");
            response.put("id", 0L);
            return ResponseEntity.ok(response);
        }

        Usuario usuario = usuarioService.login(email, password);
        UsuarioDTO responseDTO = new UsuarioDTO(usuario.getNombre(), usuario.getEmail());
        
        Map<String, Object> response = new HashMap<>();
        response.put("usuario", responseDTO);
        response.put("rol", usuario.getRol());
        response.put("id", usuario.getId());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada"));
    }

    @GetMapping("/verificar-admin")
    public ResponseEntity<?> verificarAdmin(@RequestParam String email, @RequestParam String password) {
        boolean esAdmin = usuarioService.esAdmin(email, password);
        return ResponseEntity.ok(Map.of("esAdmin", esAdmin));
    }
}