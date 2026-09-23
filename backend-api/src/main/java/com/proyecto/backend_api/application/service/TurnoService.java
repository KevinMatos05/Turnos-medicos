package com.proyecto.backend_api.application.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.application.usecase.CancelarTurnoUseCase;
import com.proyecto.backend_api.application.usecase.ConfirmarTurnoUseCase;
import com.proyecto.backend_api.domain.dto.request.CrearTurnoRequest;
import com.proyecto.backend_api.domain.dto.response.TurnoResponse;
import com.proyecto.backend_api.domain.enums.EstadoTurno;
import com.proyecto.backend_api.domain.model.Paciente;
import com.proyecto.backend_api.domain.model.Turno;
import com.proyecto.backend_api.domain.model.Medico;
import com.proyecto.backend_api.domain.repository.TurnoRepository;
import com.proyecto.backend_api.domain.model.Sucursal;
import com.proyecto.backend_api.infrastructure.exception.BusinessException;
import com.proyecto.backend_api.infrastructure.exception.ResourceNotFoundException;
import com.proyecto.backend_api.domain.repository.PacienteRepository;
import com.proyecto.backend_api.domain.repository.MedicoRepository;
import com.proyecto.backend_api.domain.repository.SucursalRepository;

@Service
public class TurnoService {
    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired 
    private PacienteRepository pacienteRepository;
    
    @Autowired 
    private MedicoRepository medicoRepository;

    @Autowired 
    private SucursalRepository sucursalRepository;

    @Autowired
    private CancelarTurnoUseCase cancelarTurnoUseCase;
    
    @Autowired
    private ConfirmarTurnoUseCase confirmarTurnoUseCase;

    public TurnoResponse crearTurno(CrearTurnoRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId()).orElseThrow(()-> new ResourceNotFoundException("No existe un paciente con id " + request.getPacienteId()));

        Medico medico = medicoRepository.findById(request.getMedicoId()).orElseThrow(() -> new ResourceNotFoundException("No existe medico con id " + request.getMedicoId()));

        if (!Boolean.TRUE.equals(medico.getActivo())) {
            throw new BusinessException("El medico seleccionado no eestá activo");
        }

        List <Turno> conflictos = turnoRepository.findTurnosActivosEnHorario(medico.getId(), request.getFechaHora());

        if (!conflictos.isEmpty()) {
            throw new BusinessException("El médico ya tiene un turno agendado en ese horario");

        }

        Turno.TurnoBuilder turnoBuilder = Turno.builder()
            .paciente(paciente)
            .medico(medico)
            .fechaHora(request.getFechaHora())
            .tipoConsulta(request.getTipoConsulta())
            .observaciones(request.getObservaciones())
            .estado(EstadoTurno.PENDIENTE);

        if (request.getSucursalId() != null) {
            Sucursal sucursal = sucursalRepository.findById(request.getSucursalId()).orElseThrow(()-> new ResourceNotFoundException("No existe una sucursal con ese id " + request.getSucursalId()));
        } else if (medico.getSucursal() != null) {
            turnoBuilder.sucursal(medico.getSucursal());
        } 

        Turno savedTurno = turnoRepository.save(turnoBuilder.build());

        return new TurnoResponse(savedTurno);

 
    } 

    public List<TurnoResponse> obtenerTurnos() {
        List<Turno> turnos = turnoRepository.findAll();
        return turnos.stream().map(TurnoResponse::new).toList();
    }

    public Optional<TurnoResponse> obtenerTurnosPorId(Long id) {
        return turnoRepository.findById(id).map(TurnoResponse::new);
    }

    public void cancelarTurno(Long id) {
        cancelarTurnoUseCase.cancelarTurno(id);
    }

    public List<TurnoResponse> listarTurnos() {
        return obtenerTurnos();
    }

    public TurnoResponse obtenerTurnos(Long id) {
        return obtenerTurnosPorId(id)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado0"));
    }

    public List<TurnoResponse> obtenerTurnosPorPaciente(Paciente paciente) {
        List<Turno> turnos = turnoRepository.findByPacienteOrderByFechaHoraDesc(paciente);
        return turnos.stream()
            .map(TurnoResponse::new)
            .toList();
    }
    
    public TurnoResponse confirmarTurno(Long id) {
        Turno turno = confirmarTurnoUseCase.confirmarTurno(id);
        return new TurnoResponse(turno);
    }
    
    public void marcarAsistencia(Long id, String observaciones) {
        confirmarTurnoUseCase.marcarAsistencia(id, observaciones);
    }
    
    public void marcarInasistencia(Long id) {
        confirmarTurnoUseCase.marcarInasistencia(id);
    }
}
