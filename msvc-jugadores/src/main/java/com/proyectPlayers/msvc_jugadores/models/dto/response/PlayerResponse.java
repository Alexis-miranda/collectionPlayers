package com.proyectPlayers.msvc_jugadores.models.dto.response;

import java.time.Instant;
import java.time.LocalDate;

public record PlayerResponse(
        Long id,
        String nombre,
        String apellidos,
        String nombreCompleto,
        LocalDate fechaNacimiento,
        String nacionalidad,
        String posicion,
        String piePreferido,
        Integer alturaCm,
        Integer dorsal,
        Boolean activo,
        Instant creadoEn,
        Instant actualizadoEn
) {}
