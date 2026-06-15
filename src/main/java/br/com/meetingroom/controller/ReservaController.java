package br.com.meetingroom.controller;

import br.com.meetingroom.dtos.ReservaDTO;
import br.com.meetingroom.dtos.ReservaResponseDTO;
import br.com.meetingroom.entities.Reserva;
import br.com.meetingroom.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
@Validated
public class ReservaController {

    private final ReservaService service;

    @GetMapping
    public ResponseEntity<Page<ReservaResponseDTO>> buscarTodasReservas(
            @PageableDefault(size = 10, sort = "inicioReserva") Pageable pageable) {
        Page<ReservaResponseDTO> response = service.findAll(pageable)
                .map(ReservaResponseDTO::new);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarReservas(@PathVariable Long id) {
        Reserva reserva = service.findById(id);
        return ResponseEntity.ok(new ReservaResponseDTO(reserva));

    }

    @PostMapping("/criar")
    public ResponseEntity<ReservaResponseDTO> criarReserva(@RequestBody @Valid ReservaDTO dto, UriComponentsBuilder uriBuilder) {
        var reserva = service.criaReserva(dto);
        var uri = uriBuilder.path("/{id}").buildAndExpand(reserva.getId()).toUri();
        return ResponseEntity.created(uri).body(new ReservaResponseDTO(reserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> atualizarReserva(@PathVariable Long id, @Valid @RequestBody ReservaDTO dto) {
        Reserva reserva = service.atualizaReserva(id, dto);
        return ResponseEntity.ok(new ReservaResponseDTO(reserva));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReserva(@PathVariable Long id) {
        service.cancelaReserva(id);
        return ResponseEntity.noContent().build();
    }

}
