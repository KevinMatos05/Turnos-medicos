package com.proyecto.backend_api.domain.services;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.domain.dto.TurnoSolicitudDTO;
import com.proyecto.backend_api.domain.dto.response.TurnoRespuestaDTO;
import com.proyecto.backend_api.domain.enums.EstadoTurno;
import com.proyecto.backend_api.domain.model.Medico;
import com.proyecto.backend_api.domain.model.Paciente;
import com.proyecto.backend_api.domain.model.Turno;
import com.proyecto.backend_api.domain.repository.MedicoRepository;
import com.proyecto.backend_api.domain.repository.PacienteRepository;
import com.proyecto.backend_api.domain.repository.TurnoRepository;



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
        if (turnoRepository.existsByMedicoIdAndFechaHora(datos.MedicoId(),datos.fechaHora())) {
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
            return convertirADTO(turnoRepository.save(turno));
    }

    // Lerr Todos (Busqueda Inteligente)
    public List<TurnoRespuestaDTO> listarTurnos(LocalDate fecha, String ApellidoMedico) {
        List<Turno> turnos;
        if (fecha != null) {
            turnos = turnoRepository.findAllByFechaHoraBetween(fecha.atStartOfDay(), fecha.atTime(23,59,59));
        } else if (ApellidoMedico != null) {
            turnos = turnoRepository.findAllByMedicoApellidoContainingIgnoreCase(ApellidoMedico);
        } else {
            turnos = turnoRepository.findAll();
        }
        return turnos.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    // Leer Uno (Por ID)
    public TurnoRespuestaDTO obtenerPorId(Long id) {
        Turno turno = turnoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        return convertirADTO(turno); 
    }

    // Actualiazr Turno (Edutar Fecha o Estado)
    public TurnoRespuestaDTO editarTurno(Long id, TurnoSolicitudDTO datosModificados) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
  
        if (datosModificados.fechaHora() != null) {
            turno.setFechaHora(datosModificados.fechaHora());
            if (datosModificados.motivoDeConsulta() != null) {
                turno.setMotivoConsulta(datosModificados.motivoDeConsulta());
            }
        }
        return convertirADTO(turnoRepository.save(turno));
    }


    // Eliminar Turno
    public void eliminarTurno(Long id) {
        if (!turnoRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, el Turno no existe");
        }
        turnoRepository.deleteById(id);
    }

    // Metodo Auxiliar
    private TurnoRespuestaDTO convertirADTO (Turno turno) {
        return new TurnoRespuestaDTO(
            turno.getId(),
            turno.getMedico().getNombre() + " " + turno.getMedico().getApellido(),
            turno.getPaciente().getNombre() + " " + turno.getPaciente().getApellido(),
            turno.getFechaHora(),
            turno.getEstado().toString()
        );
    }
}
