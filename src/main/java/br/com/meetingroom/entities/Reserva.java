package br.com.meetingroom.entities;

import br.com.meetingroom.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Identificador unico da reserva
    private Long id;
    // Quando irá começar a reserva
    private LocalDateTime inicioReserva;
    // Quando irá terminar a reserva
    private LocalDateTime fimReserva;
    // Qual é o Status da Reserva
    private StatusReserva statusReserva;
    // Quantidade de Pessoas que tem na reserva
    private Integer qtdPessoas;

    private Boolean cancelaReserva;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "sala_id")
    private Sala sala;

    public void cancelaReserva() {
        this.cancelaReserva = true;

    }

}
