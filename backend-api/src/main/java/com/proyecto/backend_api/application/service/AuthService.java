package com.proyecto.backend_api.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.domain.dto.request.LoginRequest;
import com.proyecto.backend_api.domain.dto.request.RegistroUsuarioRequest;
import com.proyecto.backend_api.domain.dto.response.UsuarioResponse;
import com.proyecto.backend_api.domain.model.Usuario;
import com.proyecto.backend_api.domain.repository.UsuarioRepository;
import com.proyecto.backend_api.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public UsuarioResponse login(LoginRequest loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());
        if (usuarioOpt.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), usuarioOpt.get().getPassword())){
            Usuario usuario = usuarioOpt.get();
            String token = jwtTokenProvider.generateToken(usuario.getEmail());
            return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .token(token)
                .build();
        }
        throw new RuntimeException("Credenciales inválidas");   
    }

    public void register (Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
    }

    public UsuarioResponse register(RegistroUsuarioRequest request) {
        if(usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El usuario ya existe");

        }
        Usuario usuario = Usuario.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .telefono(request.getTelefono())
            .rol(request.getRol())
            .activo(true)
            .build();
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
}
