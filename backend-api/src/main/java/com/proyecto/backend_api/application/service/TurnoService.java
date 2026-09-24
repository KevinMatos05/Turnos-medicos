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
import com.proyecto.backend_api.domain.enums.Rol;
import com.proyecto.backend_api.domain.model.Paciente;
import com.proyecto.backend_api.domain.model.Turno;
import com.proyecto.backend_api.domain.model.Usuario;
import com.proyecto.backend_api.domain.model.Medico;
import com.proyecto.backend_api.domain.repository.TurnoRepository;
import com.proyecto.backend_api.domain.model.Sucursal;
import com.proyecto.backend_api.infrastructure.exception.BusinessException;
import com.proyecto.backend_api.infrastructure.exception.ResourceNotFoundException;
import com.proyecto.backend_api.infrastructure.exception.UnauthorizedException;
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
            turnoBuilder.sucursal(sucursal);
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

    public void cancelarTurno(Long id, Usuario usuarioActual) {
        Turno turno = turnoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Turno no encontrado"));
        verificarAcceso(turno,usuarioActual);
        if (usuarioActual.getRol() == Rol.MEDICO) {
            cancelarTurnoUseCase.cancelarTurnoPorMedico(id, "Cancelado por el médico");
        } else {
            cancelarTurnoUseCase.cancelarTurnoPorPaciente(id, "Cancelado por paciente");
        }

    }

    private void verificarAcceso(Turno turno, Usuario usuarioActual) {
        if (usuarioActual.getRol() == Rol.ADMIN) {
            return; 
        }

        if (usuarioActual.getRol() == Rol.PACIENTE && turno.getPaciente() != null && turno.getPaciente().getUsuario().getId().equals(usuarioActual.getId())) {
            return;
        }

        if (usuarioActual.getRol() == Rol.MEDICO && turno.getMedico() != null && turno.getMedico().getUsuario().getId().equals(usuarioActual.getId())) {
            return ;
        }

        throw new UnauthorizedException("No tenes permiso para acceder a este turno");

    }

    public List<TurnoResponse> listarTurnos(Usuario usuarioActual) {
        List<Turno> turnos = switch (usuarioActual.getRol()) {
            case ADMIN -> turnoRepository.findAll();
            case PACIENTE -> {
                Paciente paciente = pacienteRepository.findByUsuario(usuarioActual).orElseThrow(()-> new ResourceNotFoundException("No se encontró el paciente asociado a este usuario"));
                yield turnoRepository.findByPacienteOrderByFechaHoraDesc(paciente);
            }
            case MEDICO -> {
                Medico medico = medicoRepository.findByUsuario(usuarioActual).orElseThrow(()->new ResourceNotFoundException("No se encontró el médico asociado a este usuario"));
                yield turnoRepository.findByMedicoOrderByFechaHoraDesc(medico);
            }
        };
        return turnos.stream().map(TurnoResponse::new).toList();

    }

    public TurnoResponse obtenerTurnos(Long id, Usuario usuarioActual) {
        Turno turno = turnoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Turno no encontrado"));

        verificarAcceso(turno, usuarioActual);
        return new TurnoResponse(turno);
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
