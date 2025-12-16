package com.proyecto.backend_api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.backend_api.domain.model.HorarioLaboral;

public interface HorarioLaboralRepository extends JpaRepository<HorarioLaboral, Long> {
    
}
