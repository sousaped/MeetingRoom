package br.com.meetingroom.controller;

import br.com.meetingroom.dtos.ReservaDTO;
import br.com.meetingroom.entities.Reserva;
import br.com.meetingroom.entities.Sala;
import br.com.meetingroom.entities.Usuario;
import br.com.meetingroom.enums.StatusReserva;
import br.com.meetingroom.enums.TipoSala;
import br.com.meetingroom.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservaService service;

    private Reserva reserva;
    private Usuario usuario;
    private Sala sala;
    private ReservaDTO dto;

    private final LocalDateTime inicio = LocalDateTime.of(2026, 7, 10, 10, 0);
    private final LocalDateTime fim = LocalDateTime.of(2026, 7, 10, 11, 0);

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Alex");
        usuario.setEmail("alex@email.com");
        usuario.setTelefone("11999999999");
        usuario.setAtivo(true);

        sala = new Sala();
        sala.setId(1L);
        sala.setNome("Sala 1");
        sala.setCapacidadeSala(6);
        sala.setTipo(TipoSala.X);
        sala.setAtivo(true);

        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setInicioReserva(inicio);
        reserva.setFimReserva(fim);
        reserva.setStatusReserva(StatusReserva.ATIVA);
        reserva.setQtdPessoas(4);
        reserva.setUsuario(usuario);
        reserva.setSala(sala);

        dto = new ReservaDTO(inicio, fim, 4, StatusReserva.ATIVA, usuario.getId(), sala.getId());
    }

    @Test
    void deveBuscarTodasReservas() throws Exception {
        var page = new PageImpl<>(List.of(reserva), PageRequest.of(0, 10), 1);
        when(service.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reserva.getId()))
                .andExpect(jsonPath("$.content[0].statusReserva").value(reserva.getStatusReserva().toString()))
                .andExpect(jsonPath("$.content[0].qtdPessoas").value(reserva.getQtdPessoas()))
                .andExpect(jsonPath("$.content[0].salaNome").value(sala.getNome()))
                .andExpect(jsonPath("$.content[0].usuarioNome").value(usuario.getNome()));

        verify(service).findAll(any());
    }

    @Test
    void deveBuscarReservaPorId() throws Exception {
        when(service.findById(reserva.getId())).thenReturn(reserva);

        mockMvc.perform(get("/api/v1/reservas/{id}", reserva.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reserva.getId()))
                .andExpect(jsonPath("$.statusReserva").value(reserva.getStatusReserva().toString()))
                .andExpect(jsonPath("$.qtdPessoas").value(reserva.getQtdPessoas()))
                .andExpect(jsonPath("$.salaNome").value(sala.getNome()))
                .andExpect(jsonPath("$.usuarioNome").value(usuario.getNome()));

        verify(service).findById(reserva.getId());
    }

    @Test
    void deveCriarReserva() throws Exception {
        when(service.criaReserva(any(ReservaDTO.class))).thenReturn(reserva);

        String jsonBody = """
                {
                    "inicioReserva": "2026-07-10T10:00:00",
                    "fimReserva": "2026-07-10T11:00:00",
                    "qtdPessoas": 4,
                    "statusReserva": "ATIVA",
                    "usuarioId": 1,
                    "salaId": 1
                }
                """;

        mockMvc.perform(post("/api/v1/reservas/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reserva.getId()))
                .andExpect(jsonPath("$.statusReserva").value(reserva.getStatusReserva().toString()))
                .andExpect(jsonPath("$.salaNome").value(sala.getNome()))
                .andExpect(jsonPath("$.usuarioNome").value(usuario.getNome()));

        verify(service).criaReserva(any(ReservaDTO.class));
    }

    @Test
    void deveAtualizarReserva() throws Exception {
        when(service.atualizaReserva(eq(reserva.getId()), any(ReservaDTO.class))).thenReturn(reserva);

        String jsonBody = """
                {
                    "inicioReserva": "2026-07-10T10:00:00",
                    "fimReserva": "2026-07-10T11:00:00",
                    "qtdPessoas": 4,
                    "statusReserva": "ATIVA",
                    "usuarioId": 1,
                    "salaId": 1
                }
                """;

        mockMvc.perform(put("/api/v1/reservas/{id}", reserva.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reserva.getId()))
                .andExpect(jsonPath("$.statusReserva").value(reserva.getStatusReserva().toString()));

        verify(service).atualizaReserva(eq(reserva.getId()), any(ReservaDTO.class));
    }

    @Test
    void deveDeletarReserva() throws Exception {
        doNothing().when(service).cancelaReserva(reserva.getId());

        mockMvc.perform(delete("/api/v1/reservas/{id}", reserva.getId()))
                .andExpect(status().isNoContent());

        verify(service).cancelaReserva(reserva.getId());
    }
}