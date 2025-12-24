package com.proyecto.backend_api.infrastructure.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.backend_api.application.service.HorarioLaboralService;
import com.proyecto.backend_api.domain.dto.request.ActualizarHorarioRequest;
import com.proyecto.backend_api.domain.dto.response.HorarioLaboralResponse;
import com.proyecto.backend_api.domain.model.HorarioLaboral;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/horarios")
public class HorarioLaboralController {
    
    @Autowired
    private HorarioLaboralService horarioLaboralService;

    @GetMapping
    public ResponseEntity<List<HorarioLaboralResponse>> obtenerHorarios() {
        List<HorarioLaboralResponse> horarios = horarioLaboralService.obtenerTodosLosHorarios();
        return ResponseEntity.ok(horarios);
    }

    @PostMapping
    public ResponseEntity<HorarioLaboral> crearHorario(@RequestBody HorarioLaboral horarioLaboral){
        HorarioLaboral nuevoHorario = horarioLaboralService.crearHorario(horarioLaboral);
        return ResponseEntity.status(201).body(nuevoHorario);        
    }

    @PostMapping("/{id}")
    public ResponseEntity<HorarioLaboral> actualizarHorario(@PathVariable Long id, @RequestBody ActualizarHorarioRequest request) {
        HorarioLaboral horarioActualizado = horarioLaboralService.actualizarHorario(id, request);
        return ResponseEntity.ok(horarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long id) {
        horarioLaboralService.eliminarHorario(id);
        return ResponseEntity.noContent().build();
    }
}


