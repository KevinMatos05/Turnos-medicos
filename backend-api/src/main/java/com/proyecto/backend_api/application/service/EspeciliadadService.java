package com.proyecto.backend_api.application.service;

import java.util.stream.Collectors;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.backend_api.domain.dto.response.EspecialidadResponse;
import com.proyecto.backend_api.domain.model.Especialidad;
import com.proyecto.backend_api.domain.repository.EspecialidadRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class EspeciliadadService {
    private final EspecialidadRepository especilidadRepository;
    
    @Autowired
    public EspeciliadadService(EspecialidadRepository especilidadRepository)  {
        this.especilidadRepository = especilidadRepository;
    }

    @Transactional(readOnly = true)
    public List<EspecialidadResponse> obtenerTodos() {
        return especilidadRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public EspecialidadResponse crear(String nombre, String descripcion) {
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(nombre);
        especialidad.setDescripcion(descripcion);
        especialidad.setActivo(true);
        Especialidad saved = especilidadRepository.save(especialidad);
        return mapToResponse(saved);
    }
    
    @Transactional
    public EspecialidadResponse actualizar(Long id, String nombre, String descripcion, Boolean activo) {
        Especialidad especialidad = especilidadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        especialidad.setNombre(nombre);
        especialidad.setDescripcion(descripcion);
        especialidad.setActivo(activo);
        Especialidad saved = especilidadRepository.save(especialidad);
        return mapToResponse(saved);
    }
    
    @Transactional
    public void eliminar(Long id) {
        especilidadRepository.deleteById(id);
    }

    private EspecialidadResponse mapToResponse(Especialidad especialidad) {
        return EspecialidadResponse.builder()
            .id(especialidad.getId())
            .nombre(especialidad.getNombre())
            .descripcion(especialidad.getDescripcion())
            .build();
    }

}
