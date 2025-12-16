package com.proyecto.backend_api.domain.repository;

import com.proyecto.backend_api.domain.model.Paciente;
import com.proyecto.backend_api.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByDocumento(String documento);
    Optional<Paciente> findByUsuario(Usuario usuario);

    List<Paciente> findByActivoTrue();

    Boolean existsByDocumento(String documento);
}
