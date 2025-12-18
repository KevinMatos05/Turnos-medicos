package com.proyecto.backend_api.domain.dto.request;

import jakarta.validation.constraints.Size;

import com.proyecto.backend_api.domain.enums.Rol;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegistroUsuarioRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    private String telefono;
    
    
    @NotNull(message = "el rol es obligatorio")
    private Rol rol;
    // Campos para paciente
    private String documento;
    private String direccion;
    private String obraSocial;
    private String numeroAfiliado;

    // Campos para medico
    private String matricula;
    private Long especialidadId;
    private Long sucursalId;

}
