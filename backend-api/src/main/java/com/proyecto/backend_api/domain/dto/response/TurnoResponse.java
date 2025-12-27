package com.proyecto.backend_api.domain.dto.response;

import java.time.LocalDateTime;

import com.proyecto.backend_api.domain.enums.EstadoTurno;
import com.proyecto.backend_api.domain.enums.TipoConsulta;
import com.proyecto.backend_api.domain.model.Turno;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoResponse {
    private Long id;
    private PacienteResponse paciente;
    private MedicoResponse medico;
    private LocalDateTime fechaHora;
    private EstadoTurno estado;
    private TipoConsulta tipoConsulta;
    private String observaciones;
    private String motivoCancelacion;
    private SucursalResponse sucursal;
    private LocalDateTime fechaCreacion;

    public TurnoResponse (Turno turno) {
        this.id = turno.getId();
        this.fechaHora = turno.getFechaHora();
        this.estado = turno.getEstado();
        this.tipoConsulta = turno.getTipoConsulta();
        this.observaciones = turno.getObservaciones();
        this.motivoCancelacion = turno.getMotivoCancelacion();
        this.fechaCreacion = turno.getFechaCreacion();
        
        if (turno.getPaciente() != null) {
            this.paciente = PacienteResponse.builder()
                .id(turno.getPaciente().getId())
                .nombreCompleto(turno.getPaciente().getUsuario().getNombre() + " " + turno.getPaciente().getUsuario().getApellido())
                .documento(turno.getPaciente().getDocumento())
                .email(turno.getPaciente().getUsuario().getEmail())
                .telefono(turno.getPaciente().getUsuario().getTelefono())
                .build();
        }

        if (turno.getMedico() != null) {
            this.medico = MedicoResponse.builder()
                .id(turno.getMedico().getId())
                .nombreCompleto(turno.getMedico().getUsuario().getNombre() + " " + turno.getMedico().getUsuario().getApellido())
                .matricula(turno.getMedico().getMatricula())
                .activo(turno.getMedico().getActivo())
                .build();
        }

        if (turno.getSucursal()!= null) {
            this.sucursal = SucursalResponse.builder()
                .id(turno.getSucursal().getId())
                .nombre(turno.getSucursal().getNombre())
                .direccion(turno.getSucursal().getDireccion())
                .ciudad(turno.getSucursal().getCiudad())
                .provincia(turno.getSucursal().getProvincia())
                .telefono(turno.getSucursal().getTelefono())
                .activo(turno.getSucursal().getActivo())
                .build();
        }

    }

}
