package com.proyecto.backend_api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record  TurnoSolicitudDTO(
    @JsonProperty("medicoId")
    Long MedicoId,
    @JsonProperty("pacienteId")
    Long PacienteId,
    @JsonProperty("fechaHora")
    LocalDateTime fechaHora,
    @JsonProperty("motivoDeConsulta")
    String motivoDeConsulta
) {}