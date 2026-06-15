package br.com.meetingroom.service;

import br.com.meetingroom.dtos.ReservaDTO;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final IReservaRepository repository;
    private final ISalaRepository salaRepository;
    private final IUsuarioRepository usuarioRepository;

    public List<Reserva> findAll() {
        //Busca todas as reservas
        return repository.findAll();

    }

    public Reserva findById(Long id) {
        //Busca a reserva por ID
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada !!"));


    }

    public Reserva criaReserva(ReservaDTO dto) {
        // Busca sala e usuário no banco
        Sala sala = salaRepository.findById(dto.salaId())
                .orElseThrow(() -> new NotFoundException("Sala não encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        List<Reserva> reservas = repository.findByUsuarioIdAndInicioReservaAndStatusReserva(
                dto.usuarioId(),
                dto.inicioReserva(),
                StatusReserva.ATIVA
        );

        for (Reserva reserva : reservas) {
            LocalDateTime reservaInicio = reserva.getInicioReserva();
            LocalDateTime reservaFim = reservaInicio.plusMinutes(
                    reserva.getFimReserva().getMinute()
            );

            LocalDateTime novoInicio = dto.inicioReserva();

            if (novoInicio.isBefore(reservaFim) && novoInicio.isAfter(reservaInicio.minusMinutes(1))) {
                throw new BadRequestException("Sala não está disponível. Próximo horário disponível: " + reservaFim);
            }
        }


        // Validação de intervalo
        if (!dto.inicioReserva().isBefore(dto.fimReserva())) {
            throw new BadRequestException("Início deve ser anterior ao fim");
        }

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

        return repository.save(reserva);


    }

    public Reserva atualizaReserva(Long id, ReservaDTO dto) {
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
        return repository.save(reserva);

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
