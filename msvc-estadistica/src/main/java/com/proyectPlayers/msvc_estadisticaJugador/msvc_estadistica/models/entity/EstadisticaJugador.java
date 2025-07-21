package com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.models.entity;

import com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.models.*;
import jakarta.persistence.*;

@Entity
@Table(name = "estadisticasJugador")
public class EstadisticaJugador {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.models.entity.Jugador jugador;

    @ManyToOne
    private Partido partido;

    private int goles;
    private int asistencias;
    private int tarjetasAmarillas;
    private int tarjetasRojas;
    private int minutosJugados;

    public com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.models.entity.Jugador getJugador() {
        return jugador;
    }

    public void setJugador(com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.models.entity.Jugador jugador) {
        this.jugador = jugador;
    }

    public int getGoles() {
        return goles;
    }

    public void setGoles(int goles) {
        this.goles = goles;
    }

    public int getMinutosJugados() {
        return minutosJugados;
    }

    public void setMinutosJugados(int minutosJugados) {
        this.minutosJugados = minutosJugados;
    }

    public int getTarjetasRojas() {
        return tarjetasRojas;
    }

    public void setTarjetasRojas(int tarjetasRojas) {
        this.tarjetasRojas = tarjetasRojas;
    }

    public int getTarjetasAmarillas() {
        return tarjetasAmarillas;
    }

    public void setTarjetasAmarillas(int tarjetasAmarillas) {
        this.tarjetasAmarillas = tarjetasAmarillas;
    }

    public int getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(int asistencias) {
        this.asistencias = asistencias;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Long getId() {
        return id;
    }
}
