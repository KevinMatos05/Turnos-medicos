package com.proyecto.backend_api.infrastructure.controller;

import com.proyecto.backend_api.application.service.AuthService;
import com.proyecto.backend_api.domain.dto.request.LoginRequest;
import com.proyecto.backend_api.domain.dto.request.RegistroUsuarioRequest;
import com.proyecto.backend_api.domain.dto.response.UsuarioResponse;
import com.proyecto.backend_api.domain.dto.response.MedicoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro y login de usuarios")
public class AuthController {

    private final AuthService authService;
    private final com.proyecto.backend_api.application.service.PacienteService pacienteService;
    private final com.proyecto.backend_api.application.service.MedicoService medicoService;

    public AuthController(AuthService authService, 
                         com.proyecto.backend_api.application.service.PacienteService pacienteService,
                         com.proyecto.backend_api.application.service.MedicoService medicoService) {
        this.authService = authService;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
    }

    @PostMapping("/register/paciente")
    @Operation(summary = "Registrar nuevo paciente",
            description = "Crea un nuevo paciente en el sistema con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente registrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioResponse> registerPaciente(@RequestBody RegistroUsuarioRequest request) {
        UsuarioResponse response = pacienteService.registrarPaciente(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/medico")
    @Operation(summary = "Registrar nuevo médico",
            description = "Crea un nuevo médico en el sistema con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico registrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<MedicoResponse> registerMedico(@RequestBody RegistroUsuarioRequest request) {
        MedicoResponse response = medicoService.registrarMedico(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario",
            description = "Genera un token JWT válido para acceder a endpoints protegidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioResponse> login(@RequestBody LoginRequest request) {
        UsuarioResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}