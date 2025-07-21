package com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.controllers;

import com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.models.entity.EstadisticaJugador;
import com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.service.EstadisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class EstadisticaController {

    @Autowired
    private EstadisticaService service;

    @GetMapping
    public ResponseEntity<?> listarEstadistica() {

        if (!service.listar().isEmpty()) {
            return ResponseEntity.ok(service.listar());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {

        Optional<EstadisticaJugador> jugadorOptional = service.porId(id);

        if (jugadorOptional.isPresent()) {
            return ResponseEntity.ok(jugadorOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<?> crearEstadistica(@RequestBody EstadisticaJugador estadisticaJugador, BindingResult errores) {


        if (errores.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(estadisticaJugador));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarEstadistica(@RequestBody EstadisticaJugador estadisticaNueva, @PathVariable Long id) {

        Optional<EstadisticaJugador> jugadorOptional = service.porId(id);

        if (jugadorOptional.isPresent()) {
            EstadisticaJugador jugadorModificado = jugadorOptional.get();

            jugadorModificado.setAsistencias(estadisticaNueva.getAsistencias());
            jugadorModificado.setGoles(estadisticaNueva.getGoles());
            jugadorModificado.setMinutosJugados(estadisticaNueva.getMinutosJugados());
            jugadorModificado.setTarjetasAmarillas(estadisticaNueva.getTarjetasAmarillas());
            jugadorModificado.setTarjetasRojas(estadisticaNueva.getTarjetasRojas());
            jugadorModificado.setPartido(estadisticaNueva.getPartido());

            ResponseEntity.status(HttpStatus.OK).body(service.guardar(jugadorModificado));


        }
        return ResponseEntity.notFound().build();
    }


}
