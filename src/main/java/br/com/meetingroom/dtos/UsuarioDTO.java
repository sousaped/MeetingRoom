package br.com.meetingroom.dtos;

public record UsuarioDTO(
        Long id,
        String nome,
        String email,
        String telefone
) {
}
