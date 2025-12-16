package com.proyecto.backend_api.domain.model;

import java.time.LocalDate;

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

public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "documento", unique = true)
    private String documento;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "obra_social")
    private String obraSocial;

    @Column(name = "numero_afiliado")
    private String numeroAfiliado;

    @OneToMany(mappedBy = "paciente")
    @Builder.Default    
    private List<Turno> turnos = new ArrayList<>();

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = false;

    @Transient
    public String getNombreCompleto() {
        return usuario != null ? usuario.getNombre() + " " + usuario.getApellido() : "";
    }


}
