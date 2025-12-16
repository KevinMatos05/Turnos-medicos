package com.proyecto.backend_api.domain.dto.request;

import com.proyecto.backend_api.domain.enums.EstadoTurno;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarEstadoTurnoRequest {
    @NotNull(message = "El estado es obligatorio")
    private EstadoTurno estado;

    private String motivoCancelacion;

    private String observaciones;
}
