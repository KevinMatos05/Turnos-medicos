package com.proyecto.backend_api.infrastructure.controller;

import com.proyecto.backend_api.application.service.AuthService;
import com.proyecto.backend_api.domain.dto.request.LoginRequest;
import com.proyecto.backend_api.domain.dto.request.RegistroUsuarioRequest;
import com.proyecto.backend_api.domain.dto.response.UsuarioResponse;
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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario",
            description = "Crea un nuevo usuario en el sistema con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioResponse> register(@RequestBody RegistroUsuarioRequest request) {
        UsuarioResponse response = authService.register(request);
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