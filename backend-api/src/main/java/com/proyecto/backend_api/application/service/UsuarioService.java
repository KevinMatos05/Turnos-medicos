package com.proyecto.backend_api.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.domain.dto.request.RegistroUsuarioRequest;
import com.proyecto.backend_api.domain.dto.response.UsuarioResponse;
import com.proyecto.backend_api.domain.model.Usuario;
import com.proyecto.backend_api.domain.repository.UsuarioRepository;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponse registrarUsuario(RegistroUsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getPassword());
        usuario = usuarioRepository.save(usuario);
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

    public Optional<UsuarioResponse> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
            .map(usuario -> UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .build());
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
