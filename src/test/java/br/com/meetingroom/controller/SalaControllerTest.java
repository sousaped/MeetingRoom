package br.com.meetingroom.controller;

import br.com.meetingroom.dtos.SalaDTO;
import br.com.meetingroom.entities.Sala;
import br.com.meetingroom.enums.TipoSala;
import br.com.meetingroom.service.SalaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SalaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SalaService service;

    private Sala sala;
    private SalaDTO dto;

    @BeforeEach
    void setUp() {
        sala = new Sala();
        sala.setId(1L);
        sala.setNome("Sala 1");
        sala.setCapacidadeSala(6);
        sala.setTipo(TipoSala.X);
        sala.setAtivo(true);

        dto = new SalaDTO(1L, "Sala 1", TipoSala.X, 6);
    }

    @Test
    void deveBuscarTodasSalas() throws Exception {
        when(service.buscarTodasSalas()).thenReturn(List.of(sala));

        mockMvc.perform(get("/api/v1/salas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeSala").value(sala.getNome()))
                .andExpect(jsonPath("$[0].capacidadeSala").value(sala.getCapacidadeSala()));

        verify(service).buscarTodasSalas();
    }

    @Test
    void deveBuscarSalaPorNome() throws Exception {
        when(service.BuscarPorNome(sala.getNome())).thenReturn(sala);

        mockMvc.perform(get("/api/v1/salas/{nome}", sala.getNome())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeSala").value(sala.getNome()))
                .andExpect(jsonPath("$.capacidadeSala").value(sala.getCapacidadeSala()));

        verify(service).BuscarPorNome(sala.getNome());
    }

    @Test
    void deveCriarSala() throws Exception {
        when(service.criaSala(any(SalaDTO.class))).thenReturn(sala);

        mockMvc.perform(post("/api/v1/salas/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nomeSala\":\"Sala 1\",\"tipo\":\"X\",\"capacidadeSala\":6}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeSala").value(sala.getNome()))
                .andExpect(jsonPath("$.capacidadeSala").value(sala.getCapacidadeSala()));

        verify(service).criaSala(any(SalaDTO.class));
    }

    @Test
    void deveAtualizarSala() throws Exception {
        when(service.atualizaSala(eq(sala.getId()), any(SalaDTO.class))).thenReturn(sala);

        mockMvc.perform(put("/api/v1/salas/{id}", sala.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nomeSala\":\"Sala 1\",\"tipo\":\"X\",\"capacidadeSala\":6}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeSala").value(sala.getNome()))
                .andExpect(jsonPath("$.capacidadeSala").value(sala.getCapacidadeSala()));

        verify(service).atualizaSala(eq(sala.getId()), any(SalaDTO.class));
    }

    @Test
    void deveDesativarSala() throws Exception {
        doNothing().when(service).desativaSala(sala.getId());

        mockMvc.perform(patch("/api/v1/salas/{id}/desativar", sala.getId()))
                .andExpect(status().isNoContent());

        verify(service).desativaSala(sala.getId());
    }
}