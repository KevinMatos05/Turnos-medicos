package com.proyecto.backend_api.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SucursalResponse {
    private Long id;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String provincia;
    private String telefono;
    private Boolean activo;
}
