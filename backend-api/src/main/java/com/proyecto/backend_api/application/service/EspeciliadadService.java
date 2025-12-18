package com.proyecto.backend_api.application.service;

import java.util.stream.Collectors;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.backend_api.domain.dto.response.EspecialidadResponse;
import com.proyecto.backend_api.domain.model.Especialidad;
import com.proyecto.backend_api.domain.repository.EspecilidadRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class EspeciliadadService {
    private final EspecilidadRepository especilidadRepository;
    
    @Autowired
    public EspeciliadadService(EspecilidadRepository especilidadRepository)  {
        this.especilidadRepository = especilidadRepository;
    }

    @Transactional(readOnly = true)
    public List<EspecialidadResponse> obtenerTodos() {
        return especilidadRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    private EspecialidadResponse mapToResponse(Especialidad especialidad) {
        return EspecialidadResponse.builder()
            .id(especialidad.getId())
            .nombre(especialidad.getNombre())
            .descripcion(especialidad.getDescripcion())
            .build();
    }

}
