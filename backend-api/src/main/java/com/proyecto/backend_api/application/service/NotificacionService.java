package com.proyecto.backend_api.application.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.domain.enums.TipoNotificacion;
import com.proyecto.backend_api.domain.model.Notificacion;
import com.proyecto.backend_api.domain.model.Turno;
import com.proyecto.backend_api.domain.repository.NotificacionRepository;

import jakarta.transaction.Transactional;

@Service
public class NotificacionService {
    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private final NotificacionRepository notificacionRepository;
    private final EmailService emailService;

    @Autowired
    public NotificacionService(NotificacionRepository notificacionRepository, EmailService emailService) {
        this.notificacionRepository = notificacionRepository;
        this.emailService = emailService;
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

    @Transactional
    public void enviarNotificacionCancelacion(Turno turno) {

        String asunto = "Cancelacion de Turno Medico";
        String mensaje = construirMensajeCancelacion(turno);

        Notificacion notificacion = Notificacion.builder()
            .usuario(turno.getPaciente().getUsuario())
            .tipo(TipoNotificacion.CANCELACION_TURNO)
            .asunto(asunto)
            .mensaje(mensaje)
            .build();

        notificacion = notificacionRepository.save(notificacion);

        try {
            enviarEmailNotificacion(notificacion);
            notificacion.setEmailEnviado(true);
            notificacion.setFechaEnvio(LocalDateTime.now());
            notificacionRepository.save(notificacion);
        } catch (Exception e) {
            // TODO: handle exception
        }
        }

        private void enviarRecordatorio(Turno turno) {
        String asunto = "Recordatorio de Turno Médico";
        String mensaje = construirMensajeRecordatorio(turno);
        
        Notificacion notificacion = Notificacion.builder()
            .usuario(turno.getPaciente().getUsuario())
            .tipo(TipoNotificacion.RECORDATORIO_TURNO)
            .asunto(asunto)
            .mensaje(mensaje)
            .emailEnviado(false)
            .leida(false)
            .build();
        
        notificacion = notificacionRepository.save(notificacion);
        
        try {
            enviarEmailNotificacion(notificacion);
            notificacion.setEmailEnviado(true);
            notificacion.setFechaEnvio(LocalDateTime.now());
            notificacionRepository.save(notificacion);
        } catch (Exception e) {
            logger.error("Error al enviar recordatorio para turno ID: {}", turno.getId(), e);
            throw new RuntimeException("Error al enviar recordatorio", e);
        }
    }
    
    /**
     * Envía el email correspondiente a una notificación
     * @param notificacion la notificación a enviar
     */
    private void enviarEmailNotificacion(Notificacion notificacion) {
        String emailDestino = notificacion.getUsuario().getEmail();
        emailService.sendEmail(emailDestino, notificacion.getAsunto(), notificacion.getMensaje());
    }
    
    /**
     * Construye el mensaje de confirmación de turno
     */
    private String construirMensajeConfirmacion(Turno turno) {
        return String.format(
            "Estimado/a %s,\n\n" +
            "Su turno ha sido confirmado con los siguientes detalles:\n\n" +
            "Médico: Dr/a. %s %s\n" +
            "Especialidad: %s\n" +
            "Fecha y hora: %s\n" +
            "Tipo de consulta: %s\n" +
            "Sucursal: %s\n\n" +
            "Por favor, llegue 10 minutos antes de su turno.\n\n" +
            "Saludos cordiales,\n" +
            "Sistema de Turnos Médicos",
            turno.getPaciente().getUsuario().getNombre() + " " + turno.getPaciente().getUsuario().getApellido(),
            turno.getMedico().getUsuario().getNombre(),
            turno.getMedico().getUsuario().getApellido(),
            turno.getMedico().getEspecialidad().getNombre(),
            turno.getFechaHora().format(DATE_FORMATTER),
            turno.getTipoConsulta(),
            turno.getSucursal() != null ? turno.getSucursal().getNombre() : "Por definir"
        );
    }
    
    /**
     * Construye el mensaje de cancelación de turno
     */
    private String construirMensajeCancelacion(Turno turno) {
        String motivoCancelacion = turno.getMotivoCancelacion() != null ? 
            "\nMotivo: " + turno.getMotivoCancelacion() : "";
            
        return String.format(
            "Estimado/a %s,\n\n" +
            "Lamentamos informarle que su turno ha sido cancelado:\n\n" +
            "Médico: Dr/a. %s %s\n" +
            "Especialidad: %s\n" +
            "Fecha y hora original: %s\n" +
            "%s\n\n" +
            "Por favor, comuníquese con nosotros para reagendar su turno.\n\n" +
            "Saludos cordiales,\n" +
            "Sistema de Turnos Médicos",
            turno.getPaciente().getUsuario().getNombre() + " " + turno.getPaciente().getUsuario().getApellido(),
            turno.getMedico().getUsuario().getNombre(),
            turno.getMedico().getUsuario().getApellido(),
            turno.getMedico().getEspecialidad().getNombre(),
            turno.getFechaHora().format(DATE_FORMATTER),
            motivoCancelacion
        );
    }
    
    /**
     * Construye el mensaje de recordatorio de turno
     */
    private String construirMensajeRecordatorio(Turno turno) {
        return String.format(
            "Estimado/a %s,\n\n" +
            "Le recordamos que tiene un turno próximo:\n\n" +
            "Médico: Dr/a. %s %s\n" +
            "Especialidad: %s\n" +
            "Fecha y hora: %s\n" +
            "Tipo de consulta: %s\n" +
            "Sucursal: %s\n\n" +
            "Por favor, recuerde llegar 10 minutos antes de su turno.\n" +
            "Si necesita cancelar o reprogramar, por favor comuníquese con anticipación.\n\n" +
            "Saludos cordiales,\n" +
            "Sistema de Turnos Médicos",
            turno.getPaciente().getUsuario().getNombre() + " " + turno.getPaciente().getUsuario().getApellido(),
            turno.getMedico().getUsuario().getNombre(),
            turno.getMedico().getUsuario().getApellido(),
            turno.getMedico().getEspecialidad().getNombre(),
            turno.getFechaHora().format(DATE_FORMATTER),
            turno.getTipoConsulta(),
            turno.getSucursal() != null ? turno.getSucursal().getNombre() : "Por definir"
        );
    }


}
