package br.com.meetingroom.service;


import br.com.meetingroom.dtos.SalaDTO;
import br.com.meetingroom.entities.Sala;
import br.com.meetingroom.enums.TipoSala;
import br.com.meetingroom.repository.ISalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @InjectMocks
    private SalaService service;

    @Mock
    private ISalaRepository salaRepository;

    private Sala sala = new Sala();

    ArgumentCaptor<Sala> captor = ArgumentCaptor.forClass(Sala.class);

    @BeforeEach
    void setUp() {
        sala = new Sala();
        sala.setId(1L);
        sala.setNome("Sala 1");
        sala.setCapacidadeSala(6);
        sala.setTipo(TipoSala.X);
        sala.setAtivo(true);
    }

    @Test
    void deveBuscarSalaPorNome() {
        when(salaRepository.findByNome(sala.getNome())).thenReturn(Optional.of(sala));

        Sala buscada = service.BuscarPorNome(sala.getNome());

        assertEquals(sala, buscada);
        verify(salaRepository).findByNome(sala.getNome());
        verifyNoMoreInteractions(salaRepository);

    }


    @Test
    void devePermitirCriarSala() {
        SalaDTO dto = new SalaDTO(1L, "Sala 01", TipoSala.COMF0RT, 6);
        when(salaRepository.findByNome(dto.nomeSala())).thenReturn(Optional.empty());
        when(salaRepository.save(any(Sala.class))).thenAnswer(i -> i.getArgument(0));

        Sala buscada = service.criaSala(dto);

        verify(salaRepository).findByNome(dto.nomeSala());

        verify(salaRepository).save(captor.capture());
        Sala encontrada = captor.getValue();
        assertEquals(dto.nomeSala(), encontrada.getNome());
        assertEquals(dto.tipo(), encontrada.getTipo());
        assertEquals(dto.capacidadeSala(), encontrada.getCapacidadeSala());

        verifyNoMoreInteractions(salaRepository);
    }

    @Test
    void devePermitirAtualizarSala() {
        SalaDTO dto = new SalaDTO(sala.getId(), "Sala 01", TipoSala.COMF0RT, 6);
        when(salaRepository.findById(dto.id())).thenReturn(Optional.of(sala));
        when(salaRepository.save(any(Sala.class))).thenAnswer(i -> i.getArgument(0));

        Sala buscada = service.atualizaSala(sala.getId(),dto);
        verify(salaRepository).save(captor.capture());
        Sala encontrada = captor.getValue();


        assertEquals(dto.nomeSala(), encontrada.getNome());
        assertEquals(dto.tipo(), encontrada.getTipo());
        assertEquals(dto.capacidadeSala(), encontrada.getCapacidadeSala());
        verifyNoMoreInteractions(salaRepository);

    }

    @Test
    void devePermitirDesativarSala() {
        var recebeId = sala.getId();
        when(salaRepository.findById(recebeId)).thenReturn(Optional.of(sala));
        when(salaRepository.save(any(Sala.class))).thenAnswer(i -> i.getArgument(0));

        service.desativaSala(recebeId);

        assertThat(sala.isAtivo())
                .isFalse();
    }

}