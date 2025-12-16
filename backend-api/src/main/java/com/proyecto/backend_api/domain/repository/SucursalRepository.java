package com.proyecto.backend_api.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.backend_api.domain.model.Sucursal;

public interface SucursalRepository extends JpaRepository<Sucursal, Long>{
    List<Sucursal> findByActivaTrue();
    List<Sucursal> findByCiudad(String ciudad);
}
