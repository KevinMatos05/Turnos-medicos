package com.proyecto.backend_api.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteResponse {
    private Long id;
    private String nombreCompleto;
    private String documento;
    private String telefono;
    private String email;
    private String direccion;
    private String obraSocial;
    private String numeroAfiliado;
    private Boolean activo;
}
