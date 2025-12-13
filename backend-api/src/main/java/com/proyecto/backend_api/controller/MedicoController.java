package com.proyecto.backend_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.backend_api.services.MedicoService;
import com.proyecto.backend_api.dto.MedicoRespuestaDTO;
import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {
    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public List<MedicoRespuestaDTO> listarMedicos() {
        return medicoService.getAll();
    }
}
