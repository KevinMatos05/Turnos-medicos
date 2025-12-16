package com.proyecto.backend_api.domain.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadResponse {
    private LocalDateTime fecha;
    private List<LocalDateTime> horasDisponibles;
}
