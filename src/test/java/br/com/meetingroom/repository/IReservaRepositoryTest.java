package br.com.meetingroom.repository;

import br.com.meetingroom.entities.Reserva;
import br.com.meetingroom.entities.Sala;
import br.com.meetingroom.entities.Usuario;
import br.com.meetingroom.enums.StatusReserva;
import br.com.meetingroom.enums.TipoSala;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class IReservaRepositoryTest {

    @Autowired
    private IReservaRepository repository;

    @Autowired
    private TestEntityManager em;

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Alex");
        usuario.setEmail("Alex@gmail.com");
        usuario.setTelefone("+314 555-5555");
        usuario.setAtivo(true);
        em.persist(usuario);

        return usuario;
    }

    @Test
    @DisplayName("Deve retornar conflito quando há sobreposição de horário (cenário de criação)")
    void deveRetornarConflitoNaCriacao() {
        Usuario usuario = criarUsuario();
        Sala sala = em.persist(new Sala("Sala 01", 10, TipoSala.X));

        Reserva existente = new Reserva();
        existente.setSala(sala);
        existente.setUsuario(usuario);
        existente.setInicioReserva(LocalDateTime.of(2026, 2, 25, 19, 42, 0));
        existente.setFimReserva(LocalDateTime.of(2026, 2, 26, 8, 0, 0));
        existente.setStatusReserva(StatusReserva.ATIVA);
        em.persist(existente);
        em.flush();

        List<Reserva> conflitos = repository.findConflitosExcluindoReserva(
                sala.getId(),
                LocalDateTime.of(2026, 2, 25, 19, 43),
                LocalDateTime.of(2026, 2, 26, 8, 1),
                null
        );

        assertThat(conflitos).hasSize(1);
        assertThat(conflitos.get(0).getId()).isEqualTo(existente.getId());
    }


    @Test
    @DisplayName("Não deve retornar conflito quando não há sobreposição de horário")
    void naoDeveRetornarConflitoSemSobreposicao() {
        Usuario usuario = criarUsuario();
        Sala sala = em.persist(new Sala("Sala 01", 10, TipoSala.X));

        Reserva existente = new Reserva();
        existente.setSala(sala);
        existente.setUsuario(usuario);
        existente.setInicioReserva(LocalDateTime.of(2026, 2, 25, 19, 42));
        existente.setFimReserva(LocalDateTime.of(2026, 2, 26, 8, 0));
        existente.setStatusReserva(StatusReserva.ATIVA);
        em.persist(existente);
        em.flush();

        List<Reserva> conflitos = repository.findConflitosExcluindoReserva(
                sala.getId(),
                LocalDateTime.of(2026, 2, 25, 13, 0),
                LocalDateTime.of(2026, 2, 25, 19, 41),
                null
        );

        assertThat(conflitos).isEmpty();
    }


    @Test
    @DisplayName("Não deve retornar conflito ao editar a própria reserva (excluindo seu próprio ID)")
    void naoDeveRetornarConflitoAoExcluirPropriaReserva() {
        Usuario usuario = criarUsuario();
        Sala sala = em.persist(new Sala("Sala 01", 10, TipoSala.X));

        Reserva existente = new Reserva();
        existente.setSala(sala);
        existente.setUsuario(usuario);
        existente.setInicioReserva(LocalDateTime.of(2026, 2, 25, 19, 42, 0));
        existente.setFimReserva(LocalDateTime.of(2026, 2, 26, 8, 0, 0));
        existente.setStatusReserva(StatusReserva.ATIVA);
        em.persist(existente);
        em.flush();

        List<Reserva> conflitos = repository.findConflitosExcluindoReserva(
                sala.getId(),
                existente.getInicioReserva(),
                existente.getFimReserva(),
                existente.getId()
        );

        assertThat(conflitos).isEmpty();
    }
}