package com.proyecto.backend_api.domain.repository;

import com.proyecto.backend_api.domain.model.Auditoria;
import com.proyecto.backend_api.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByUsuarioOrderByFechaHoraDesc(Usuario usuario);
    
    List<Auditoria> findByEntidadAndEntidadIdOrderByFechaHoraDesc(String entidad, Long entidadId);
    
    List<Auditoria> findByFechaHoraBetweenOrderByFechaHoraDesc(
        LocalDateTime fechaInicio, 
        LocalDateTime fechaFin
    );
    
    List<Auditoria> findByAccionContainingIgnoreCaseOrderByFechaHoraDesc(String accion);
}
