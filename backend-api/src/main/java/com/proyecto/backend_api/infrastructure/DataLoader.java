package com.proyecto.backend_api.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;

import com.proyecto.backend_api.domain.model.Especialidad;
import com.proyecto.backend_api.domain.model.Medico;
import com.proyecto.backend_api.domain.model.Paciente;
import com.proyecto.backend_api.domain.repository.*;

@Configuration
public class DataLoader {
    
    @Bean
    public CommandLineRunner initDataBase (MedicoRepository medicoRepo, PacienteRepository pacienteRepo, EspecilidadRepository especilidadRepsoitory) {
        return args -> {
            // Solo cargamos datos si la base de datos esta vacia
            // 1. Crear y guardar especialidades PRIMERO
            Especialidad cardio = Especialidad.builder().nombre("Cardiologia").build();
            Especialidad pedia = Especialidad.builder().nombre("Pediatria").build();
            
            especilidadRepsoitory.saveAll(Set.of(cardio, pedia));

            // 2. Crear medicos con las especialidades ya guardadas
            Medico medico1 = Medico.builder()
                .nombre("Juan")
                .apellido("Perez")
                .matricula("MAT123")
                .especialidades(Set.of(cardio))
                .build();

            Medico medico2 = Medico.builder()
                .nombre("Maria")
                .apellido("Gomez")
                .matricula("MAT456")
                .especialidades(Set.of(pedia))
                .build();

            medicoRepo.saveAll(Set.of(medico1,medico2));
            
            // 3. Crear Pacientes
            Paciente paciente1 = Paciente.builder()
                .nombre("Juan")
                .apellido("Perez")
                .dni("12345678")
                .email("juanPerez@gmail.com")
                .fechaNacimiento(LocalDate.of(1980,5,12))
                .build();
            pacienteRepo.save(paciente1);

            System.out.println("DATOS DE PRUEBA CARGADOS CORRECTAMENTE");
        };




    }
}
