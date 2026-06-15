package br.com.meetingroom.controller;

import br.com.meetingroom.dtos.SalaDTO;
import br.com.meetingroom.dtos.SalaResponseDTO;
import br.com.meetingroom.entities.Sala;
import br.com.meetingroom.service.SalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salas")
@RequiredArgsConstructor
@Validated
public class SalaController {

    private final SalaService service;

    @GetMapping
    public ResponseEntity<List<SalaResponseDTO>> buscarTodasSalas() {
        List<Sala> salas = service.buscarTodasSalas();
        List<SalaResponseDTO> response = salas.stream()
                .map(SalaResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{nome}")
    public ResponseEntity<SalaResponseDTO> buscarSalaPorNome(@PathVariable String nome) {
        Sala sala = service.BuscarPorNome(nome);
        return ResponseEntity.ok(new SalaResponseDTO(sala));
    }

    @PostMapping("/criar")
    public ResponseEntity<SalaResponseDTO> criaSala(@RequestBody @Valid SalaDTO dto, UriComponentsBuilder uriBuilder) {
        var sala = service.criaSala(dto);
        var uri = uriBuilder.path("/{id}").buildAndExpand(sala.getId()).toUri();
        return ResponseEntity.created(uri).body(new SalaResponseDTO(sala));

    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaResponseDTO> atualizaSala(@PathVariable Long id, @RequestBody SalaDTO dto) {
        Sala sala = service.atualizaSala(id, dto);
        return ResponseEntity.ok(new SalaResponseDTO(sala));

    }


    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativaSala(@PathVariable Long id) {
        service.desativaSala(id);
        return ResponseEntity.noContent().build();
    }


}
