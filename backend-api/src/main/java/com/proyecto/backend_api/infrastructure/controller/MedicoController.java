package com.proyecto.backend_api.infrastructure.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.backend_api.application.service.DiaBloqueoService;
import com.proyecto.backend_api.application.service.MedicoService;
import com.proyecto.backend_api.domain.dto.request.BloquearDiaRequest;
import com.proyecto.backend_api.domain.dto.request.RegistroUsuarioRequest;
import com.proyecto.backend_api.domain.dto.response.DiaBloqueoResponse;
import com.proyecto.backend_api.domain.dto.response.MedicoResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medicos")
@Tag(name = "Médicos", description = "Endpoints para gestión de médicos")
public class MedicoController {
    private final MedicoService medicoService;
    private final DiaBloqueoService diaBloqueoService;

    @Autowired
    public MedicoController(MedicoService medicoService, DiaBloqueoService diaBloqueoService) {
        this.medicoService = medicoService;
        this.diaBloqueoService = diaBloqueoService;
    }

    @PostMapping
    public ResponseEntity<MedicoResponse> registrarMedico(@RequestBody RegistroUsuarioRequest request) {
        MedicoResponse medicoResponse = medicoService.registrarMedico(request);
        return ResponseEntity.ok(medicoResponse);
    }

    @GetMapping
    @Operation(summary = "Obtener médicos", description = "Obtiene lista de médicos, opcionalmente filtrados por especialidad")
    public ResponseEntity<List<MedicoResponse>> obtenerMedicos(
            @RequestParam(required = false) Long especialidad) {
        List<MedicoResponse> medicos;
        if (especialidad != null) {
            medicos = medicoService.obtenerMedicosPorEspecialidad(especialidad);
        } else {
            medicos = medicoService.obtenerMedicos();
        }
        return ResponseEntity.ok(medicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponse> obtenerMedicoPorId(@PathVariable Long id) {
        MedicoResponse medicoResponse = medicoService.obtenerMedicoPorId(id);
        return ResponseEntity.ok(medicoResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponse> actualizarMedico(@PathVariable Long id, @RequestBody RegistroUsuarioRequest request) {
        MedicoResponse medicoResponse = medicoService.actualizarMedico(id, request);
        return ResponseEntity.ok(medicoResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedico(@PathVariable Long id) {
        medicoService.eliminarMedico(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/bloquear-dia")
    @Operation(summary = "Bloquear día", description = "Bloquea un día específico para un médico")
    public ResponseEntity<DiaBloqueoResponse> bloquearDia(
            @PathVariable Long id,
            @Valid @RequestBody BloquearDiaRequest request) {
        request.setMedicoId(id);
        DiaBloqueoResponse response = diaBloqueoService.bloquearDia(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}/dias-bloqueados")
    @Operation(summary = "Obtener días bloqueados", description = "Obtiene los días bloqueados de un médico")
    public ResponseEntity<List<DiaBloqueoResponse>> obtenerDiasBloqueados(@PathVariable Long id) {
        List<DiaBloqueoResponse> bloqueos = diaBloqueoService.obtenerBloqueosPorMedico(id);
        return ResponseEntity.ok(bloqueos);
    }
    
    @DeleteMapping("/bloqueos/{bloqueoId}")
    @Operation(summary = "Desbloquear día", description = "Desbloquea un día previamente bloqueado")
    public ResponseEntity<Void> desbloquearDia(@PathVariable Long bloqueoId) {
        diaBloqueoService.desbloquearDia(bloqueoId);
        return ResponseEntity.noContent().build();
    }
}
