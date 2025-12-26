package com.proyecto.backend_api.domain.dto.response;

import com.proyecto.backend_api.domain.enums.Rol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private Rol rol;
    private Boolean activo;
    private String token;
}
