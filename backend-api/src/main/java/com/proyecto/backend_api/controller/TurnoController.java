package com.proyecto.backend_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import com.proyecto.backend_api.domain.dto.TurnoSolicitudDTO;
import com.proyecto.backend_api.domain.dto.response.TurnoRespuestaDTO;
import com.proyecto.backend_api.domain.services.TurnoService;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {
    @Autowired
    private TurnoService turnoService;

    // CREAR TURNO
    @PostMapping
    public ResponseEntity<TurnoRespuestaDTO> crearTurno(@RequestBody TurnoSolicitudDTO solicitud) {
        TurnoRespuestaDTO newTurno = turnoService.reservarTurno(solicitud);
        return ResponseEntity.ok(newTurno);
    }

    // LISTAR TURNOS CON FILTROS
    @GetMapping
    public ResponseEntity<List<TurnoRespuestaDTO>> listarTurnos(
        @RequestParam(required = false) LocalDate fecha,
        @RequestParam(required = false) String medico
    ) {
        return ResponseEntity.ok(turnoService.listarTurnos(fecha, medico));
    }

    // Obtener uno (ID)
    @GetMapping("/{id}")
    public ResponseEntity<TurnoRespuestaDTO> obtenerTurnos(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(turnoService.obtenerPorId(id));
    }

    // Editar Turno
    @PostMapping("/{id}")
    public ResponseEntity<TurnoRespuestaDTO> actualizarTurno(
        @PathVariable Long id,
        @RequestBody TurnoSolicitudDTO solicitud
    ) {
        return ResponseEntity.ok(turnoService.editarTurno(id, solicitud));
    }

    // Eliminar Turno
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTurno(@PathVariable Long id) {
        turnoService.eliminarTurno(id);
        return ResponseEntity.noContent().build();
    }
}
