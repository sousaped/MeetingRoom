package br.com.meetingroom.controller;

import br.com.meetingroom.dtos.ReservaDTO;
import br.com.meetingroom.dtos.ReservaResponseDTO;
import br.com.meetingroom.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/reserva")
@RequiredArgsConstructor
@Validated
public class ReservaController {

    private final ReservaService service;

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> buscarReservas() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarReservas(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));

    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> criarReserva(@Valid @RequestBody ReservaDTO dto) {
        ReservaResponseDTO criarReserva = service.criaReserva(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarReserva);
    }

    @PutMapping("/{id}/reserva")
    public ResponseEntity<ReservaResponseDTO> atualizarReserva(@PathVariable Long id, @Valid @RequestBody ReservaDTO dto) {
        ReservaResponseDTO atualizaReserva = service.atualizaReserva(id, dto);
        return ResponseEntity.ok(atualizaReserva);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReserva(@PathVariable Long id) {
        service.cancelaReserva(id);
        return ResponseEntity.noContent().build();
    }

}
