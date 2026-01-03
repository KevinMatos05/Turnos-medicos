package com.proyecto.backend_api.application.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.backend_api.domain.dto.request.RegistroUsuarioRequest;
import com.proyecto.backend_api.domain.dto.response.EspecialidadResponse;
import com.proyecto.backend_api.domain.dto.response.MedicoResponse;
import com.proyecto.backend_api.domain.dto.response.SucursalResponse;
import com.proyecto.backend_api.domain.model.Medico;
import com.proyecto.backend_api.domain.model.Usuario;
import com.proyecto.backend_api.domain.repository.EspecialidadRepository;
import com.proyecto.backend_api.domain.repository.MedicoRepository;
import com.proyecto.backend_api.domain.repository.SucursalRepository;
import com.proyecto.backend_api.domain.repository.UsuarioRepository;

@Service
public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspecialidadRepository especilidadRepository;
    private final SucursalRepository sucursalRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MedicoService(MedicoRepository medicoRepository, UsuarioRepository usuarioRepository, EspecialidadRepository especilidadRepository, SucursalRepository sucursalRepository, PasswordEncoder passwordEncoder) {
        this.medicoRepository = medicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.especilidadRepository = especilidadRepository;
        this.sucursalRepository = sucursalRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<Medico> findAll(){
        return medicoRepository.findAll();
    } 

    public Optional<Medico> findById(Long id) {
        return medicoRepository.findById(id);
    }

    public Medico save(Medico medico){
        return medicoRepository.save(medico);
    }

    public void deleteById(Long id) {
        medicoRepository.deleteById(id);
    }

    public MedicoResponse registrarMedico(RegistroUsuarioRequest request) {
        Medico medico = save(convertirRequestAMedico(request));
        return convertirMedicoAResponse(medico);
    }

    @Transactional(readOnly = true)
    public List<MedicoResponse> obtenerMedicos() {
        return findAll().stream()
            .map(this::convertirMedicoAResponse)
            .collect(Collectors.toList());        
    }
    
    @Transactional(readOnly = true)
    public List<MedicoResponse> obtenerMedicosPorEspecialidad(Long especialidadId) {
        return medicoRepository.findAll().stream()
            .filter(medico -> medico.getEspecialidad() != null && 
                             medico.getEspecialidad().getId().equals(especialidadId))
            .map(this::convertirMedicoAResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicoResponse obtenerMedicoPorId(Long id) {
        Medico medico = findById(id)
        .orElseThrow(() -> new RuntimeException("Medico no encontrado con id: " + id));
        return convertirMedicoAResponse(medico);
    }

    public void eliminarMedico(Long id) {
        deleteById(id);
    }

    public MedicoResponse actualizarMedico (Long id,RegistroUsuarioRequest request) {
        Medico medico = findById(id)
        .orElseThrow(() -> new RuntimeException("Medico no encontrado con id: " + id));
        actualizarMedicoDesdeRequest(medico, request);
        return convertirMedicoAResponse(save(medico));
    }

    private Medico convertirRequestAMedico(RegistroUsuarioRequest request){
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setTelefono(request.getTelefono());
        usuario.setRol(com.proyecto.backend_api.domain.enums.Rol.MEDICO);
        usuario.setActivo(true);
        usuario = usuarioRepository.save(usuario);

        Medico medico = new Medico();
        medico.setUsuario(usuario);
        medico.setMatricula(request.getMatricula());
        if (request.getEspecialidadId() != null) {
            especilidadRepository.findById(request.getEspecialidadId())
            .ifPresent(medico::setEspecialidad);
        }
        if (request.getSucursalId() != null) {
            sucursalRepository.findById(request.getSucursalId())
            .ifPresent(medico::setSucursal);
        }
        return medico;
    } 

    private void actualizarMedicoDesdeRequest(Medico medico, RegistroUsuarioRequest request) {
        Usuario usuario = medico.getUsuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setTelefono(request.getTelefono());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getEspecialidadId() != null) {
            especilidadRepository.findById(request.getEspecialidadId())
            .ifPresent(medico::setEspecialidad);
        }

        if (request.getSucursalId() != null) {
            sucursalRepository.findById(request.getSucursalId())
            .ifPresent(medico::setSucursal);
        }
    }

    private MedicoResponse convertirMedicoAResponse(Medico medico) {
        MedicoResponse.MedicoResponseBuilder builder = MedicoResponse.builder()
            .id(medico.getId())
            .matricula(medico.getMatricula())
            .duracionTurnoMinutos(medico.getDuracionTurnoMinutos())
            .activo(medico.getActivo());
        
        // Verificar que el usuario no sea null antes de acceder
        if (medico.getUsuario() != null) {
            builder.nombreCompleto(medico.getUsuario().getNombre() + " " + medico.getUsuario().getApellido());
        }
        
        // Agregar especialidad si existe
        if (medico.getEspecialidad() != null) {
            builder.especialidad(EspecialidadResponse.builder()
                .id(medico.getEspecialidad().getId())
                .nombre(medico.getEspecialidad().getNombre())
                .descripcion(medico.getEspecialidad().getDescripcion())
                .build());
        }
        
        // Agregar sucursal si existe
        if (medico.getSucursal() != null) {
            builder.sucursal(SucursalResponse.builder()
                .id(medico.getSucursal().getId())
                .nombre(medico.getSucursal().getNombre())
                .direccion(medico.getSucursal().getDireccion())
                .build());
        }
        
        return builder.build();
    }

}
