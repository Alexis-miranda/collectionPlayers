package com.proyectPlayers.msvc_equipos.repositories;

import com.proyectPlayers.msvc_equipos.models.entity.Equipo;
import com.proyectPlayers.msvc_equipos.models.entity.EquipoJugador;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EquipoRepository extends CrudRepository<Equipo, Long> {

 // @Modifying
 //@Query("delete from equipoJugador ej where ej.jugadorId=?1")
 //   void eliminarEquipoJugadorPorId(Long id);
 @Transactional
 @Modifying
 @Query("delete from EquipoJugador ej where ej.jugadorId = ?1")
 void eliminarEquipoJugadorPorId(Long id);

}
