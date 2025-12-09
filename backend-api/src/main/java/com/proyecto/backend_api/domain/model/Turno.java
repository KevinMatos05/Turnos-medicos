package com.proyecto.backend_api.domain.model;


import com.proyecto.backend_api.domain.enums.EstadoTurno;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fechaHora;    

    @Enumerated(EnumType.STRING)
    private EstadoTurno estado;

    @ManyToOne
    private Medico medico;

    @ManyToOne
    private Paciente paciente;

}
