package br.com.meetingroom.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Identificador Único do Usuário
    private Long id;
    // Nome do Usuário
    private String nome;
    // E-mail do Usuário
    @Column(unique = true)
    private String email;
    // Telefone do Usuário
    @Column(unique = true)
    private String telefone;
    // Atributo para ativar o usuário ao ser criado
    @Column(nullable = false)
    private boolean ativo = true;

    //Metodo para desativar o usuario
    public void usuarioInativo() {
        this.ativo = false;
    }

}
