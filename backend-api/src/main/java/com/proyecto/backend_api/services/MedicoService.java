package com.proyecto.backend_api.services;

import java.util.stream.Collectors;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.backend_api.dto.MedicoRespuestaDTO;
import com.proyecto.backend_api.repository.MedicoRepository;

@Service
public class MedicoService {
    @Autowired
    private MedicoRepository medicoRepository;

    public List<MedicoRespuestaDTO> getAll() {
        return medicoRepository.findAll().stream()
        .map(medico -> new MedicoRespuestaDTO(medico.getId(), medico.getNombre(), medico.getApellido(), medico.getMatricula()
    ))
    .collect(Collectors.toList());
    }
}
