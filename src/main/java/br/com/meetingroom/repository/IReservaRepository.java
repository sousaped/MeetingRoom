package br.com.meetingroom.repository;

import br.com.meetingroom.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IReservaRepository extends JpaRepository<Reserva, Long> {


    @Query("""
    SELECT r FROM Reserva r
    WHERE r.sala.id = :salaId
      AND r.statusReserva = br.com.meetingroom.enums.StatusReserva.ATIVA
      AND r.inicioReserva < :fim
      AND r.fimReserva > :inicio
      AND r.id <> :reservaId
""")
    List<Reserva> findConflitosExcluindoReserva(
            @Param("salaId") Long salaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("reservaId") Long reservaId
    );

}
