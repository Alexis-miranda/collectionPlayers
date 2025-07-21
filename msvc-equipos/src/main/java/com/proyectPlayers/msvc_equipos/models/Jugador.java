package com.proyectPlayers.msvc_equipos.models;

import com.proyectPlayers.msvc_equipos.models.entity.Equipo;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Jugador {

    @Id
    private Long id;

    private String nombre;

    private Integer dorsal;



    public Jugador() {

    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }




    public Integer getDorsal() {
        return dorsal;
    }

    public void setDorsal(Integer dorsal) {
        this.dorsal = dorsal;
    }


    private static final long serialVersionUID = 1L;

}
