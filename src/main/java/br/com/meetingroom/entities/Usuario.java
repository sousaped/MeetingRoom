package br.com.meetingroom.entities;

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
    private Boolean ativo = true;

    public Usuario(Usuario usuario) {
    }


    //Metodo para desativar o usuario
    public void usuarioInativo() {
        this.ativo = false;
    }

}
