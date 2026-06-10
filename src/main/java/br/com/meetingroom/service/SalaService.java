package br.com.meetingroom.service;

import br.com.meetingroom.dtos.SalaDTO;
import br.com.meetingroom.dtos.SalaResponseDTO;
import br.com.meetingroom.entities.Sala;
import br.com.meetingroom.enums.TipoSala;
import br.com.meetingroom.execptions.NotFoundException;
import br.com.meetingroom.repository.ISalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final ISalaRepository repository;

    public List<SalaResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(SalaResponseDTO::new)
                .toList();
    }

    public SalaResponseDTO findByName(TipoSala name) {
        return repository.findByName(name)
                .map(SalaResponseDTO::new)
                .orElseThrow(() -> new NotFoundException("Sala não encontrado"));
    }

    public SalaResponseDTO criaSala(SalaDTO dto) {
        Sala sala = new Sala();


        sala.setNomeSala(dto.nomeSala());
        sala.setCapacidadeSala(dto.capacidadeSala());

        return new SalaResponseDTO(repository.save(sala));
    }


    public SalaResponseDTO atualizaSala(Long id, SalaResponseDTO dto) {
        Sala sala = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sala inexistente"));

        sala.setCapacidadeSala(dto.capacidadeSala());

        return new SalaResponseDTO(repository.save(sala));

    }

    public void destivaSala(Long id) {
        Sala sala = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sala inexistente"));

        sala.salaDesativada();
        repository.save(sala);
    }
}
