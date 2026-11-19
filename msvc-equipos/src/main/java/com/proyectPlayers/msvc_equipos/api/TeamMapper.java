package com.proyectPlayers.msvc_equipos.api;

import com.proyectPlayers.msvc_equipos.api.dto.TeamRequest;
import com.proyectPlayers.msvc_equipos.api.dto.TeamResponse;
import com.proyectPlayers.msvc_equipos.models.entity.Equipo;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    public TeamResponse toResponse(Equipo team) {
        return new TeamResponse(team.getid(), team.getNombreEquipo(), team.getBanderaEquipo(),
                team.getNombreEstadio(), team.getUbicacionEstadio());
    }

    public void updateEntity(Equipo team, TeamRequest request) {
        team.setNombreEquipo(request.name());
        team.setBanderaEquipo(request.flagUrl());
        team.setNombreEstadio(request.stadiumName());
        team.setUbicacionEstadio(request.stadiumLocation());
    }

    public Equipo toEntity(TeamRequest request) {
        Equipo team = new Equipo();
        updateEntity(team, request);
        return team;
    }
}
