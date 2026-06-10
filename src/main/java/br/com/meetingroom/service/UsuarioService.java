package br.com.meetingroom.service;

import br.com.meetingroom.dtos.UsuarioDTO;
import br.com.meetingroom.dtos.UsuarioResponseDTO;
import br.com.meetingroom.entities.Usuario;
import br.com.meetingroom.execptions.BadRequestException;
import br.com.meetingroom.execptions.NotFoundException;
import br.com.meetingroom.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final IUsuarioRepository repository;

    public List<UsuarioResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }

    public UsuarioResponseDTO findById(Long id) {
        return repository.findById(id)
                .map(UsuarioResponseDTO::new)
                .orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
    }

    public UsuarioResponseDTO criaUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new BadRequestException("Este e-mail já está cadastrado");
        }

        usuario.setAtivo(true);
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setTelefone(dto.telefone());
        return new UsuarioResponseDTO(repository.save(usuario));
    }


    public UsuarioResponseDTO atualizaUsuario(Long id, UsuarioDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado !!"));

        usuario.setEmail(dto.email());
        usuario.setTelefone(dto.telefone());

        return new UsuarioResponseDTO(repository.save(usuario));
    }


    public void desativarUsuario(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado !!"));

        usuario.usuarioInativo();
        repository.save(usuario);
    }


}
