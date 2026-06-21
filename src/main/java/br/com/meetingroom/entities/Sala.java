package br.com.meetingroom.entities;

import br.com.meetingroom.dtos.SalaDTO;
import br.com.meetingroom.enums.TipoSala;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Identificador unico da sala
    private Long id;
    // Nome da sala
    @Column(unique = true, nullable = false)
    private String nome;
    // Nome do tipo da sala
    private TipoSala tipo;
    // Capacidade da sala
    @Column(nullable = false)
    private Integer capacidadeSala;
    // Verifica se a sala está ativa ou não
    private Boolean ativo = true;

    public Sala(String nome, Integer capacidadeSala, TipoSala tipo) {
        this.nome = nome;
        this.capacidadeSala = capacidadeSala;
        this.tipo = tipo;
    }


    public void salaDesativada() {
        this.ativo = false;
    }


}
