package com.proyecto.backend_api.dto;

import java.time.LocalDateTime;

public record  TurnoSolicitudDTO(
    Long MedicoId,
    Long PacienteId,
    LocalDateTime fechaHora,
    String motivoDeConsulta
) {}