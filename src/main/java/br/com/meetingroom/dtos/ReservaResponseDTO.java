package br.com.meetingroom.dtos;

import br.com.meetingroom.entities.Reserva;
import br.com.meetingroom.entities.Sala;
import br.com.meetingroom.entities.Usuario;
import br.com.meetingroom.enums.StatusReserva;

import java.time.LocalDateTime;

public record ReservaResponseDTO(
        Long id,
        LocalDateTime inicioReserva,
        LocalDateTime fimReserva,
        StatusReserva statusReserva,
        Integer qtdPessoas,
        Long salaId,
        String salaNome,
        Long usuarioId,
        String usuarioNome) {

    public ReservaResponseDTO(Reserva reserva){
        this(
                reserva.getId(),
                reserva.getInicioReserva(),
                reserva.getFimReserva(),
                reserva.getStatusReserva(),
                reserva.getQtdPessoas(),
                reserva.getSala().getId(),
                reserva.getSala().getNome(),
                reserva.getUsuario().getId(),
                reserva.getUsuario().getNome()
        );
    }
}
