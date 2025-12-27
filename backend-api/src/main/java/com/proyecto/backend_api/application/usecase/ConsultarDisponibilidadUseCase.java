package com.proyecto.backend_api.application.usecase;

import com.proyecto.backend_api.application.service.DiaBloqueoService;
import com.proyecto.backend_api.domain.dto.response.DisponibilidadResponse;
import com.proyecto.backend_api.domain.enums.EstadoTurno;
import com.proyecto.backend_api.domain.model.HorarioLaboral;
import com.proyecto.backend_api.domain.model.Medico;
import com.proyecto.backend_api.domain.repository.HorarioLaboralRepository;
import com.proyecto.backend_api.domain.repository.MedicoRepository;
import com.proyecto.backend_api.domain.repository.TurnoRepository;
import com.proyecto.backend_api.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultarDisponibilidadUseCase {
    
    private final HorarioLaboralRepository horarioLaboralRepository;
    private final TurnoRepository turnoRepository;
    private final MedicoRepository medicoRepository;
    private final DiaBloqueoService diaBloqueoService;

    public ConsultarDisponibilidadUseCase(HorarioLaboralRepository horarioLaboralRepository,
                                         TurnoRepository turnoRepository,
                                         MedicoRepository medicoRepository,
                                         DiaBloqueoService diaBloqueoService) {
        this.horarioLaboralRepository = horarioLaboralRepository;
        this.turnoRepository = turnoRepository;
        this.medicoRepository = medicoRepository;
        this.diaBloqueoService = diaBloqueoService;
    }

    public List<DisponibilidadResponse> consultarDisponibilidad(Long medicoId, LocalDate fecha) {
        Medico medico = medicoRepository.findById(medicoId)
            .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado"));

        // Verificar si el día está bloqueado
        if (diaBloqueoService.estaBloqueado(medicoId, fecha)) {
            return new ArrayList<>();
        }

        DayOfWeek diaSemana = fecha.getDayOfWeek();
        
        // Buscar horarios laborales del médico para ese día
        List<HorarioLaboral> horariosLaborales = horarioLaboralRepository
            .findByMedicoAndDiaSemana(medico, diaSemana);

        if (horariosLaborales.isEmpty()) {
            return new ArrayList<>();
        }

        List<DisponibilidadResponse> disponibilidades = new ArrayList<>();
        Integer duracionTurno = medico.getDuracionTurnoMinutos();

        for (HorarioLaboral horario : horariosLaborales) {
            LocalTime horaActual = horario.getHoraInicio();
            
            while (horaActual.plusMinutes(duracionTurno).isBefore(horario.getHoraFin()) ||
                   horaActual.plusMinutes(duracionTurno).equals(horario.getHoraFin())) {
                
                LocalDateTime fechaHoraConsulta = LocalDateTime.of(fecha, horaActual);
                
                // Verificar si ya existe un turno en ese horario
                boolean ocupado = turnoRepository
                    .findTurnosActivosEnHorario(medicoId, fechaHoraConsulta)
                    .stream()
                    .anyMatch(t -> t.getEstado() == EstadoTurno.PENDIENTE || 
                                   t.getEstado() == EstadoTurno.CONFIRMADO);

                DisponibilidadResponse disponibilidad = DisponibilidadResponse.builder()
                    .medicoId(medicoId)
                    .medicoNombre(medico.getNombreCompleto())
                    .fecha(fecha)
                    .horaInicio(horaActual)
                    .horaFin(horaActual.plusMinutes(duracionTurno))
                    .disponible(!ocupado)
                    .build();

                disponibilidades.add(disponibilidad);
                horaActual = horaActual.plusMinutes(duracionTurno);
            }
        }

        return disponibilidades;
    }

    public List<DisponibilidadResponse> consultarDisponibilidadRango(Long medicoId, 
                                                                     LocalDate fechaInicio, 
                                                                     LocalDate fechaFin) {
        List<DisponibilidadResponse> todasDisponibilidades = new ArrayList<>();
        
        LocalDate fechaActual = fechaInicio;
        while (!fechaActual.isAfter(fechaFin)) {
            todasDisponibilidades.addAll(consultarDisponibilidad(medicoId, fechaActual));
            fechaActual = fechaActual.plusDays(1);
        }
        
        return todasDisponibilidades;
    }
}
