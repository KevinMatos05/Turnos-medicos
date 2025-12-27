package com.proyecto.backend_api.application.usecase;

import org.springframework.stereotype.Service;

import com.proyecto.backend_api.application.service.NotificacionService;
import com.proyecto.backend_api.domain.enums.EstadoTurno;
import com.proyecto.backend_api.domain.model.Turno;
import com.proyecto.backend_api.domain.repository.TurnoRepository;
import com.proyecto.backend_api.infrastructure.exception.BusinessException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class CancelarTurnoUseCase {
    private final TurnoRepository turnoRepository;
    private final NotificacionService notificacionService;
    private static final int HORAS_MINIMAS_CANCELACION = 24;

    public CancelarTurnoUseCase(TurnoRepository turnoRepository, NotificacionService notificacionService) {
        this.turnoRepository = turnoRepository;
        this.notificacionService = notificacionService;
    }
    
    public void cancelarTurnoPorPaciente(Long turnoId, String motivo) {
        Turno turno = turnoRepository.findById(turnoId)
            .orElseThrow(() -> new BusinessException("Turno no encontrado"));
        
        // Validar que el turno esté en estado cancelable
        if (turno.getEstado() == EstadoTurno.CANCELADO_POR_PACIENTE || 
            turno.getEstado() == EstadoTurno.CANCELADO_POR_MEDICO) {
            throw new BusinessException("El turno ya fue cancelado");
        }
        
        if (turno.getEstado() == EstadoTurno.ASISTIDO || turno.getEstado() == EstadoTurno.NO_ASISTIDO) {
            throw new BusinessException("No se puede cancelar un turno finalizado");
        }
        
        // Validar horas mínimas de anticipación
        long horasAntes = ChronoUnit.HOURS.between(LocalDateTime.now(), turno.getFechaHora());
        if (horasAntes < HORAS_MINIMAS_CANCELACION) {
            throw new BusinessException(
                String.format("No se puede cancelar con menos de %d horas de anticipación. Faltan %d horas.", 
                    HORAS_MINIMAS_CANCELACION, horasAntes)
            );
        }
        
        turno.setEstado(EstadoTurno.CANCELADO_POR_PACIENTE);
        turno.setMotivoCancelacion(motivo);
        turnoRepository.save(turno);
        notificacionService.enviarNotificacionCancelacion(turno);
    }
    
    public void cancelarTurnoPorMedico(Long turnoId, String motivo) {
        Turno turno = turnoRepository.findById(turnoId)
            .orElseThrow(() -> new BusinessException("Turno no encontrado"));
        
        if (turno.getEstado() == EstadoTurno.ASISTIDO || turno.getEstado() == EstadoTurno.NO_ASISTIDO) {
            throw new BusinessException("No se puede cancelar un turno finalizado");
        }
        
        turno.setEstado(EstadoTurno.CANCELADO_POR_MEDICO);
        turno.setMotivoCancelacion(motivo);
        turnoRepository.save(turno);
        notificacionService.enviarNotificacionCancelacion(turno);
    }
    
    public void cancelarTurno(Long turnoId) {
        cancelarTurnoPorPaciente(turnoId, "Cancelación sin motivo especificado");
    }
}
