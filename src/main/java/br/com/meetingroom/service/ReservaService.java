package br.com.meetingroom.service;

import br.com.meetingroom.dtos.ReservaDTO;
import br.com.meetingroom.dtos.ReservaResponseDTO;
import br.com.meetingroom.entities.Reserva;
import br.com.meetingroom.entities.Sala;
import br.com.meetingroom.entities.Usuario;
import br.com.meetingroom.enums.StatusReserva;
import br.com.meetingroom.execptions.BadRequestException;
import br.com.meetingroom.execptions.NotFoundException;
import br.com.meetingroom.repository.IReservaRepository;
import br.com.meetingroom.repository.ISalaRepository;
import br.com.meetingroom.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final IReservaRepository repository;
    private final ISalaRepository salaRepository;
    private final IUsuarioRepository usuarioRepository;

    public List<ReservaResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(ReservaResponseDTO::new)
                .toList();

    }

    public ReservaResponseDTO findById(Long id) {
        return repository.findById(id)
                .map(ReservaResponseDTO::new)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada !!"));


    }

    public ReservaResponseDTO criaReserva(ReservaDTO dto) {


        // Validação de intervalo
        if (!dto.inicioReserva().isBefore(dto.fimReserva())) {
            throw new BadRequestException("Início deve ser anterior ao fim");
        }

        // Busca sala e usuário no banco
        Sala sala = salaRepository.findById(dto.salaId())
                .orElseThrow(() -> new NotFoundException("Sala não encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        // Sala deve estar ativa
        if (!sala.getAtivo()) {
            throw new BadRequestException("Sala indisponível");
        }

        // Validação de capacidade
        if (dto.qtdPessoas() > sala.getCapacidadeSala()) {
            throw new BadRequestException("Quantidade de pessoas excede a capacidade da sala");
        }


        Reserva reserva = new Reserva();
        reserva.setStatusReserva(dto.tipo());
        reserva.setInicioReserva(dto.inicioReserva());
        reserva.setFimReserva(dto.fimReserva());

        return new ReservaResponseDTO(repository.save(reserva));


    }

    public ReservaResponseDTO atualizaReserva(Long id, ReservaDTO dto) {
        //Verifica se existe reserva com esse id
        Reserva reserva = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva inexistente !!"));

        //Seta um novo valor para StatusReserva
        reserva.setStatusReserva(dto.tipo());
        //Seta um novo valor para o início da reserva
        reserva.setInicioReserva(dto.inicioReserva());
        //Seta um novo valor para o fim da reserva
        reserva.setFimReserva(dto.fimReserva());

        // Salva os dados no banco
        return new ReservaResponseDTO(repository.save(reserva));

    }


    public void cancelaReserva(Long id) {
        //Verifica se existe reserva com esse id
        Reserva reserva = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva inexistente !!"));
        //Seta o Status Cancelado
        reserva.setStatusReserva(StatusReserva.CANCELADA);

        //Exclui do banco de dados a reserva
        repository.delete(reserva);
    }


}
