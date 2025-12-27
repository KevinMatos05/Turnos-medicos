package com.proyecto.backend_api.domain.dto.response;

import com.proyecto.backend_api.domain.model.DiaBloqueo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaBloqueoResponse {
    private Long id;
    private Long medicoId;
    private String medicoNombre;
    private LocalDate fecha;
    private String motivo;
    private Boolean activo;

    public DiaBloqueoResponse(DiaBloqueo diaBloqueo) {
        this.id = diaBloqueo.getId();
        this.medicoId = diaBloqueo.getMedico().getId();
        this.medicoNombre = diaBloqueo.getMedico().getNombreCompleto();
        this.fecha = diaBloqueo.getFecha();
        this.motivo = diaBloqueo.getMotivo();
        this.activo = diaBloqueo.getActivo();
    }
}
