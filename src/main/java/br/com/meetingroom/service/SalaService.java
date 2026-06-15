package br.com.meetingroom.service;

import br.com.meetingroom.dtos.SalaDTO;
import br.com.meetingroom.entities.Sala;
import br.com.meetingroom.execptions.BadRequestException;
import br.com.meetingroom.execptions.NotFoundException;
import br.com.meetingroom.repository.ISalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final ISalaRepository repository;

    public List<Sala> buscarTodasSalas() {
        //Lista todas as Salas
        return repository.findAll();
    }

    public Sala BuscarPorNome(String nome) {
        // Busca a sala por nome
        return repository.findByNome(nome)
                .orElseThrow(() -> new NotFoundException("Sala não encontrado"));
    }

    public Sala criaSala(SalaDTO dto) {
        //Valida se já existe uma sala com esse nome
        if (repository.findByNome(dto.nomeSala()).isPresent()) {
            throw new BadRequestException("Já existe uma sala com esse nome");
        }


        Sala sala = new Sala();
        //Nome da sala
        sala.setNome(dto.nomeSala());

        //Capacidade da sala
        sala.setCapacidadeSala(dto.capacidadeSala());

        //Salva no banco
        return repository.save(sala);
    }


    public Sala atualizaSala(Long id, SalaDTO dto) {
        //Valida se a sala já existe
        Sala sala = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sala inexistente"));

        //Troca o nome da sala
        sala.setNome(dto.nomeSala());

        //Troca a capacidade da Sala
        sala.setCapacidadeSala(dto.capacidadeSala());

        //Salva no banco
        return (repository.save(sala));

    }

    public void desativaSala(Long id) {
        //Valida se a sala existe
        Sala sala = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sala inexistente"));
        //Desativa a sala
        sala.salaDesativada();
        //Salva no banco
        repository.save(sala);
    }
}
