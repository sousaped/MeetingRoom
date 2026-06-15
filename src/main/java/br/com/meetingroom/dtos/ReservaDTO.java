package br.com.meetingroom.dtos;

import br.com.meetingroom.enums.StatusReserva;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservaDTO(
        LocalDateTime inicioReserva,
        LocalDateTime fimReserva,
        Integer qtdPessoas,
        StatusReserva statusReserva,
        @NotNull
        Long usuarioId,
        @NotNull
        Long salaId) {
}
