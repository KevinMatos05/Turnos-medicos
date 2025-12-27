package com.proyecto.backend_api.infrastructure.controller;

import com.proyecto.backend_api.application.usecase.ConsultarDisponibilidadUseCase;
import com.proyecto.backend_api.domain.dto.response.DisponibilidadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agenda")
@Tag(name = "Agenda", description = "Endpoints para consultar disponibilidad de médicos")
public class AgendaController {

    private final ConsultarDisponibilidadUseCase consultarDisponibilidadUseCase;

    public AgendaController(ConsultarDisponibilidadUseCase consultarDisponibilidadUseCase) {
        this.consultarDisponibilidadUseCase = consultarDisponibilidadUseCase;
    }

    @GetMapping("/medico/{medicoId}")
    @Operation(summary = "Consultar disponibilidad de médico", 
               description = "Obtiene los horarios disponibles de un médico para una fecha específica o rango de fechas")
    public ResponseEntity<List<DisponibilidadResponse>> consultarDisponibilidad(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        
        List<DisponibilidadResponse> disponibilidades;
        
        if (fechaFin != null) {
            disponibilidades = consultarDisponibilidadUseCase
                .consultarDisponibilidadRango(medicoId, fecha, fechaFin);
        } else {
            disponibilidades = consultarDisponibilidadUseCase
                .consultarDisponibilidad(medicoId, fecha);
        }
        
        return ResponseEntity.ok(disponibilidades);
    }
}
