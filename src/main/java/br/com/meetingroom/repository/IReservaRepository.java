package br.com.meetingroom.repository;

import br.com.meetingroom.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReservaRepository extends JpaRepository<Reserva, Long> {
}
