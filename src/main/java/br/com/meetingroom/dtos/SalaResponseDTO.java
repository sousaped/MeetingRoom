package br.com.meetingroom.dtos;

import br.com.meetingroom.entities.Sala;

public record SalaResponseDTO(
        String nomeSala,
        Integer capacidadeSala) {

    public SalaResponseDTO(Sala sala) {
        this(
                sala.getNome(),
                sala.getCapacidadeSala()
        );
    }
}
