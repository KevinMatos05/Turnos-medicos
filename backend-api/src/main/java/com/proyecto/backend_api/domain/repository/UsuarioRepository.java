package com.proyecto.backend_api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.backend_api.domain.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Usuario findByEmail(String email);
}
