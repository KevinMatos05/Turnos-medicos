package com.proyecto.backend_api.application.service;

import com.proyecto.backend_api.domain.dto.request.BloquearDiaRequest;
import com.proyecto.backend_api.domain.dto.response.DiaBloqueoResponse;
import com.proyecto.backend_api.domain.model.DiaBloqueo;
import com.proyecto.backend_api.domain.model.Medico;
import com.proyecto.backend_api.domain.repository.DiaBloqueoRepository;
import com.proyecto.backend_api.domain.repository.MedicoRepository;
import com.proyecto.backend_api.infrastructure.exception.BusinessException;
import com.proyecto.backend_api.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiaBloqueoService {
    
    private final DiaBloqueoRepository diaBloqueoRepository;
    private final MedicoRepository medicoRepository;

    public DiaBloqueoService(DiaBloqueoRepository diaBloqueoRepository, MedicoRepository medicoRepository) {
        this.diaBloqueoRepository = diaBloqueoRepository;
        this.medicoRepository = medicoRepository;
    }

    public DiaBloqueoResponse bloquearDia(BloquearDiaRequest request) {
        Medico medico = medicoRepository.findById(request.getMedicoId())
            .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado"));

        // Validar que la fecha sea futura
        if (request.getFecha().isBefore(LocalDate.now())) {
            throw new BusinessException("No se puede bloquear una fecha pasada");
        }

        // Verificar si ya existe un bloqueo activo para esa fecha
        if (diaBloqueoRepository.existsByMedicoAndFechaAndActivoTrue(medico, request.getFecha())) {
            throw new BusinessException("Ya existe un bloqueo activo para esta fecha");
        }

        DiaBloqueo diaBloqueo = DiaBloqueo.builder()
            .medico(medico)
            .fecha(request.getFecha())
            .motivo(request.getMotivo())
            .activo(true)
            .build();

        DiaBloqueo saved = diaBloqueoRepository.save(diaBloqueo);
        return new DiaBloqueoResponse(saved);
    }

    public List<DiaBloqueoResponse> obtenerBloqueosPorMedico(Long medicoId) {
        Medico medico = medicoRepository.findById(medicoId)
            .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado"));

        return diaBloqueoRepository.findByMedicoAndActivoTrue(medico)
            .stream()
            .map(DiaBloqueoResponse::new)
            .collect(Collectors.toList());
    }

    public List<DiaBloqueoResponse> obtenerBloqueosPorMedicoYRango(
            Long medicoId, LocalDate fechaInicio, LocalDate fechaFin) {
        Medico medico = medicoRepository.findById(medicoId)
            .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado"));

        return diaBloqueoRepository.findByMedicoAndFechaBetweenAndActivoTrue(medico, fechaInicio, fechaFin)
            .stream()
            .map(DiaBloqueoResponse::new)
            .collect(Collectors.toList());
    }

    public void desbloquearDia(Long bloqueoId) {
        DiaBloqueo diaBloqueo = diaBloqueoRepository.findById(bloqueoId)
            .orElseThrow(() -> new ResourceNotFoundException("Bloqueo no encontrado"));

        diaBloqueo.setActivo(false);
        diaBloqueoRepository.save(diaBloqueo);
    }

    public boolean estaBloqueado(Long medicoId, LocalDate fecha) {
        return diaBloqueoRepository.findByMedicoIdAndFechaAndActivoTrue(medicoId, fecha).isPresent();
    }
}
