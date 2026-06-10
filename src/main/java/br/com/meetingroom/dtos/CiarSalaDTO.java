package br.com.meetingroom.dtos;

import br.com.meetingroom.enums.TipoSala;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CiarSalaDTO(
        @NotBlank
        String nome,
        @NotNull
        TipoSala tipo,
        @Positive
        @NotNull
        Integer capacidadeSala

) {
}
