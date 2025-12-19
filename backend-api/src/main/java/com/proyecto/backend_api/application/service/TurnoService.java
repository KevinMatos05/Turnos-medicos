package com.proyecto.backend_api.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.domain.dto.request.CrearTurnoRequest;
import com.proyecto.backend_api.domain.dto.response.TurnoResponse;
import com.proyecto.backend_api.domain.model.Turno;
import com.proyecto.backend_api.domain.repository.TurnoRepository;

@Service
public class TurnoService {
    @Autowired
    private TurnoRepository turnoRepository;

    public TurnoResponse crearTurno(CrearTurnoRequest request) {
        Turno turno = new Turno();

        Turno savedTurno = turnoRepository.save(turno);
        return new TurnoResponse(savedTurno);
    }

    public List<TurnoResponse> obtenerTurnos() {
        List<Turno> turnos = turnoRepository.findAll();
        return turnos.stream().map(TurnoResponse::new).toList();
    }

    public Optional<TurnoResponse> obtenerTurnosPorId(Long id) {
        return turnoRepository.findById(id).map(TurnoResponse::new);
    }

    public void cancelarTurno(Long id) {
        turnoRepository.deleteById(id);
    }

    public List<TurnoResponse> listarTurnos() {
        return obtenerTurnos();
    }

    public TurnoResponse obtenerTurnos(Long id) {
        return obtenerTurnosPorId(id)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado0"));
    }
}
