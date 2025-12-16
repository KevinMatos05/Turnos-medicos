package com.proyecto.backend_api.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoRespuestaDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String especialidad;
    private String telefono;
}