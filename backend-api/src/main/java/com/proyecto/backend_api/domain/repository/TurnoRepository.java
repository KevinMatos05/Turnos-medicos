package com.proyecto.backend_api.domain.repository;

import com.proyecto.backend_api.domain.enums.EstadoTurno;
import com.proyecto.backend_api.domain.model.Medico;
import com.proyecto.backend_api.domain.model.Paciente;
import com.proyecto.backend_api.domain.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {
    List<Turno> findByPacienteOrderByFechaHoraDesc(Paciente paciente);
    
    List<Turno> findByMedicoAndFechaHoraBetweenOrderByFechaHora(
        Medico medico, 
        LocalDateTime fechaInicio, 
        LocalDateTime fechaFin
    );
    
    List<Turno> findByPacienteAndEstadoIn(Paciente paciente, List<EstadoTurno> estados);
    
    @Query("SELECT t FROM Turno t WHERE t.medico = :medico AND t.fechaHora BETWEEN :inicio AND :fin AND t.estado NOT IN :estadosExcluidos")
    List<Turno> findTurnosDisponibles(
        @Param("medico") Medico medico,
        @Param("inicio") LocalDateTime inicio,
        @Param("fin") LocalDateTime fin,
        @Param("estadosExcluidos") List<EstadoTurno> estadosExcluidos
    );
    
    @Query("SELECT t FROM Turno t WHERE t.medico.id = :medicoId AND t.fechaHora = :fechaHora AND t.estado IN ('PENDIENTE', 'CONFIRMADO')")
    List<Turno> findTurnosActivosEnHorario(
        @Param("medicoId") Long medicoId,
        @Param("fechaHora") LocalDateTime fechaHora
    );
    
    @Query("SELECT t FROM Turno t WHERE t.estado = 'CONFIRMADO' AND t.fechaHora BETWEEN :inicio AND :fin")
    List<Turno> findTurnosParaRecordatorio(
        @Param("inicio") LocalDateTime inicio,
        @Param("fin") LocalDateTime fin
    );
    
    @Query("SELECT COUNT(t) FROM Turno t WHERE t.medico = :medico AND t.estado = :estado AND t.fechaHora BETWEEN :inicio AND :fin")
    Long countByMedicoAndEstadoAndFechaHoraBetween(
        @Param("medico") Medico medico,
        @Param("estado") EstadoTurno estado,
        @Param("inicio") LocalDateTime inicio,
        @Param("fin") LocalDateTime fin
    );
}