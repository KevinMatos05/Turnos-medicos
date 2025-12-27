package com.proyecto.backend_api.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.domain.dto.request.RegistroUsuarioRequest;
import com.proyecto.backend_api.domain.dto.response.UsuarioResponse;
import com.proyecto.backend_api.domain.model.Paciente;
import com.proyecto.backend_api.domain.model.Usuario;
import com.proyecto.backend_api.domain.repository.PacienteRepository;
import com.proyecto.backend_api.domain.repository.UsuarioRepository;

@Service
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public PacienteService(PacienteRepository pacienteRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Paciente> obtenerTodosLosPacientes() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> obtenerPacientePorId(Long id) {
        return pacienteRepository.findById(id);
    }

    public UsuarioResponse obtenerPacientePorIdResponse(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        Usuario usuario = paciente.getUsuario();
        return UsuarioResponse.builder()
            .id(usuario.getId())
            .nombre(usuario.getNombre())
            .apellido(usuario.getApellido())
            .email(usuario.getEmail())
            .telefono(usuario.getTelefono())
            .rol(usuario.getRol())
            .activo(usuario.getActivo())
            .build();
    }

    public Paciente registarPaciente (Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public UsuarioResponse registrarPaciente(RegistroUsuarioRequest request) {
        Usuario usuario = Usuario.builder()
            .email(request.getEmail())
            .password(request.getPassword())
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .rol(request.getRol())
            .activo(true)
            .build();
        usuario = usuarioRepository.save(usuario);

        Paciente paciente = new Paciente();
        paciente.setUsuario(usuario);
        paciente.setDocumento(request.getDocumento());
        paciente.setDireccion(request.getDireccion());
        paciente.setObraSocial(request.getObraSocial());
        paciente.setNumeroAfiliado(request.getNumeroAfiliado());
        pacienteRepository.save(paciente);

        return UsuarioResponse.builder()
            .id(usuario.getId())
            .nombre(usuario.getNombre())
            .apellido(usuario.getApellido())
            .email(usuario.getApellido())
            .telefono(usuario.getTelefono())
            .rol(usuario.getRol())
            .build();
    }

    public Paciente actualizarPaciente(Long id, Paciente pacienteActualizado) {
        pacienteActualizado.setId(id);
        return pacienteRepository.save(pacienteActualizado);
    }

    public UsuarioResponse actualizarPaciente (Long id, RegistroUsuarioRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Usuario usuario = paciente.getUsuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setTelefono(request.getApellido());
        if (request.getPassword()!= null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        usuarioRepository.save(usuario);

        paciente.setDocumento(request.getDocumento());
        paciente.setDireccion(request.getDireccion());
        paciente.setObraSocial(request.getObraSocial());
        paciente.setNumeroAfiliado(request.getNumeroAfiliado());
        pacienteRepository.save(paciente);

        return UsuarioResponse.builder()
            .id(usuario.getId())
            .nombre(usuario.getNombre())
            .apellido(usuario.getApellido())
            .email(usuario.getEmail())
            .telefono(usuario.getTelefono())
            .rol(usuario.getRol())
            .activo(usuario.getActivo())
            .build();
    }

    public void eliminarPaciente(Long id) {
        pacienteRepository.deleteById(id);
    }

    public Paciente obtenerPacienteById(Long id) {
        return pacienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));
    }

}
