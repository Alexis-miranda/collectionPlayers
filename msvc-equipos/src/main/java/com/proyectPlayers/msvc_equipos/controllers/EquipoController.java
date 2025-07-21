package com.proyectPlayers.msvc_equipos.controllers;

import com.proyectPlayers.msvc_equipos.models.Jugador;
import com.proyectPlayers.msvc_equipos.models.entity.Equipo;
import com.proyectPlayers.msvc_equipos.services.EquipoService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class EquipoController {


    @Autowired
    private EquipoService service;

    @GetMapping
    public List<Equipo> listarEquipo() {

        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalleEquipo(@PathVariable Long id) {
        Optional<Equipo> optionalEquipo = service.porIdConJugadores(id);
        if (optionalEquipo.isPresent()) {
            return ResponseEntity.ok(optionalEquipo.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEquipo(@PathVariable Long id) {
        Optional<Equipo> optionalEquipo = service.porId(id);
        if (optionalEquipo.isPresent()) {
            service.eliminar(optionalEquipo.get().getid());

            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<?> CrearEquipo(@Valid @RequestBody Equipo equipo, BindingResult result) {

        if (result.hasErrors()) {
            return validar(result);
        }
Equipo equipoBd = service.guardar(equipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipoBd);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> EditarEquipo(@Valid @RequestBody Equipo equipo, @PathVariable Long id) {

        Optional<Equipo> equipoOptional = service.porId(id);
        if (equipoOptional.isPresent()) {
            Equipo equipoModificado = equipoOptional.get();
            equipoModificado.setBanderaEquipo(equipo.getBanderaEquipo());
            equipoModificado.setNombreEstadio(equipo.getNombreEstadio());
            equipoModificado.setNombreEquipo(equipo.getNombreEquipo());
            equipoModificado.setUbicacionEstadio(equipo.getUbicacionEstadio());


            return ResponseEntity.ok(service.guardar(equipoModificado));
        }

        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

//desde aquí

    @PutMapping("/asignar-jugador/{equipoId}")
    public ResponseEntity<?> asignarJugador(@RequestBody Jugador jugador, @PathVariable Long equipoId) {
        Optional<Jugador> o;
        try {

            o = service.asignarJugador(jugador, equipoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el jugador por " +
                            "el id o error en la comunicacion: " + e.getMessage()));
        }
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-jugador/{equipoId}")
    public ResponseEntity<?> crearJugador(@RequestBody Jugador usuario, @PathVariable Long equipoId) {
        Optional<Jugador> o;
        try {
            o = service.crearJugador(usuario, equipoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No se pudo crear el jugador " +
                            "o error en la comunicacion: " + e.getMessage()));
        }
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-jugador/{equipoId}")
    public ResponseEntity<?> eliminarJugador(@RequestBody Jugador usuario, @PathVariable Long equipoId) {
        Optional<Jugador> o;
        try {
            o = service.eliminarJugador(usuario, equipoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el jugador por " +
                            "el id o error en la comunicacion: " + e.getMessage()));
        }
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-equipo-jugador/{id}")
    public ResponseEntity<?> eliminarEquipoJugadorPorId(@PathVariable Long id){
        service.eliminarEquipoJugadorPorId(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }


}
