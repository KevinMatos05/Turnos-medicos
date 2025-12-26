package com.proyecto.backend_api.domain.repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.backend_api.domain.model.HorarioLaboral;
import com.proyecto.backend_api.domain.model.Medico;

public interface HorarioLaboralRepository extends JpaRepository<HorarioLaboral, Long> {
    List<HorarioLaboral> findByMedico(Medico medico);

    List<HorarioLaboral> findByMedicoAndDiaSemana(Medico medico, DayOfWeek diaSemana);

    @Query("SELECT h FROM HorarioLaboral h WHERE h.medico.id = :medicoId")
    List<HorarioLaboral> findByMedicoId(@Param("medicoId") Long medicoId);

    @Query("SELECT h FROM HorarioLaboral h WHERE h.medico.id = :medicoId AND h.diaSemana = :diaSemana")
    List<HorarioLaboral> findByMedicoAndDiaSemana(@Param("medicoId") Long medicoId, @Param("diaSemana") DayOfWeek diaSemana);

    @Query("SELECT h FROM HorarioLaboral h WHERE h.medico.id = :medicoId AND h.diaSemana = :diaSemana AND h.horaInicio <= :horaInicio AND h.horaFin >= :horaFin")
    List<HorarioLaboral> findByMedicoAndDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(@Param("medicoId") Long medicoId, @Param("diaSemana") DayOfWeek diaSemana, @Param("horaInicio") LocalTime horaInicio, @Param("horaFin") LocalTime horaFin);

    void deleteByMedico(Medico medico);
}
