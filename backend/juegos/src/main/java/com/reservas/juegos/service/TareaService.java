package com.reservas.juegos.service;

import com.reservas.juegos.entities.Tarea;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TareaService {

    // lista que guarda todas las tareas mientras el programa esta corriendo
    private List<Tarea> tareas = new ArrayList<>();

    // -------------------------------------------------------
    // devuelve la lista completa de tareas
    // -------------------------------------------------------
    public List<Tarea> listar() {
        return tareas;
    }

    // -------------------------------------------------------
    // agrega una tarea nueva a la lista
    // -------------------------------------------------------
    public void agregar(Tarea tarea) {
        tareas.add(tarea);
    }

    // -------------------------------------------------------
    // lee las tareas desde un archivo CSV y las agrega a la lista
    // -------------------------------------------------------
    public void cargarDesdeCSV(String ruta) throws Exception {
        List<Tarea> cargadas = CsvService.cargar(ruta);
        tareas.addAll(cargadas);
    }

    // -------------------------------------------------------
    // guarda todas las tareas de la lista en un archivo CSV
    // -------------------------------------------------------
    public void exportarACSV(String ruta) throws Exception {
        CsvService.exportar(tareas, ruta);
    }
}
