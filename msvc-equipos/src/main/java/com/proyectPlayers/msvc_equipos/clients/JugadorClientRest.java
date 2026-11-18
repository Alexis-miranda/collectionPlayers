package com.proyectPlayers.msvc_equipos.clients;

import com.proyectPlayers.msvc_equipos.models.Jugador;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange
public interface JugadorClientRest {

    @GetExchange("/{id}")
    Jugador detalle(@PathVariable("id") Long id);

    @PostExchange("/")
    Jugador crear(@RequestBody Jugador jugador);

    @GetExchange("/jugadores-por-equipo")
    List<Jugador> obtenerJugadorPorEquipo(@RequestParam("ids") List<Long> ids);
}
