package com.proyecto.backend_api.domain.dto.request;

import lombok.Data;

@Data
public class RegistroUsuarioRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
}
