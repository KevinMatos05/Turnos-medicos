package com.proyecto.backend_api.domain.repository;

import com.proyecto.backend_api.domain.model.DiaBloqueo;
import com.proyecto.backend_api.domain.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaBloqueoRepository extends JpaRepository<DiaBloqueo, Long> {
    List<DiaBloqueo> findByMedicoAndActivoTrue(Medico medico);
    
    List<DiaBloqueo> findByMedicoAndFechaBetweenAndActivoTrue(
        Medico medico, 
        LocalDate fechaInicio, 
        LocalDate fechaFin
    );
    
    @Query("SELECT d FROM DiaBloqueo d WHERE d.medico.id = :medicoId AND d.fecha = :fecha AND d.activo = true")
    Optional<DiaBloqueo> findByMedicoIdAndFechaAndActivoTrue(
        @Param("medicoId") Long medicoId, 
        @Param("fecha") LocalDate fecha
    );
    
    boolean existsByMedicoAndFechaAndActivoTrue(Medico medico, LocalDate fecha);
}
