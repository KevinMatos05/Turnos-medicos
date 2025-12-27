package com.proyecto.backend_api.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.backend_api.application.service.PacienteService;
import com.proyecto.backend_api.application.service.TurnoService;
import com.proyecto.backend_api.domain.dto.request.RegistroUsuarioRequest;
import com.proyecto.backend_api.domain.dto.response.TurnoResponse;
import com.proyecto.backend_api.domain.dto.response.UsuarioResponse;
import com.proyecto.backend_api.domain.model.Paciente;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pacientes")
@Tag(name = "Pacientes", description = "Endpoints para gestión de pacientes")
public class PacienteController {
    private final PacienteService pacienteService;
    private final TurnoService turnoService;

    public PacienteController(PacienteService pacienteService, TurnoService turnoService) {
        this.pacienteService = pacienteService;
        this.turnoService = turnoService;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registarPaciente(@RequestBody RegistroUsuarioRequest request) {
        UsuarioResponse usuarioResponse = pacienteService.registrarPaciente(request);
        return ResponseEntity.ok(usuarioResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPaciente(@PathVariable Long id) {
        UsuarioResponse usuarioResponse = pacienteService.obtenerPacientePorIdResponse(id);
        return ResponseEntity.ok(usuarioResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarPaciente(@PathVariable Long id, @RequestBody RegistroUsuarioRequest request) {
        UsuarioResponse usuarioResponse = pacienteService.actualizarPaciente(id, request);
        return ResponseEntity.ok(usuarioResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        pacienteService.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/turnos")
    @Operation(summary = "Obtener turnos de un paciente", 
               description = "Recupera el historial completo de turnos de un paciente específico ordenados por fecha descendente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de turnos obtenida exitosamente",
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = TurnoResponse.class))),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<List<TurnoResponse>> obtenerTurnosPorPaciente(@PathVariable Long id) {
        Paciente paciente = pacienteService.obtenerPacienteById(id);
        List<TurnoResponse> turnos = turnoService.obtenerTurnosPorPaciente(paciente);
        return ResponseEntity.ok(turnos);
    }
}
