package com.proyecto.backend_api.domain.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CrearTurnosRequest {
    private Long pacienteId;
    private Long medicoId;
    private LocalDateTime fechaHora;
    private String motivo;
}
