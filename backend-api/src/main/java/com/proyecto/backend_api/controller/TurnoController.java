package com.proyecto.backend_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.backend_api.dto.TurnoRespuestaDTO;
import com.proyecto.backend_api.dto.TurnoSolicitudDTO;
import com.proyecto.backend_api.services.TurnoService;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {
    @Autowired
    private TurnoService turnoService;

    @PostMapping
    public ResponseEntity<TurnoRespuestaDTO> crearTurno(@RequestBody TurnoSolicitudDTO solicitud) {
        TurnoRespuestaDTO newTurno = turnoService.reservarTurno(solicitud);
        return ResponseEntity.ok(newTurno);

    }

}
