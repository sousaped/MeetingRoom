package br.com.meetingroom.dtos;

import br.com.meetingroom.enums.StatusReserva;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservaDTO(
        StatusReserva tipo,
        LocalDateTime inicioReserva,
        LocalDateTime fimReserva,
        Integer qtdPessoas,
        @NotNull
        Long usuarioId,
        @NotNull
        Long salaId) {
}
