package br.com.meetingroom.controller;

import br.com.meetingroom.dtos.UsuarioDTO;
import br.com.meetingroom.entities.Usuario;
import br.com.meetingroom.service.UsuarioService;
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
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService service;

    private Usuario usuario;
    private UsuarioDTO dto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Alex");
        usuario.setEmail("alex@email.com");
        usuario.setTelefone("11999999999");
        usuario.setAtivo(true);

        dto = new UsuarioDTO(1L, "Alex", "alex@email.com", "11999999999");
    }

    @Test
    void deveBuscarTodosUsuarios() throws Exception {
        when(service.findAll()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(usuario.getNome()))
                .andExpect(jsonPath("$[0].email").value(usuario.getEmail()))
                .andExpect(jsonPath("$[0].telefone").value(usuario.getTelefone()));

        verify(service).findAll();
    }

    @Test
    void deveBuscarUsuarioPorEmail() throws Exception {
        when(service.findByEmail(usuario.getEmail())).thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuarios/{email}", usuario.getEmail())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(usuario.getNome()))
                .andExpect(jsonPath("$.email").value(usuario.getEmail()))
                .andExpect(jsonPath("$.telefone").value(usuario.getTelefone()));

        verify(service).findByEmail(usuario.getEmail());
    }

    @Test
    void deveCriarUsuario() throws Exception {
        when(service.criaUsuario(any(UsuarioDTO.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/v1/usuarios/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nome\":\"Alex\",\"email\":\"alex@email.com\",\"telefone\":\"11999999999\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(usuario.getNome()))
                .andExpect(jsonPath("$.email").value(usuario.getEmail()))
                .andExpect(jsonPath("$.telefone").value(usuario.getTelefone()));

        verify(service).criaUsuario(any(UsuarioDTO.class));
    }

    @Test
    void deveAtualizarUsuario() throws Exception {
        when(service.atualizaUsuario(eq(usuario.getId()), any(UsuarioDTO.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/v1/usuarios/{id}", usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nome\":\"Alex\",\"email\":\"alex@email.com\",\"telefone\":\"11999999999\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(usuario.getNome()))
                .andExpect(jsonPath("$.email").value(usuario.getEmail()))
                .andExpect(jsonPath("$.telefone").value(usuario.getTelefone()));

        verify(service).atualizaUsuario(eq(usuario.getId()), any(UsuarioDTO.class));
    }

    @Test
    void deveDesativarUsuario() throws Exception {
        doNothing().when(service).desativarUsuario(usuario.getId());

        mockMvc.perform(patch("/api/v1/usuarios/{id}/desativar", usuario.getId()))
                .andExpect(status().isNoContent());

        verify(service).desativarUsuario(usuario.getId());
    }
}