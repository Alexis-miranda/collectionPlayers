package com.proyectPlayers.msvc_jugadores.clients;

import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.bind.annotation.PathVariable;

@HttpExchange
public interface EquipoClientRest {

    @DeleteExchange("/eliminar-equipo-jugador/{id}")
    void eliminarEquipoJugadorPorId(@PathVariable("id") Long id);
}
