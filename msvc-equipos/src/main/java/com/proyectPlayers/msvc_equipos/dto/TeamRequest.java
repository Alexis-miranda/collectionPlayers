package com.proyectPlayers.msvc_equipos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamRequest(
        @NotBlank @Size(max = 255) String name,
        @Size(max = 500) String flagUrl,
        @Size(max = 255) String stadiumName,
        @Size(max = 255) String stadiumLocation
) {}
