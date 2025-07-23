package com.proyectPlayers.msvc_equipos.clients;

import com.proyectPlayers.msvc_equipos.models.Jugador;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="msvc-jugadores", url="host.docker.internal:8005")
public interface JugadorClientRest {
    @GetMapping("/{id}")
    Jugador detalle(@PathVariable Long id);

    @PostMapping("/")
    Jugador crear(@RequestBody Jugador jugador);

    @GetMapping("/jugadores-por-equipo")
    List<Jugador> obtenerJugadorPorEquipo(@RequestParam Iterable<Long> ids);
}
