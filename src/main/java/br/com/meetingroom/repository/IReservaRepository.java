package br.com.meetingroom.repository;

import br.com.meetingroom.entities.Reserva;
import br.com.meetingroom.entities.Usuario;
import br.com.meetingroom.enums.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioIdAndInicioReservaAndStatusReserva(Long usuarioId, LocalDateTime inicioReserva, StatusReserva statusReserva);

}
