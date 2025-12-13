package com.proyecto.backend_api.repository;


import com.proyecto.backend_api.domain.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long>{
    // crear automaticamente el SQL basado en el nombre del metodo
    boolean existsByMedicoIdAndFechaHora(Long medicoId, LocalDateTime fechaHora);

    // Buscar Turno por Apellido del Medico
    List<Turno> findAllMedicoApelldioContainingIgnoreCase(String apellido);

    // Buscar por Rango de Fechas 
    List<Turno> findallByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}
