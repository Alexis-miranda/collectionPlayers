package com.proyectPlayers.msvc_equipos.api;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(Long id) {
        super("Team not found: " + id);
    }
}
