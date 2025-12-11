package com.proyecto.backend_api.repository;


import com.proyecto.backend_api.domain.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;


@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long>{
    // crear automaticamente el SQL basado en el nombre del metodo
    boolean existeByMedicoYFechaHora(Long medicoId, LocalDateTime fechaHora);
}
