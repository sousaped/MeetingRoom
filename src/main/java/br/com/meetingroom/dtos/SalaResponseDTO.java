package br.com.meetingroom.dtos;

import br.com.meetingroom.entities.Sala;
import br.com.meetingroom.enums.TipoSala;

public record SalaResponseDTO(
        TipoSala nomeSala,
        Integer capacidadeSala) {

    public SalaResponseDTO(Sala sala) {
        this(
                sala.getNomeSala(),
                sala.getCapacidadeSala()
        );
    }
}
