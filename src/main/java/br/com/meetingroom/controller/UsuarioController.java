package br.com.meetingroom.controller;

import br.com.meetingroom.dtos.UsuarioDTO;
import br.com.meetingroom.dtos.UsuarioResponseDTO;
import br.com.meetingroom.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/usuario")
@RequiredArgsConstructor
@Validated
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> buscarUsuarios() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioDTO dto){
        UsuarioResponseDTO criarUsuario = service.criaUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarUsuario);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto){
        UsuarioResponseDTO atualizarUsuario = service.atualizaUsuario(id, dto);
        return ResponseEntity.ok(atualizarUsuario);

    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarUsuario(@PathVariable Long id){
        service.desativarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
