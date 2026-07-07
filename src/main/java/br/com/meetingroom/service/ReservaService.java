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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final IReservaRepository repository;
    private final ISalaRepository salaRepository;
    private final IUsuarioRepository usuarioRepository;

    @Transactional(readOnly = true) // apenas leitura, sem lock
    public Page<Reserva> findAll(Pageable pageable) {
        return repository.findAll(pageable);

    }

    @Transactional(readOnly = true) // leitura de conflito + save devem ser atômicos
    public Reserva findById(Long id) {
        //Busca a reserva por ID
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada !!"));


    }

    @Transactional
    public Reserva criaReserva(ReservaDTO dto) {
        // Busca sala e usuário no banco
        Sala sala = salaRepository.findById(dto.salaId())
                .orElseThrow(() -> new NotFoundException("Sala não encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        validaReserva(sala, dto, null);


        Reserva reserva = new Reserva();
        reserva.setSala(sala);
        reserva.setUsuario(usuario);
        reserva.setQtdPessoas(dto.qtdPessoas());
        reserva.setStatusReserva(dto.statusReserva());
        reserva.setInicioReserva(dto.inicioReserva());
        reserva.setFimReserva(dto.fimReserva());

        return repository.save(reserva);


    }

    @Transactional
    public Reserva atualizaReserva(Long id, ReservaDTO dto) {
        //Verifica se existe reserva com esse id
        Reserva reserva = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva inexistente !!"));

        // Busca sala e usuário no banco
        Sala sala = salaRepository.findById(dto.salaId())
                .orElseThrow(() -> new NotFoundException("Sala não encontrada"));
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        validaReserva(sala, dto, id);

        //Seta o id da sala.
        reserva.setSala(sala);
        //Seta um novo valor para StatusReserva
        reserva.setStatusReserva(dto.statusReserva());
        //Seta um novo valor para o início da reserva
        reserva.setInicioReserva(dto.inicioReserva());
        //Seta um novo valor para o fim da reserva
        reserva.setFimReserva(dto.fimReserva());
        //Seta um novo valor para a quantidade de pessoas
        reserva.setQtdPessoas(dto.qtdPessoas());


        // Salva os dados no banco
        return repository.save(reserva);

    }

    @Transactional
    public void cancelaReserva(Long id) {
        //Verifica se existe reserva com esse id
        Reserva reserva = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva inexistente !!"));
        //Seta o Status Cancelado
        reserva.setStatusReserva(StatusReserva.CANCELADA);

        repository.save(reserva);
    }


    private void validaReserva(Sala sala, ReservaDTO dto, Long reservaIdIgnorar) {

        // 1. inicio < fim
        if (!dto.inicioReserva().isBefore(dto.fimReserva())) {
            throw new BadRequestException("Início deve ser anterior ao fim");
        }

        // 2. sala ativa
        if (!sala.isAtivo()) {
            throw new BadRequestException("Sala indisponível");
        }

        // 3. capacidade
        if (dto.qtdPessoas() > sala.getCapacidadeSala()) {
            throw new BadRequestException("Quantidade de pessoas excede a capacidade da sala");
        }

        // 4. conflito
        boolean temConflito = !repository.findConflitosExcluindoReserva(
                dto.salaId(),
                dto.inicioReserva(),
                dto.fimReserva(),
                reservaIdIgnorar
        ).isEmpty();

        if (temConflito) {
            throw new BadRequestException("Sala indisponível para o período solicitado");
        }
    }


}
