package br.com.meetingroom.service;

import br.com.meetingroom.dtos.UsuarioDTO;
import br.com.meetingroom.entities.Usuario;
import br.com.meetingroom.execptions.NotFoundException;
import br.com.meetingroom.repository.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService service;

    @Mock
    IUsuarioRepository repository;

    Usuario usuario = new Usuario();

    ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);


    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Alex");
        usuario.setEmail("Alex@gmail.com");
        usuario.setTelefone("+334567890");
        usuario.setAtivo(true);


    }

    @Test
    void deveBuscarTodosUsuariosComSucesso() {
        //Arrange
        when(repository.findAll()).thenReturn(Collections.singletonList(usuario));


        //ACT
        List<Usuario> usuarios = service.findAll();

        //Assert
        assertEquals(Collections.singletonList(usuario), usuarios);
        verify(repository).findAll();
        verifyNoMoreInteractions(repository);


    }

    @Test
    void deveBuscarUsuariosPorEmailComSucesso() {
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        Usuario resultado = service.findByEmail(usuario.getEmail());

        assertEquals(usuario, resultado);
        verify(repository).findByEmail(usuario.getEmail());
        verifyNoMoreInteractions(repository);


    }

    @Test
    void deveBuscarUsuariosPorEmailComErro() {
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            service.findByEmail(usuario.getEmail());
        });

        verify(repository).findByEmail(usuario.getEmail());
        verifyNoMoreInteractions(repository);


    }

    @Test
    void devePermitirCriarUsuario() {
        UsuarioDTO dto = new UsuarioDTO(1L, "Alex", "alex@gmail.com", "11940028922");
        when(repository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(repository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = service.criaUsuario(dto);

        verify(repository).save(captor.capture());
        Usuario capturado = captor.getValue();


        assertThat(capturado.getNome())
                .isEqualTo(dto.nome());

        assertThat(capturado.getEmail())
                .isEqualTo(dto.email());

        assertThat(capturado.getTelefone())
                .isEqualTo(dto.telefone());

        assertThat(capturado.isAtivo())
                .isTrue();


    }


    @Test
    void devePermitirAlterarUsuario() {
        UsuarioDTO dto = new UsuarioDTO(usuario.getId(), "Alex", "alex@live.com", "11940028922");
        when(repository.findById(dto.id())).thenReturn(Optional.of(usuario));
        when(repository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = service.atualizaUsuario(usuario.getId(), dto);
        verify(repository).save(captor.capture());
        Usuario capturado = captor.getValue();


        assertThat(capturado.getNome())
                .isEqualTo(dto.nome());

        assertThat(capturado.getEmail())
                .isEqualTo(dto.email());

        assertThat(capturado.getTelefone())
                .isEqualTo(dto.telefone());


    }

    @Test
    void devePermitirDesativarUsuario() {
        var recebeId = usuario.getId();
        when(repository.findById(recebeId)).thenReturn(Optional.of(usuario));
        when(repository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        service.desativarUsuario(recebeId);

        assertThat(usuario.isAtivo())
            .isFalse();


    }
}