package com.reservas.juegos.exception;

import com.reservas.juegos.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        String code = getCodeFromMessage(ex.getMessage());
        int status = getStatusFromMessage(ex.getMessage());
        ErrorResponse error = new ErrorResponse(code, ex.getMessage(), status);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        String code = getCodeFromMessage(ex.getMessage());
        int status = getStatusFromMessage(ex.getMessage());
        ErrorResponse error = new ErrorResponse(code, ex.getMessage(), status);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        String code = getCodeFromMessage(ex.getMessage());
        int status = getStatusFromMessage(ex.getMessage());
        ErrorResponse error = new ErrorResponse(code, ex.getMessage(), status);
        return ResponseEntity.status(status).body(error);
    }

    private String getCodeFromMessage(String message) {
        if (message == null) return "ERROR_VALIDACION";
        
        // ========== AUTH CONTROLLER ==========
        if (message.contains("nombre") && message.contains("obligatorio")) return "NOMBRE_REQUERIDO";
        if (message.contains("email") && message.contains("obligatorio")) return "EMAIL_REQUERIDO";
        if (message.contains("contraseña") || message.contains("password")) return "PASSWORD_REQUERIDO";
        if (message.contains("duplicado") || message.contains("ya existe")) return "EMAIL_DUPLICADO";
        if (message.contains("Credenciales")) return "CREDENCIALES_INVALIDAS";
        if (message.contains("Usuario no encontrado")) return "USUARIO_NO_ENCONTRADO";
        if (message.contains("Contraseña incorrecta")) return "CONTRASENA_INCORRECTA";
        
        // ========== CARACTERISTICA CONTROLLER ==========
        if (message.contains("clave")) return "CLAVE_REQUERIDA";
        if (message.contains("valor")) return "VALOR_REQUERIDO";
        if (message.contains("Característica con id") && message.contains("no encontrada")) return "CARACTERISTICA_NO_ENCONTRADA";
        if (message.contains("Producto no encontrado")) return "PRODUCTO_NO_ENCONTRADO";
        if (message.contains("ID debe ser")) return "ID_INVALIDO";
        
        // ========== BUSQUEDA CONTROLLER ==========
        if (message.contains("búsqueda") && message.contains("3 caracteres")) return "BUSQUEDA_DEMASIADO_CORTA";
        if (message.contains("obligatorio para la búsqueda")) return "NOMBRE_BUSQUEDA_REQUERIDO";
        
        // ========== CATEGORIA CONTROLLER ==========
        if (message.contains("Categoría con id") && message.contains("no encontrada")) return "CATEGORIA_NO_ENCONTRADA";
        if (message.contains("nombre de la categoría") && message.contains("obligatorio")) return "NOMBRE_CATEGORIA_REQUERIDO";
        if (message.contains("nombre de la categoría debe tener al menos 3 caracteres")) return "NOMBRE_CATEGORIA_DEMASIADO_CORTO";
        if (message.contains("datos de la categoría")) return "DATOS_INVALIDOS";
        if (message.contains("Ya existe una categoría con el nombre")) return "NOMBRE_CATEGORIA_DUPLICADO";
        if (message.contains("Error al listar las categorías")) return "ERROR_LISTAR_CATEGORIAS";
        if (message.contains("Error al buscar la categoría")) return "ERROR_BUSCAR_CATEGORIA";
        if (message.contains("Error al crear la categoría")) return "ERROR_CREAR_CATEGORIA";
        if (message.contains("Error al actualizar la categoría")) return "ERROR_ACTUALIZAR_CATEGORIA";
        if (message.contains("Error al eliminar la categoría")) return "ERROR_ELIMINAR_CATEGORIA";
        
        // ========== FAVORITO CONTROLLER ==========
        if (message.contains("datos del favorito")) return "DATOS_FAVORITO_REQUERIDOS";
        if (message.contains("ID del usuario es obligatorio")) return "USUARIO_ID_REQUERIDO";
        if (message.contains("ID del producto es obligatorio")) return "PRODUCTO_ID_REQUERIDO";
        if (message.contains("ID del favorito debe ser mayor")) return "ID_FAVORITO_INVALIDO";
        if (message.contains("El favorito ya existe")) return "FAVORITO_DUPLICADO";
        if (message.contains("Favorito con id") && message.contains("no encontrado")) return "FAVORITO_NO_ENCONTRADO";
        if (message.contains("no encontrado para el usuario")) return "FAVORITO_NO_ENCONTRADO";
        if (message.contains("Error al crear el favorito")) return "ERROR_CREAR_FAVORITO";
        if (message.contains("Error al listar los favoritos")) return "ERROR_LISTAR_FAVORITOS";
        
        // ========== DISPONIBILIDAD CONTROLLER ==========
        if (message.contains("Error al verificar disponibilidad")) return "ERROR_VERIFICAR_DISPONIBILIDAD";
        if (message.contains("Error al cambiar disponibilidad")) return "ERROR_CAMBIAR_DISPONIBILIDAD";
        if (message.contains("Recurso con id") && message.contains("no encontrado")) return "RECURSO_NO_ENCONTRADO";
        
        // ========== GENERAL ==========
        if (message.contains("Error interno")) return "ERROR_INTERNO";
        
        return "ERROR_VALIDACION";
    }

    private int getStatusFromMessage(String message) {
        if (message == null) return 400;
        
        // ========== 401 UNAUTHORIZED ==========
        if (message.contains("Credenciales") || 
            message.contains("Usuario no encontrado") || 
            message.contains("Contraseña incorrecta")) {
            return 401;
        }
        
        // ========== 404 NOT FOUND ==========
        if (message.contains("no encontrada") || 
            message.contains("no encontrado") ||
            message.contains("Recurso con id")) {
            return 404;
        }
        
        // ========== 400 BAD REQUEST (default) ==========
        return 400;
    }
}