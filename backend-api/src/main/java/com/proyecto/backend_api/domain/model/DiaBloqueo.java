package com.proyecto.backend_api.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "dia_bloqueo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaBloqueo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 500)
    private String motivo;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;
}
