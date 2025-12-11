package com.proyecto.backend_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.dto.TurnoSolicitudDTO;
import com.proyecto.backend_api.repository.MedicoRepository;
import com.proyecto.backend_api.repository.PacienteRepository;
import com.proyecto.backend_api.repository.TurnoRepository;
import com.proyecto.backend_api.domain.enums.EstadoTurno;
import com.proyecto.backend_api.domain.model.Medico;
import com.proyecto.backend_api.domain.model.Paciente;
import com.proyecto.backend_api.domain.model.Turno;
import com.proyecto.backend_api.dto.TurnoRespuestaDTO;

@Service
public class TurnoService {
    @Autowired
    private TurnoRepository turnoRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;

    public TurnoRespuestaDTO reservarTurno(TurnoSolicitudDTO datos) {
        // Validar que no exista Turno ya solicitado
        if (turnoRepository.existeByMedicoYFechaHora(datos.MedicoId(),datos.fechaHora())) {
            throw new RuntimeException("El medico ya tiene un turno reservado en esa fecha y hora");
        }

        // Buscar entidades
        Medico medico = medicoRepository.findById(datos.MedicoId())
            .orElseThrow(() -> new RuntimeException("Medico no encontrado"));
        Paciente paciente = pacienteRepository.findById(datos.PacienteId())
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        
            // Crear y guardar el turno
            Turno turno = Turno.builder()
                .medico(medico)
                .paciente(paciente)
                .fechaHora(datos.fechaHora())
                .motivoConsulta(datos.motivoDeConsulta())
                .estado(EstadoTurno.PENDIENTE)
                .build();
            turno = turnoRepository.save(turno);

        // convertir a DTO para devolver al front
        return new TurnoRespuestaDTO(
            turno.getId(),
            medico.getNombre() + " " + medico.getApellido(),
            paciente.getNombre() + " " + paciente.getApellido(),
            turno.getFechaHora(),
            turno.getEstado().toString()
        );
    }
}
