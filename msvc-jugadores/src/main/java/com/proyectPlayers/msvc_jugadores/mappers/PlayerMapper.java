package com.proyectPlayers.msvc_jugadores.mappers;

import com.proyectPlayers.msvc_jugadores.models.dto.request.PlayerRequest;
import com.proyectPlayers.msvc_jugadores.models.dto.response.PlayerResponse;
import com.proyectPlayers.msvc_jugadores.models.entity.Jugador;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public Jugador toEntity(PlayerRequest request) {
        Jugador jugador = new Jugador();
        updateEntity(jugador, request);
        return jugador;
    }

    public void updateEntity(Jugador jugador, PlayerRequest request) {
        jugador.setNombre(request.nombre().trim());
        jugador.setApellidos(clean(request.apellidos()));
        jugador.setFechaNacimiento(request.fechaNacimiento());
        jugador.setNacionalidad(clean(request.nacionalidad()));
        jugador.setPosicion(clean(request.posicion()));
        jugador.setPiePreferido(clean(request.piePreferido()));
        jugador.setAlturaCm(request.alturaCm());
        jugador.setDorsal(request.dorsal());
        jugador.setActivo(request.activo() == null ? true : request.activo());
    }

    public PlayerResponse toResponse(Jugador jugador) {
        String fullName = jugador.getApellidos() == null || jugador.getApellidos().isBlank()
                ? jugador.getNombre()
                : jugador.getNombre() + " " + jugador.getApellidos();
        return new PlayerResponse(
                jugador.getId(), jugador.getNombre(), jugador.getApellidos(), fullName,
                jugador.getFechaNacimiento(), jugador.getNacionalidad(), jugador.getPosicion(),
                jugador.getPiePreferido(), jugador.getAlturaCm(), jugador.getDorsal(),
                jugador.getActivo(), jugador.getCreadoEn(), jugador.getActualizadoEn()
        );
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
