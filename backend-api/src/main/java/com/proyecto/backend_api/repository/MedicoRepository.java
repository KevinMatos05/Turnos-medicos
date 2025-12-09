package com.proyecto.backend_api.repository;

import com.proyecto.backend_api.domain.model.Medico;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MedicoRepository extends JpaRepository <Medico, Long> {
    Optional<Medico> findByMatricula(String matricula);
}
