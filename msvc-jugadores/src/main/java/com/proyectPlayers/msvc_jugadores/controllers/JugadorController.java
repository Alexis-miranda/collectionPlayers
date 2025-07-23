package com.proyectPlayers.msvc_jugadores.controllers;

import com.proyectPlayers.msvc_jugadores.models.entity.Jugador;
import com.proyectPlayers.msvc_jugadores.services.JugadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class JugadorController {

    @Autowired
    private JugadorService service;


    @GetMapping
    public ResponseEntity<List<Jugador>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Jugador> usuarioOptional = service.porId(id);
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Jugador jugador, BindingResult result) {

        if (result.hasErrors()) {
            return validar(result);
        }
Jugador jugadorDb = service.guardar(jugador);
        return ResponseEntity.status(HttpStatus.CREATED).body(jugadorDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Jugador jugador, BindingResult result, @PathVariable Long id) {

        if (result.hasErrors()) {
            return validar(result);
        }

        Optional<Jugador> o = service.porId(id);
        if (o.isPresent()) {
            Jugador usuarioDb = o.get();

            usuarioDb.setNombre(jugador.getNombre());
           // usuarioDb.setEquipo(jugador.getEquipo());
            usuarioDb.setDorsal(jugador.getDorsal());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Jugador> o = service.porId(id);
        if (o.isPresent()) {
            service.eliminar(o.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
