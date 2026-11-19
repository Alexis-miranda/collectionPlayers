package com.proyectPlayers.msvc_jugadores.services;

import com.proyectPlayers.msvc_jugadores.models.dto.request.PlayerRequest;
import com.proyectPlayers.msvc_jugadores.models.dto.response.PageResponse;
import com.proyectPlayers.msvc_jugadores.models.dto.response.PlayerResponse;

import java.util.List;

public interface JugadorService {
    PageResponse<PlayerResponse> listar(int page, int size);
    PlayerResponse porId(Long id);
    PlayerResponse crear(PlayerRequest request);
    PlayerResponse actualizar(Long id, PlayerRequest request);
    void eliminar(Long id);
    List<PlayerResponse> listarPorIds(List<Long> ids);
}
