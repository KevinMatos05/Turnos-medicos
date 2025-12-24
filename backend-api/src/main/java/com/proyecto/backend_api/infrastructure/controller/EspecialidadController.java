package com.proyecto.backend_api.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.backend_api.application.service.EspeciliadadService;
import com.proyecto.backend_api.domain.dto.response.EspecialidadResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EspecialidadController {
    private final EspeciliadadService especiliadadService;
    @GetMapping
    public ResponseEntity<List<EspecialidadResponse>> obtenerTodas() {
        return ResponseEntity.ok(especiliadadService.obtenerTodos());  
    }
}
