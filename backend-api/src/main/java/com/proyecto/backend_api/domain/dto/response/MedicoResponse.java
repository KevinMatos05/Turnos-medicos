package com.proyecto.backend_api.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoResponse {
    private Long id;
    private String nombreCompleto;
    private String matricula;
    private EspecialidadResponse especialidad;
    private SucursalResponse sucursal;
    private Integer duracionTurnoMinutos;
    private List<HorarioLaboralResponse> horariosLaborales;
    private Boolean activo;
}
