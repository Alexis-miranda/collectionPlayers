package com.proyectPlayers.msvc_equipos.models.entity;

import com.proyectPlayers.msvc_equipos.models.Jugador;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Equipos")
public class Equipo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String banderaEquipo;
    private String nombreEquipo;
    private String nombreEstadio;
    private String ubicacionEstadio;



    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "equipo_id")
    private List<EquipoJugador> equipoJugadorList;

    @Transient
    private List<Jugador> jugadoresList;

    public List<EquipoJugador> getEquipoJugadorList() {
        return equipoJugadorList;
    }

    public List<Jugador> getJugadoresList() {
        return jugadoresList;
    }

    public void setJugadoresList(List<Jugador> jugadoresList) {
        this.jugadoresList = jugadoresList;
    }

    public void setEquipoJugadorList(List<EquipoJugador> equipoJugadorList) {
        this.equipoJugadorList = equipoJugadorList;
    }

    public Equipo() {
        equipoJugadorList = new ArrayList<>();
        jugadoresList = new ArrayList<>();
    }


    public void addequipoJugadorList(EquipoJugador cursoUsuario) {
        equipoJugadorList.add(cursoUsuario);
    }

    public void removeEquipoJugadores(EquipoJugador cursoUsuario) {
        equipoJugadorList.remove(cursoUsuario);
    }


    public String getBanderaEquipo() {
        return banderaEquipo;
    }

    public void setBanderaEquipo(String banderaEquipo) {
        this.banderaEquipo = banderaEquipo;
    }

    public Long getid() {
        return id;
    }

    public String getNombreEstadio() {
        return nombreEstadio;
    }

    public void setNombreEstadio(String nombreEstadio) {
        this.nombreEstadio = nombreEstadio;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getUbicacionEstadio() {
        return ubicacionEstadio;
    }

    public void setUbicacionEstadio(String ubicacionEstadio) {
        this.ubicacionEstadio = ubicacionEstadio;
    }
}
