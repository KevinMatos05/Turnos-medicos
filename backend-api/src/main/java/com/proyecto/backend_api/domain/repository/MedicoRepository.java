package com.proyecto.backend_api.domain.repository;

import com.proyecto.backend_api.domain.model.Especialidad;
import com.proyecto.backend_api.domain.model.Medico;
import com.proyecto.backend_api.domain.model.Sucursal;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MedicoRepository extends JpaRepository <Medico, Long> {
    List<Medico> findByActivoTrue();

    List<Medico> findByEspecialidadAndActivoTrue(Especialidad especialidad);

    List<Medico> findBySucursalAndActivoTrue(Sucursal sucursal);

    Optional<Medico> findByMatricula(String matricula);

    @Query("SELECT m FROM Medico m WHERE m.especialidad.id = :especialidadId AND m.activo = true")
    List<Medico> findByEspecialidadId(@Param("especiliadadId") Long especialidadId);

    @Query("SELECT m FROM Medico m JOIN m.horariosLaborales h WHERE h.diaSeamana = :diaSemana AND m.activo = true")
    List<Medico> findByDiaDisponible(@Param("diaSemana") DayOfWeek diaSemana);
    
}