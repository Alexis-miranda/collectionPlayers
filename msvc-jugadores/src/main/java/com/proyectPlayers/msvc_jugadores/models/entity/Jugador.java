package com.proyectPlayers.msvc_jugadores.models.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "jugadores", indexes = {
        @Index(name = "idx_jugadores_nombre", columnList = "nombre")
})
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(length = 80)
    private String apellidos;

    @Column
    private LocalDate fechaNacimiento;

    @Column(length = 60)
    private String nacionalidad;

    @Column(length = 20)
    private String posicion;

    @Column(length = 20)
    private String piePreferido;

    @Column
    private Integer alturaCm;

    @Column
    private Integer dorsal;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false, updatable = false)
    private Instant creadoEn;

    @Column(nullable = false)
    private Instant actualizadoEn;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        creadoEn = now;
        actualizadoEn = now;
        if (activo == null) activo = true;
    }

    @PreUpdate
    void onUpdate() {
        actualizadoEn = Instant.now();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }
    public String getPiePreferido() { return piePreferido; }
    public void setPiePreferido(String piePreferido) { this.piePreferido = piePreferido; }
    public Integer getAlturaCm() { return alturaCm; }
    public void setAlturaCm(Integer alturaCm) { this.alturaCm = alturaCm; }
    public Integer getDorsal() { return dorsal; }
    public void setDorsal(Integer dorsal) { this.dorsal = dorsal; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Instant getCreadoEn() { return creadoEn; }
    public Instant getActualizadoEn() { return actualizadoEn; }
}
