package com.proyecto.backend_api.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.domain.model.Notificacion;
import com.proyecto.backend_api.domain.repository.NotificacionRepository;

@Service
public class NotificacionService {
    private final NotificacionRepository notificacionRepository;

    @Autowired
    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public Notificacion crearNotificacion(Notificacion notificacion){
        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> obtenerNotificaciones() {
        return notificacionRepository.findAll();
    }

    public List<Notificacion> obtenerTodasLasNotificaciones() {
        return obtenerNotificaciones();
    }

    public void eliminarNotificacion(Long id){
        notificacionRepository.deleteById(id);
    }

    public Notificacion obtenerNotificacionPorId(Long id) {
        return notificacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notificacion no encontrada"));
    }


}
