package com.proyecto.backend_api.domain.dto.request;

import java.time.LocalDateTime;

import com.proyecto.backend_api.domain.enums.TipoConsulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearTurnoRequest {
    @NotNull(message="El ID del paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "El ID del medico es obligatorio")
    private Long medicoId;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime fechaHora;

    @NotNull(message = "El tipo de consulta es obligatorio")
    private TipoConsulta tipoConsulta;

    private String observaciones;

    private Long sucursalId;
}
