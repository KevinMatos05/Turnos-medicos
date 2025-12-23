package com.proyecto.backend_api.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.application.service.NotificacionService;
import com.proyecto.backend_api.domain.enums.EstadoTurno;
import com.proyecto.backend_api.domain.model.Turno;
import com.proyecto.backend_api.domain.repository.TurnoRepository;

@Service
public class ConfirmarTurnoUseCase {
    private final TurnoRepository turnoRepository;
    private final NotificacionService notificacionService;

    @Autowired
    public ConfirmarTurnoUseCase(TurnoRepository turnoRepository, NotificacionService notificacionService) {
        this.turnoRepository = turnoRepository;
        this.notificacionService = notificacionService;
    }

    public Turno confirmarTurno(Long turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        turno.setEstado(EstadoTurno.CONFIRMADO);
        Turno turnoConfirmado = turnoRepository.save(turno);
        
        notificacionService.enviarNotificacion(turnoConfirmado);

        return turnoConfirmado;
        
    }

    }


