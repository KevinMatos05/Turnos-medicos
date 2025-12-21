package com.proyecto.backend_api.application.usecase;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.backend_api.domain.model.HorarioLaboral;
import com.proyecto.backend_api.domain.repository.HorarioLaboralRepository;

@Service
public class BuscarDisponibilidadUseCase {
    
    private final HorarioLaboralRepository horarioLaboralRepository;

    public BuscarDisponibilidadUseCase(HorarioLaboralRepository horarioLaboralRepository) {
        this.horarioLaboralRepository = horarioLaboralRepository;
    }

    public List<HorarioLaboral> buscarDisponibilidad(Long medicoId, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        return horarioLaboralRepository.findByMedicoAndDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(medicoId, diaSemana, horaInicio, horaFin);
    }
}
