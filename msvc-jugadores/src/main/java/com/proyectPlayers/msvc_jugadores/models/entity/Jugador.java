package com.proyectPlayers.msvc_jugadores.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "jugadores")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private Integer dorsal;

    //@ManyToOne
    //@JoinColumn(name = "equipo_id")
    //private Equipo equipo;

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
