package com.proyecto.backend_api.application.usecase;

import org.springframework.stereotype.Service;

import com.proyecto.backend_api.application.service.NotificacionService;
import com.proyecto.backend_api.domain.enums.EstadoTurno;
import com.proyecto.backend_api.domain.model.Turno;
import com.proyecto.backend_api.domain.repository.TurnoRepository;

@Service
public class CancelarTurnoUseCase {
    private final TurnoRepository turnoRepository;
    private final NotificacionService notificacionService;

    public CancelarTurnoUseCase(TurnoRepository turnoRepository, NotificacionService notificacionService) {
        this.turnoRepository = turnoRepository;
        this.notificacionService = notificacionService;
    }
    public void cancelarTurno(Long turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        turno.setEstado(EstadoTurno.CANCELADO_POR_PACIENTE);
        turnoRepository.save(turno);
        notificacionService.enviarNotificacionCancelacion(turno);
    }

}
