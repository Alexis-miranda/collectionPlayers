package com.proyectPlayers.msvc_jugadores.models.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PlayerRequest(
        @NotBlank @Size(max = 80) String nombre,
        @Size(max = 80) String apellidos,
        @Past LocalDate fechaNacimiento,
        @Size(max = 60) String nacionalidad,
        @Pattern(regexp = "GOALKEEPER|DEFENDER|MIDFIELDER|FORWARD", message = "must be GOALKEEPER, DEFENDER, MIDFIELDER or FORWARD")
        String posicion,
        @Pattern(regexp = "RIGHT|LEFT|BOTH", message = "must be RIGHT, LEFT or BOTH")
        String piePreferido,
        @Min(100) @Max(230) Integer alturaCm,
        @Min(1) @Max(99) Integer dorsal,
        Boolean activo
) {}
