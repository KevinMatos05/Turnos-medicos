package com.proyecto.backend_api.infrastructure.controller;

import com.proyecto.backend_api.application.service.EspeciliadadService;
import com.proyecto.backend_api.application.service.MedicoService;
import com.proyecto.backend_api.application.service.SucursalService;
import com.proyecto.backend_api.domain.dto.request.ActualizarEspecialidadRequest;
import com.proyecto.backend_api.domain.dto.request.CrearSucursalRequest;
import com.proyecto.backend_api.domain.dto.request.RegistroUsuarioRequest;
import com.proyecto.backend_api.domain.dto.response.EspecialidadResponse;
import com.proyecto.backend_api.domain.dto.response.MedicoResponse;
import com.proyecto.backend_api.domain.dto.response.SucursalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administración", description = "Endpoints para administración del sistema")
@SecurityRequirement(name = "bearer-auth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final MedicoService medicoService;
    private final EspeciliadadService especialidadService;
    private final SucursalService sucursalService;

    public AdminController(MedicoService medicoService, 
                          EspeciliadadService especialidadService,
                          SucursalService sucursalService) {
        this.medicoService = medicoService;
        this.especialidadService = especialidadService;
        this.sucursalService = sucursalService;
    }

    // ========== GESTIÓN DE MÉDICOS ==========
    
    @PostMapping("/medicos")
    @Operation(summary = "Crear médico", description = "Crea un nuevo médico en el sistema")
    public ResponseEntity<MedicoResponse> crearMedico(@Valid @RequestBody RegistroUsuarioRequest request) {
        MedicoResponse response = medicoService.registrarMedico(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/medicos")
    @Operation(summary = "Listar médicos", description = "Obtiene todos los médicos del sistema")
    public ResponseEntity<List<MedicoResponse>> listarMedicos() {
        List<MedicoResponse> medicos = medicoService.obtenerMedicos();
        return ResponseEntity.ok(medicos);
    }

    @PutMapping("/medicos/{id}")
    @Operation(summary = "Actualizar médico", description = "Actualiza los datos de un médico")
    public ResponseEntity<MedicoResponse> actualizarMedico(
            @PathVariable Long id,
            @Valid @RequestBody RegistroUsuarioRequest request) {
        MedicoResponse response = medicoService.actualizarMedico(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/medicos/{id}")
    @Operation(summary = "Eliminar médico", description = "Elimina un médico del sistema")
    public ResponseEntity<Void> eliminarMedico(@PathVariable Long id) {
        medicoService.eliminarMedico(id);
        return ResponseEntity.noContent().build();
    }

    // ========== GESTIÓN DE ESPECIALIDADES ==========

    @PostMapping("/especialidades")
    @Operation(summary = "Crear especialidad", description = "Crea una nueva especialidad")
    public ResponseEntity<EspecialidadResponse> crearEspecialidad(
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion) {
        EspecialidadResponse response = especialidadService.crear(nombre, descripcion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/especialidades")
    @Operation(summary = "Listar especialidades", description = "Obtiene todas las especialidades")
    public ResponseEntity<List<EspecialidadResponse>> listarEspecialidades() {
        List<EspecialidadResponse> especialidades = especialidadService.obtenerTodos();
        return ResponseEntity.ok(especialidades);
    }

    @PutMapping("/especialidades/{id}")
    @Operation(summary = "Actualizar especialidad", description = "Actualiza una especialidad existente")
    public ResponseEntity<EspecialidadResponse> actualizarEspecialidad(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEspecialidadRequest request) {
        EspecialidadResponse response = especialidadService.actualizar(
            id, request.getNombre(), request.getDescripcion(), request.getActivo());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/especialidades/{id}")
    @Operation(summary = "Eliminar especialidad", description = "Elimina una especialidad")
    public ResponseEntity<Void> eliminarEspecialidad(@PathVariable Long id) {
        especialidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ========== GESTIÓN DE SUCURSALES ==========

    @PostMapping("/sucursales")
    @Operation(summary = "Crear sucursal", description = "Crea una nueva sucursal")
    public ResponseEntity<SucursalResponse> crearSucursal(@Valid @RequestBody CrearSucursalRequest request) {
        SucursalResponse response = sucursalService.crear(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sucursales")
    @Operation(summary = "Listar sucursales", description = "Obtiene todas las sucursales")
    public ResponseEntity<List<SucursalResponse>> listarSucursales() {
        List<SucursalResponse> sucursales = sucursalService.obtenerTodas();
        return ResponseEntity.ok(sucursales);
    }

    @GetMapping("/sucursales/{id}")
    @Operation(summary = "Obtener sucursal", description = "Obtiene una sucursal por ID")
    public ResponseEntity<SucursalResponse> obtenerSucursal(@PathVariable Long id) {
        SucursalResponse response = sucursalService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/sucursales/{id}")
    @Operation(summary = "Actualizar sucursal", description = "Actualiza una sucursal existente")
    public ResponseEntity<SucursalResponse> actualizarSucursal(
            @PathVariable Long id,
            @Valid @RequestBody CrearSucursalRequest request) {
        SucursalResponse response = sucursalService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/sucursales/{id}")
    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable Long id) {
        sucursalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
