package br.com.meetingroom.service;

import br.com.meetingroom.dtos.UsuarioDTO;
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

    public List<Usuario> findAll() {
        // Busca todos os usuarios cadastrados
        return repository.findAll();

    }

    public Usuario findByEmail(String email) {
        // Busca o usuario por ID
        return repository.findByEmail(email)
                //Se não encontrar o usuario devolve "Usuário não encontrado"
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

    }

    public Usuario criaUsuario(UsuarioDTO dto) {
        // Verifica se existe algum usúario cadastrado com ese e-mail se encontra devolve "Este e-mail já está cadastrado"
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new BadRequestException("Este e-mail já está cadastrado");
        }


        Usuario usuario = new Usuario();
        //Ao Cadastrar o usuario deixa ele como ativo
        usuario.setAtivo(true);
        //Cadastra um nome para o usuario
        usuario.setNome(dto.nome());
        //Cadastra um e-mail para o usuario
        usuario.setEmail(dto.email());
        //Cadastra um telefone para o usuario
        usuario.setTelefone(dto.telefone());
        //Salva no banco
        return repository.save(usuario);
    }


    public Usuario atualizaUsuario(Long id, UsuarioDTO dto) {
        //Verifica se o usuario já está cadastrado caso não esteja lança "Usuário não encontrado !!"
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado !!"));

        //Troca o e-mail cadastrado
        usuario.setEmail(dto.email());
        //Troca o telefone cadastrado
        usuario.setTelefone(dto.telefone());

        return repository.save(usuario);
    }


    public void desativarUsuario(Long id) {
        //Verifica se o usuario já está cadastrado caso não esteja lança "Usuário não encontrado !!"
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado !!"));
        //Desativa o usuario
        usuario.usuarioInativo();
        //salva no banco
        repository.save(usuario);
    }


}
