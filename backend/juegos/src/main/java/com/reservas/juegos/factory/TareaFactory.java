package com.reservas.juegos.factory;

import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.entities.TareaNormal;
import com.reservas.juegos.entities.TareaUrgente;

public class TareaFactory {

    public static Tarea crearTarea(String tipo, String nombre) {

        if (tipo.equalsIgnoreCase("urgente")) {
            return new TareaUrgente(nombre);

        } else if (tipo.equalsIgnoreCase("normal")) {
            return new TareaNormal(nombre);

        } else {
            throw new IllegalArgumentException("Tipo de tarea no válido");
        }
    }
}