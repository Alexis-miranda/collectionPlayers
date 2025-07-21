package com.proyectPlayers.msvc_equipos.services;

import com.proyectPlayers.msvc_equipos.models.Jugador;
import com.proyectPlayers.msvc_equipos.models.entity.Equipo;

import java.util.List;
import java.util.Optional;

public interface EquipoService {

    List<Equipo> listar();

    Optional<Equipo> porId(Long id);

    Equipo guardar(Equipo equipo);

    void eliminar(Long id);
    void eliminarEquipoJugadorPorId(Long id);

    Optional<Equipo> porIdConJugadores(Long id);

    Optional<Jugador> asignarJugador(Jugador jugador, Long equipoId);
    Optional<Jugador> crearJugador(Jugador jugador, Long equipoId);
    Optional<Jugador> eliminarJugador(Jugador jugador, Long equipoId);

}
