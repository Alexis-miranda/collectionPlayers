package com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.repositories;

import com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.models.entity.EstadisticaJugador;
import org.springframework.data.repository.CrudRepository;

public interface EstadisticaRepository extends CrudRepository<EstadisticaJugador, Long> {
}
