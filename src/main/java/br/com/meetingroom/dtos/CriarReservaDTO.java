package br.com.meetingroom.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CriarReservaDTO(
        @NotNull
        @Future
        LocalDateTime inicioReserva,
        @NotNull
        LocalDateTime fimReserva,
        @Positive
        Integer qtdPessoas,
        @NotNull
        Long usuarioId,
        @NotNull
        Long salaId
) {
}
