package com.proyectPlayers.msvc_equipos.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "equipoJugador")
public class EquipoJugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="jugadorId", unique = true)
    private Long jugadorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }

    public Long getJugadorId() {
        return jugadorId;
    }

    public EquipoJugador() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EquipoJugador)) {
            return false;
        }
        EquipoJugador o = (EquipoJugador) obj;
        return this.jugadorId != null && this.jugadorId.equals(o.jugadorId);
    }
}
