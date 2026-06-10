package br.com.meetingroom.dtos;

import br.com.meetingroom.entities.Reserva;
import br.com.meetingroom.enums.StatusReserva;

import java.time.LocalDateTime;

public record ReservaResponseDTO(
        StatusReserva tipo,
        LocalDateTime inicioReserva,
        LocalDateTime fimReserva) {

    public ReservaResponseDTO(Reserva reserva){
        this(
                reserva.getStatusReserva(),
                reserva.getInicioReserva(),
                reserva.getFimReserva()
        );
    }
}
