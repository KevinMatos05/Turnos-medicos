package com.proyecto.backend_api.application.service;

import com.proyecto.backend_api.domain.model.Auditoria;
import com.proyecto.backend_api.domain.model.Usuario;
import com.proyecto.backend_api.domain.repository.AuditoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuditoriaService {
    
    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    public void registrarAuditoria(Usuario usuario, String accion, String entidad, Long entidadId, String detalles) {
        Auditoria auditoria = Auditoria.builder()
            .usuario(usuario)
            .accion(accion)
            .entidad(entidad)
            .entidadId(entidadId)
            .detalles(detalles)
            .build();
        
        auditoriaRepository.save(auditoria);
    }

    public void registrarAuditoria(Usuario usuario, String accion, String entidad, Long entidadId) {
        registrarAuditoria(usuario, accion, entidad, entidadId, null);
    }
}
