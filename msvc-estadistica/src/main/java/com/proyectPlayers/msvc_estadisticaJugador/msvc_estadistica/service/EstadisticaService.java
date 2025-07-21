package com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.service;

import com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.models.entity.EstadisticaJugador;

import java.util.List;
import java.util.Optional;

public interface EstadisticaService  {

    /**
     * listar todas las estdisticas de un  jugador
     * @return
     */
    List<EstadisticaJugador> listar();

    /**
     * listar por id de partido
     * @param id
     * @return
     */
    Optional<EstadisticaJugador> porId(Long id);

    /**
     * guardar una nueva estadistica
     * @param jugador
     * @return
     */
    EstadisticaJugador guardar(EstadisticaJugador jugador);

    /**
     * eliminar
     * @param id
     */
    void eliminar(Long id);

    /**
     *
     * @param id
     * @return
     */
    Optional<EstadisticaJugador> Modificarestadistica(Long id);


}
