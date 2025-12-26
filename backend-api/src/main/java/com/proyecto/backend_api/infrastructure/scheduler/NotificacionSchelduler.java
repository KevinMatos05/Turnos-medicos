package com.proyecto.backend_api.infrastructure.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.proyecto.backend_api.application.service.NotificacionService;

@Component
public class NotificacionSchelduler {
    @Autowired
    private NotificacionService notificacionService;

    @Scheduled(cron = "0 0 9 * * ?")
    public void enviarRecordatorios() {
        notificacionService.enviarRecordatoriosTurnos();
    }
}
