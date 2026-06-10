package br.com.meetingroom.dtos;

import br.com.meetingroom.entities.Usuario;

public record UsuarioResponseDTO(
        String nome,
        String email,
        String telefone) {

    public UsuarioResponseDTO (Usuario usuario) {
        this(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }
}
