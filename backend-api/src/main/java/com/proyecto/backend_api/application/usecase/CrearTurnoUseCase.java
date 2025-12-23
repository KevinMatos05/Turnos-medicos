package com.proyecto.backend_api.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.application.service.TurnoService;
import com.proyecto.backend_api.domain.dto.request.CrearTurnoRequest;
import com.proyecto.backend_api.domain.dto.response.TurnoResponse;

@Service
public class CrearTurnoUseCase {
    private final TurnoService turnoService;

    @Autowired
    public CrearTurnoUseCase(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    public TurnoResponse crearTurno(CrearTurnoRequest request) {
        return turnoService.crearTurno(request);
    }
}
