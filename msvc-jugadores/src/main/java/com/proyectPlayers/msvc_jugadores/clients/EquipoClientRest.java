package com.proyectPlayers.msvc_jugadores.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="msvc-equipos", url = "localhost:8006")
public interface EquipoClientRest {

    @DeleteMapping("/eliminar-equipo-jugador/{id}")
    void eliminarEquipoJugadorPorId(@PathVariable Long id);
}
