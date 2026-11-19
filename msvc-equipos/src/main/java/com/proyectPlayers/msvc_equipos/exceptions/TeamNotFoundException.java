package com.proyectPlayers.msvc_equipos.exceptions;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(Long id) {
        super("Team not found: " + id);
    }
}
