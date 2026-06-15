package br.com.meetingroom.controller;

import br.com.meetingroom.dtos.UsuarioDTO;
import br.com.meetingroom.dtos.UsuarioResponseDTO;
import br.com.meetingroom.entities.Usuario;
import br.com.meetingroom.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Validated
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> buscarUsuarios() {
        List<Usuario> usuarios = service.findAll();
        List<UsuarioResponseDTO> response = usuarios.stream()
                .map(UsuarioResponseDTO::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        Usuario usuario = service.findById(id);
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
    }

    @PostMapping("/criar")
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioDTO dto, UriComponentsBuilder uriBuilder) {
        var usuario = service.criaUsuario(dto);
        var uri = uriBuilder.path("/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new UsuarioResponseDTO(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        Usuario usuario = service.atualizaUsuario(id, dto);
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));

    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarUsuario(@PathVariable Long id) {
        service.desativarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
