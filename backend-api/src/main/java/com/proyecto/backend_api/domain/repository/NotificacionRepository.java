package com.proyecto.backend_api.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.backend_api.domain.enums.TipoNotificacion;
import com.proyecto.backend_api.domain.model.Notificacion;
import com.proyecto.backend_api.domain.model.Turno;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
    List<Notificacion> findByTurnoAndTipo(Turno turno, TipoNotificacion tipo);
}
