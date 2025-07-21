package com.proyectPlayers.msvc_jugadores.services;


import com.proyectPlayers.msvc_jugadores.models.entity.Jugador;

import java.util.List;
import java.util.Optional;

public interface JugadorService {

    List<Jugador> listar();
    Optional<Jugador> porId(Long id);
    Jugador guardar(Jugador jugador);
    void eliminar(Long id);
    List<Jugador> listarPorIds(Iterable<Long> ids);

}
