package br.com.meetingroom.service;

import br.com.meetingroom.dtos.ReservaDTO;
import br.com.meetingroom.entities.Reserva;
import br.com.meetingroom.entities.Sala;
import br.com.meetingroom.entities.Usuario;
import br.com.meetingroom.enums.StatusReserva;
import br.com.meetingroom.enums.TipoSala;
import br.com.meetingroom.execptions.NotFoundException;
import br.com.meetingroom.repository.IReservaRepository;
import br.com.meetingroom.repository.ISalaRepository;
import br.com.meetingroom.repository.IUsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class
ReservaServiceTest {
    @InjectMocks
    private ReservaService service;

    @Mock
    private IReservaRepository repository;
    @Mock
    private ISalaRepository salaRepository;
    @Mock
    private IUsuarioRepository usuarioRepository;


    @Test
    @DisplayName("Deve criar reserva com sucesso quando dados são válidos")
    void deveRetornarSeReservaFoiCriada() {
        //Arrange
        Long salaId = 1L;
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("fulano");
        usuario.setEmail("fulano@gmail.com");
        usuario.setTelefone("(11)9 1234-5678");
        usuario.setAtivo(true);

        Sala sala = new Sala();
        sala.setId(salaId);
        sala.setNome("Sala 01");
        sala.setTipo(TipoSala.X);
        sala.setCapacidadeSala(6);
        sala.setAtivo(true);

        ReservaDTO dto = new ReservaDTO(
                LocalDateTime.of(2026, 2, 25, 19, 0),
                LocalDateTime.of(2026, 2, 25, 21, 0),
                5,
                StatusReserva.ATIVA,
                usuarioId,
                salaId

        );

        when(salaRepository.findById(salaId)).thenReturn(Optional.of(sala));
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(repository.save(any(Reserva.class))).thenAnswer(i -> i.getArguments()[0]);


        //Act
        Reserva resultado = service.criaReserva(dto);

        //Asserts

        assertThat(resultado).isNotNull();
        assertThat(resultado.getSala()).isEqualTo(sala);
        assertThat(resultado.getUsuario()).isEqualTo(usuario);
        assertThat(resultado.getStatusReserva()).isEqualTo(StatusReserva.ATIVA);
        assertThat(resultado.getQtdPessoas()).isEqualTo(5);

        verify(repository, times(1)).save(any(Reserva.class));


    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando sala não existe")
    void deveLancarExcecaoQuandoSalaNaoEncontrada() {
        //Arrange
        Long salaId = 1L;
        ReservaDTO dto = new ReservaDTO(
                LocalDateTime.of(2026, 2, 25, 19, 0),
                LocalDateTime.of(2026, 2, 25, 21, 0),
                5,
                StatusReserva.ATIVA,
                1L,
                salaId

        );

        when(salaRepository.findById(salaId)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> service.criaReserva(dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Sala não encontrada");


    }
}