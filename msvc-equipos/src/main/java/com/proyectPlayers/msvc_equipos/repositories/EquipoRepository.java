package com.proyectPlayers.msvc_equipos.repositories;

import com.proyectPlayers.msvc_equipos.models.entity.Equipo;
import com.proyectPlayers.msvc_equipos.models.entity.EquipoJugador;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    @Transactional
    @Modifying
    @Query("delete from EquipoJugador ej where ej.jugadorId = ?1")
    void eliminarEquipoJugadorPorId(Long id);
}
