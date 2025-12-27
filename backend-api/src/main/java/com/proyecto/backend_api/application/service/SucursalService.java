package com.proyecto.backend_api.application.service;

import com.proyecto.backend_api.domain.dto.request.CrearSucursalRequest;
import com.proyecto.backend_api.domain.dto.response.SucursalResponse;
import com.proyecto.backend_api.domain.model.Sucursal;
import com.proyecto.backend_api.domain.repository.SucursalRepository;
import com.proyecto.backend_api.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SucursalService {
    
    private final SucursalRepository sucursalRepository;

    public SucursalService(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    @Transactional(readOnly = true)
    public List<SucursalResponse> obtenerTodas() {
        return sucursalRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SucursalResponse obtenerPorId(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));
        return mapToResponse(sucursal);
    }

    public SucursalResponse crear(CrearSucursalRequest request) {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(request.getNombre());
        sucursal.setDireccion(request.getDireccion());
        sucursal.setTelefono(request.getTelefono());
        sucursal.setEmail(request.getEmail());
        sucursal.setActivo(request.getActivo() != null ? request.getActivo() : true);
        
        Sucursal saved = sucursalRepository.save(sucursal);
        return mapToResponse(saved);
    }

    public SucursalResponse actualizar(Long id, CrearSucursalRequest request) {
        Sucursal sucursal = sucursalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));
        
        sucursal.setNombre(request.getNombre());
        sucursal.setDireccion(request.getDireccion());
        sucursal.setTelefono(request.getTelefono());
        sucursal.setEmail(request.getEmail());
        if (request.getActivo() != null) {
            sucursal.setActivo(request.getActivo());
        }
        
        Sucursal saved = sucursalRepository.save(sucursal);
        return mapToResponse(saved);
    }

    public void eliminar(Long id) {
        if (!sucursalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sucursal no encontrada");
        }
        sucursalRepository.deleteById(id);
    }

    private SucursalResponse mapToResponse(Sucursal sucursal) {
        return SucursalResponse.builder()
            .id(sucursal.getId())
            .nombre(sucursal.getNombre())
            .direccion(sucursal.getDireccion())
            .telefono(sucursal.getTelefono())
            .activo(sucursal.getActivo())
            .build();
    }
}
