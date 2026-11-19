package com.proyectPlayers.msvc_equipos.services;

import com.proyectPlayers.msvc_equipos.dto.PageResponse;
import com.proyectPlayers.msvc_equipos.dto.TeamRequest;
import com.proyectPlayers.msvc_equipos.dto.TeamResponse;
import com.proyectPlayers.msvc_equipos.models.Jugador;
import com.proyectPlayers.msvc_equipos.models.entity.Equipo;

import java.util.List;
import java.util.Optional;

public interface EquipoService {

    // Modern, versioned Teams API
    PageResponse<TeamResponse> listarPaginado(int page, int size);
    TeamResponse detalleDto(Long id);
    TeamResponse crear(TeamRequest request);
    TeamResponse actualizar(Long id, TeamRequest request);

    // Existing operations retained for backwards compatibility
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
