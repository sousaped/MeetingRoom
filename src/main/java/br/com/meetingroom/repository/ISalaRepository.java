package br.com.meetingroom.repository;

import br.com.meetingroom.entities.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISalaRepository extends JpaRepository<Sala, Long> {

    Optional<Sala> findByNome(String nome);

}
