package com.proyectPlayers.msvc_equipos.dto;

public record TeamResponse(
        Long id,
        String name,
        String flagUrl,
        String stadiumName,
        String stadiumLocation
) {}
