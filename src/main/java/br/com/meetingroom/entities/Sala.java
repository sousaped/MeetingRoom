package br.com.meetingroom.entities;

import br.com.meetingroom.enums.TipoSala;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String nomeSala;
    // Nome do tipo da sala
    private TipoSala tipo;
    // Capacidade da sala
    private Integer capacidadeSala;
    // Verifica se a sala está ativa ou não
    private Boolean ativo = true;


    public void salaDesativada(){
        this.ativo = false;
    }


}
