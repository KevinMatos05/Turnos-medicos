package com.proyecto.backend_api.infrastructure;

import com.proyecto.backend_api.domain.enums.Rol;
import com.proyecto.backend_api.domain.model.Especialidad;
import com.proyecto.backend_api.domain.model.Medico;
import com.proyecto.backend_api.domain.model.Paciente;
import com.proyecto.backend_api.domain.model.Sucursal;
import com.proyecto.backend_api.domain.model.Usuario;
import com.proyecto.backend_api.domain.repository.EspecialidadRepository;
import com.proyecto.backend_api.domain.repository.MedicoRepository;
import com.proyecto.backend_api.domain.repository.PacienteRepository;
import com.proyecto.backend_api.domain.repository.SucursalRepository;
import com.proyecto.backend_api.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Profile("!prod")
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final EspecialidadRepository especialidadRepository;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
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
        
        if (medicoRepository.count() == 0) {
            cargarMedicosYPacientesIniciales();
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
    
    private void cargarMedicosYPacientesIniciales() {
        // Obtener especialidades y sucursales
        List<Especialidad> especialidades = especialidadRepository.findAll();
        List<Sucursal> sucursales = sucursalRepository.findAll();
        
        if (especialidades.isEmpty() || sucursales.isEmpty()) {
            log.warn("⚠️ No se pueden crear médicos sin especialidades o sucursales");
            return;
        }
        
        // Crear médicos de prueba
        Usuario medico1User = Usuario.builder()
                .email("dr.garcia@turnosmedicos.com")
                .password(passwordEncoder.encode("medico123"))
                .nombre("Carlos")
                .apellido("García")
                .telefono("011-4444-4444")
                .rol(Rol.MEDICO)
                .activo(true)
                .build();
        usuarioRepository.save(medico1User);
        
        Medico medico1 = Medico.builder()
                .usuario(medico1User)
                .matricula("MN-12345")
                .especialidad(especialidades.get(0)) // Cardiología
                .sucursal(sucursales.get(0))
                .duracionTurnoMinutos(30)
                .activo(true)
                .build();
        medicoRepository.save(medico1);
        
        Usuario medico2User = Usuario.builder()
                .email("dra.lopez@turnosmedicos.com")
                .password(passwordEncoder.encode("medico123"))
                .nombre("Ana")
                .apellido("López")
                .telefono("011-5555-5555")
                .rol(Rol.MEDICO)
                .activo(true)
                .build();
        usuarioRepository.save(medico2User);
        
        Medico medico2 = Medico.builder()
                .usuario(medico2User)
                .matricula("MN-67890")
                .especialidad(especialidades.size() > 1 ? especialidades.get(1) : especialidades.get(0)) // Dermatología
                .sucursal(sucursales.size() > 1 ? sucursales.get(1) : sucursales.get(0))
                .duracionTurnoMinutos(20)
                .activo(true)
                .build();
        medicoRepository.save(medico2);
        
        Usuario medico3User = Usuario.builder()
                .email("dr.martinez@turnosmedicos.com")
                .password(passwordEncoder.encode("medico123"))
                .nombre("Roberto")
                .apellido("Martínez")
                .telefono("011-6666-6666")
                .rol(Rol.MEDICO)
                .activo(true)
                .build();
        usuarioRepository.save(medico3User);
        
        Medico medico3 = Medico.builder()
                .usuario(medico3User)
                .matricula("MN-11111")
                .especialidad(especialidades.size() > 2 ? especialidades.get(2) : especialidades.get(0)) // Pediatría
                .sucursal(sucursales.get(0))
                .duracionTurnoMinutos(30)
                .activo(true)
                .build();
        medicoRepository.save(medico3);
        
        // Crear paciente de prueba completo
        Usuario pacienteUser = usuarioRepository.findByEmail("paciente@test.com").orElse(null);
        if (pacienteUser != null) {
            Paciente paciente = Paciente.builder()
                    .usuario(pacienteUser)
                    .documento("12345678")
                    .fechaNacimiento(LocalDate.of(1990, 5, 15))
                    .direccion("Calle Falsa 123")
                    .obraSocial("OSDE")
                    .numeroAfiliado("123456789")
                    .activo(true)
                    .build();
            pacienteRepository.save(paciente);
        }
        
        log.info("✅ Médicos de prueba creados:");
        log.info("   🩺 Dr. García (Cardiología) - dr.garcia@turnosmedicos.com / medico123");
        log.info("   🩺 Dra. López (Dermatología) - dra.lopez@turnosmedicos.com / medico123");
        log.info("   🩺 Dr. Martínez (Pediatría) - dr.martinez@turnosmedicos.com / medico123");
        log.info("✅ Paciente de prueba completo creado");
    }
}
