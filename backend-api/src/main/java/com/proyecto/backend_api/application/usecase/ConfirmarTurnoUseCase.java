package com.proyecto.backend_api.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.application.service.NotificacionService;
import com.proyecto.backend_api.domain.enums.EstadoTurno;
import com.proyecto.backend_api.domain.model.Turno;
import com.proyecto.backend_api.domain.repository.TurnoRepository;
import com.proyecto.backend_api.infrastructure.exception.BusinessException;

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
            .orElseThrow(() -> new BusinessException("Turno no encontrado"));
        
        // Validar que el turno esté en estado PENDIENTE
        if (turno.getEstado() != EstadoTurno.PENDIENTE) {
            throw new BusinessException("Solo se pueden confirmar turnos en estado PENDIENTE");
        }
        
        turno.setEstado(EstadoTurno.CONFIRMADO);
        Turno turnoConfirmado = turnoRepository.save(turno);
        
        notificacionService.enviarNotificacion(turnoConfirmado);

        return turnoConfirmado;
    }
    
    public void marcarAsistencia(Long turnoId, String observaciones) {
        Turno turno = turnoRepository.findById(turnoId)
            .orElseThrow(() -> new BusinessException("Turno no encontrado"));
        
        if (turno.getEstado() != EstadoTurno.CONFIRMADO && turno.getEstado() != EstadoTurno.PENDIENTE) {
            throw new BusinessException("No se puede marcar asistencia para este turno");
        }
        
        turno.setEstado(EstadoTurno.ASISTIDO);
        if (observaciones != null && !observaciones.isEmpty()) {
            turno.setObservaciones(observaciones);
        }
        turnoRepository.save(turno);
    }
    
    public void marcarInasistencia(Long turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
            .orElseThrow(() -> new BusinessException("Turno no encontrado"));
        
        if (turno.getEstado() != EstadoTurno.CONFIRMADO && turno.getEstado() != EstadoTurno.PENDIENTE) {
            throw new BusinessException("No se puede marcar inasistencia para este turno");
        }
        
        turno.setEstado(EstadoTurno.NO_ASISTIDO);
        turnoRepository.save(turno);
    }
}


