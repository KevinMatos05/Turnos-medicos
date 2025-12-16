package com.proyecto.backend_api.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "medico")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable=false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    @Column(name="matricula", nullable = false, unique = true)
    private String matricula;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "duracion_turno_minutos")
    @Builder.Default
    private Integer duracionTurnoMinutos = 30;

    @OneToMany(mappedBy = "medioc", cascade =CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HorarioLaboral> horariosLaborales = new ArrayList<>();   
    
    @OneToMany(mappedBy = "medico")
    @Builder.Default
    private List<Turno> turnos = new ArrayList<>();

    @Transient
    public String getNombreCompleto() {
        return usuario != null ? usuario.getNombre() + " " + usuario.getApellido() : "";

    }
}
