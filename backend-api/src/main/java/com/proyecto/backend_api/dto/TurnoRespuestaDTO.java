package com.proyecto.backend_api.dto;

import java.time.LocalDateTime;

public record TurnoRespuestaDTO(
    Long id,
    String medicoNombre,
    String pacienteNombre,
    LocalDateTime fechaHora,
    String estado
) {}
