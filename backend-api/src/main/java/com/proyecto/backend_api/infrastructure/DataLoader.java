package com.proyecto.backend_api.infrastructure;

import com.proyecto.backend_api.domain.enums.Rol;
import com.proyecto.backend_api.domain.model.Especialidad;
import com.proyecto.backend_api.domain.model.Sucursal;
import com.proyecto.backend_api.domain.model.Usuario;
import com.proyecto.backend_api.domain.repository.EspecialidadRepository;
import com.proyecto.backend_api.domain.repository.SucursalRepository;
import com.proyecto.backend_api.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final EspecialidadRepository especialidadRepository;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (especialidadRepository.count() == 0) {
            cargarEspecialidades();
        }
        
        if (sucursalRepository.count() == 0) {
            cargarSucursales();
        }
        
        if (usuarioRepository.count() == 0) {
            cargarUsuariosIniciales();
        }
        
        log.info("✅ Datos iniciales cargados correctamente");
    }

    private void cargarEspecialidades() {
        List<Especialidad> especialidades = Arrays.asList(
            Especialidad.builder()
                .nombre("Cardiología")
                .descripcion("Especialidad médica que se encarga del estudio, diagnóstico y tratamiento de las enfermedades del corazón")
                .build(),
            Especialidad.builder()
                .nombre("Dermatología")
                .descripcion("Especialidad médica que se encarga del estudio de la estructura y función de la piel")
                .build(),
            Especialidad.builder()
                .nombre("Pediatría")
                .descripcion("Especialidad médica que estudia al niño y sus enfermedades")
                .build(),
            Especialidad.builder()
                .nombre("Traumatología")
                .descripcion("Especialidad médica que se dedica al estudio de las lesiones del aparato locomotor")
                .build(),
            Especialidad.builder()
                .nombre("Oftalmología")
                .descripcion("Especialidad médica que estudia las enfermedades del ojo y su tratamiento")
                .build(),
            Especialidad.builder()
                .nombre("Ginecología")
                .descripcion("Especialidad médica que trata las enfermedades del sistema reproductor femenino")
                .build(),
            Especialidad.builder()
                .nombre("Neurología")
                .descripcion("Especialidad médica que trata los trastornos del sistema nervioso")
                .build(),
            Especialidad.builder()
                .nombre("Psiquiatría")
                .descripcion("Especialidad dedicada al estudio y tratamiento de las enfermedades mentales")
                .build()
        );

        especialidadRepository.saveAll(especialidades);
        log.info("✅ Especialidades cargadas: {}", especialidades.size());
    }

    private void cargarSucursales() {
        List<Sucursal> sucursales = Arrays.asList(
            Sucursal.builder()
                .nombre("Centro Médico Central")
                .direccion("Av. Corrientes 1234")
                .ciudad("Buenos Aires")
                .provincia("Buenos Aires")
                .telefono("011-4567-8900")
                .email("central@turnosmedicos.com")
                .activo(true)
                .build(),
            Sucursal.builder()
                .nombre("Clínica Norte")
                .direccion("Av. Santa Fe 5678")
                .ciudad("Buenos Aires")
                .provincia("Buenos Aires")
                .telefono("011-4567-8901")
                .email("norte@turnosmedicos.com")
                .activo(true)
                .build(),
            Sucursal.builder()
                .nombre("Centro Médico Sur")
                .direccion("Av. Belgrano 9012")
                .ciudad("Buenos Aires")
                .provincia("Buenos Aires")
                .telefono("011-4567-8902")
                .email("sur@turnosmedicos.com")
                .activo(true)
                .build()
        );

        sucursalRepository.saveAll(sucursales);
        log.info("✅ Sucursales cargadas: {}", sucursales.size());
    }

    private void cargarUsuariosIniciales() {
        // Usuario Admin
        Usuario admin = Usuario.builder()
                .email("admin@turnosmedicos.com")
                .password(passwordEncoder.encode("admin123"))
                .nombre("Admin")
                .apellido("Sistema")
                .telefono("011-1111-1111")
                .rol(Rol.ADMIN)
                .activo(true)
                .build();
        usuarioRepository.save(admin);

        // Usuario Paciente de prueba
        Usuario pacienteUser = Usuario.builder()
                .email("paciente@test.com")
                .password(passwordEncoder.encode("paciente123"))
                .nombre("Juan")
                .apellido("Pérez")
                .telefono("011-2222-2222")
                .rol(Rol.PACIENTE)
                .activo(true)
                .build();
        usuarioRepository.save(pacienteUser);

        // Usuario Médico de prueba
        Usuario medicoUser = Usuario.builder()
                .email("medico@test.com")
                .password(passwordEncoder.encode("medico123"))
                .nombre("María")
                .apellido("González")
                .telefono("011-3333-3333")
                .rol(Rol.MEDICO)
                .activo(true)
                .build();
        usuarioRepository.save(medicoUser);

        log.info("✅ Usuarios iniciales creados:");
        log.info("   👤 Admin: admin@turnosmedicos.com / admin123");
        log.info("   👤 Paciente: paciente@test.com / paciente123");
        log.info("   👤 Médico: medico@test.com / medico123");
    }
}
