package br.com.meetingroom.dtos;

import jakarta.validation.constraints.NotBlank;


public record UsuarioDTO(
        Long id,
        @NotBlank
        String nome,
        @NotBlank
        String email,
        @NotBlank
        String telefone
) {
}
