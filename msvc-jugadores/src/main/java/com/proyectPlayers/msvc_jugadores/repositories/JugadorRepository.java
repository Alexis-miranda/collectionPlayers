package com.proyectPlayers.msvc_jugadores.repositories;

import com.proyectPlayers.msvc_jugadores.models.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    List<Jugador> findAllByIdIn(Collection<Long> ids);
}
