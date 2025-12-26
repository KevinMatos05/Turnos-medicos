package com.proyecto.backend_api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.backend_api.domain.model.Especialidad;
import org.springframework.stereotype.Repository;



@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long>{
    
}
