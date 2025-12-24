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
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.backend_api.application.service.MedicoService;
import com.proyecto.backend_api.domain.dto.request.RegistroUsuarioRequest;
import com.proyecto.backend_api.domain.dto.response.MedicoResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {
    private final MedicoService medicoService;

    @Autowired
    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public ResponseEntity<MedicoResponse> registrarMedico(@RequestBody RegistroUsuarioRequest request) {
        MedicoResponse medicoResponse = medicoService.registrarMedico(request);
        return ResponseEntity.ok(medicoResponse);
    }

    @GetMapping
    public ResponseEntity<List<MedicoResponse>> obtenerMedicos() {
        List<MedicoResponse> medicos = medicoService.obtenerMedicos();
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


}
