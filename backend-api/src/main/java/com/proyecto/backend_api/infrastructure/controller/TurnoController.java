package com.proyecto.backend_api.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.backend_api.application.service.TurnoService;
import com.proyecto.backend_api.domain.dto.request.CrearTurnoRequest;
import com.proyecto.backend_api.domain.dto.response.TurnoResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/turnos")
public class TurnoController {
    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @PostMapping
    @Operation(summary = "Crear nuevo turno", description = "Crea un nuevo turno medico para un paciente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Turno creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TurnoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })

    public ResponseEntity<TurnoResponse> crearTurno(@Valid @RequestBody CrearTurnoRequest request) {
        TurnoResponse turnoResponse = turnoService.crearTurno(request);
        return ResponseEntity.ok(turnoResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener turno por ID", description = "Recupera los detalles de un turno especifico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Turno encontrado", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = TurnoResponse.class)
        )),
        @ApiResponse(responseCode = "404", description = "Turno no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")

    })
    public ResponseEntity<TurnoResponse> obtenerTurno(@PathVariable Long id) {
        TurnoResponse turnoResponse = turnoService.obtenerTurnos(id);
        return ResponseEntity.ok(turnoResponse);
    }

    @GetMapping
    @Operation(summary = "Listar todos los turnos", description = "Obtiene la lista de todos los turnos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de turnos obtenido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TurnoResponse.class)))
    })

    public ResponseEntity<List<TurnoResponse>> listarTurnos() {
        List<TurnoResponse> turnos = turnoService.listarTurnos();
        return ResponseEntity.ok(turnos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar Turno", description = "Cancela un turno medico existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Turno cancelado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Turno no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")

    })

    public ResponseEntity<Void> cancelarTurno(@PathVariable Long id) {
        turnoService.cancelarTurno(id);
        return ResponseEntity.noContent().build();
    }
}
