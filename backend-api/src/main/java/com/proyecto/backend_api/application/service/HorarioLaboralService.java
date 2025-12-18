package com.proyecto.backend_api.application.service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.domain.dto.request.ActualizarHorarioRequest;
import com.proyecto.backend_api.domain.dto.response.HorarioLaboralResponse;
import com.proyecto.backend_api.domain.model.HorarioLaboral;
import com.proyecto.backend_api.domain.repository.HorarioLaboralRepository;

@Service
public class HorarioLaboralService {
    private final HorarioLaboralRepository horarioLaboralRepository;

    @Autowired
    public HorarioLaboralService(HorarioLaboralRepository horarioLaboralRepository) {
        this.horarioLaboralRepository = horarioLaboralRepository;
    }

    public List<HorarioLaboral> obtenerHorariosPorMedico(Long medicoId) {
        return horarioLaboralRepository.findByMedicoId(medicoId);
    }

    public HorarioLaboral crearHorario(HorarioLaboral horarioLaboral) {
        return horarioLaboralRepository.save(horarioLaboral);
    }

    public void eliminarHorario(Long id) {
        horarioLaboralRepository.deleteById(id);
    }

    public List<HorarioLaboral> obtenerHorariosPorDia(Long medicoId, DayOfWeek diaSemana) {
        return horarioLaboralRepository.findByMedicoAndDiaSemana(medicoId, diaSemana);
    }

    public List<HorarioLaboralResponse> obtenerTodosLosHorarios() {
        return horarioLaboralRepository.findAll()
            .stream()
            .map(h -> HorarioLaboralResponse.builder()
                .id(h.getId())
                .diaSemana(h.getDiaSemana())
                .horaInicio(h.getHoraInicio())
                .horaFin(h.getHoraFin())
                .build())
            .collect(Collectors.toList());
    } 

    public HorarioLaboral actualizarHorario(Long id, HorarioLaboral horarioActualizado) {
        horarioActualizado.setId(id);
        return horarioLaboralRepository.save(horarioActualizado);
    }

    public HorarioLaboral acutHorarioLaboral(Long id, ActualizarHorarioRequest request) {
        HorarioLaboral horario = horarioLaboralRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        
        if (request.getDiaSemana() != null) {
            horario.setDiaSemana(DayOfWeek.valueOf(request.getDiaSemana()));
        }
        if (request.getHoraInicio() != null) {
            horario.setHoraInicio(request.getHoraInicio());
        }
        if (request.getHoraFin() != null)  {
            horario.setHoraFin(request.getHoraFin());
        }

        return horarioLaboralRepository.save(horario);
    }
}
