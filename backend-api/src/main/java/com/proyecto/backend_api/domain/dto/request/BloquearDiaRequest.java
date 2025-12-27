package com.proyecto.backend_api.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloquearDiaRequest {
    
    @NotNull(message = "El ID del médico es obligatorio")
    private Long medicoId;
    
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
    
    private String motivo;
}
