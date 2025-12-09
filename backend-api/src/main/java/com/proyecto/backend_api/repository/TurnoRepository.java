package com.proyecto.backend_api.repository;


import com.proyecto.backend_api.domain.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long>{

}
