package com.proyecto.backend_api.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.proyecto.backend_api.domain.enums.TipoNotificacion;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "turno_id")
    private Turno turno;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo",nullable = false)
    private TipoNotificacion tipo;

    @Column(nullable = false)
    private String asunto;

    @Column(nullable = false)
    private String mensaje;

    @Column(name = "email_enviado")
    @Builder.Default
    private Boolean emailEnviado = false;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha:creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "leida")
    @Builder.Default
    private Boolean leida = false;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (leida == null) {
            leida = false;
        }
        if (emailEnviado == null) {
            emailEnviado = false;
        }
    }



}
