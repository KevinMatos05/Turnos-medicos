package com.proyecto.backend_api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.backend_api.domain.model.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
}
