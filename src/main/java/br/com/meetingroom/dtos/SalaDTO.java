package br.com.meetingroom.dtos;

import br.com.meetingroom.enums.TipoSala;

public record SalaDTO(
        Long id,
        String nomeSala,
        TipoSala tipo,
        Integer capacidadeSala
) {
}
