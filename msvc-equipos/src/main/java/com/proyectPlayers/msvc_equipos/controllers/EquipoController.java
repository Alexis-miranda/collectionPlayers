package com.proyectPlayers.msvc_equipos.controllers;

import com.proyectPlayers.msvc_equipos.dto.PageResponse;
import com.proyectPlayers.msvc_equipos.dto.TeamRequest;
import com.proyectPlayers.msvc_equipos.dto.TeamResponse;
import com.proyectPlayers.msvc_equipos.models.Jugador;
import com.proyectPlayers.msvc_equipos.models.entity.Equipo;
import com.proyectPlayers.msvc_equipos.services.EquipoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

@RestController
public class EquipoController {

    private final EquipoService service;

    public EquipoController(EquipoService service) {
        this.service = service;
    }

    // Modern, versioned Teams API

    @GetMapping("/api/v1/teams")
    public PageResponse<TeamResponse> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.listarPaginado(page, size);
    }

    @GetMapping("/api/v1/teams/{id}")
    public TeamResponse detalleDto(@PathVariable Long id) {
        return service.detalleDto(id);
    }

    @PostMapping("/api/v1/teams")
    public ResponseEntity<TeamResponse> crear(@Valid @RequestBody TeamRequest request) {
        TeamResponse response = service.crear(request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/api/v1/teams/{id}")
    public TeamResponse actualizar(@PathVariable Long id, @Valid @RequestBody TeamRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/api/v1/teams/{id}")
    public ResponseEntity<Void> eliminarVersionado(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Existing legacy endpoints retained during the migration

    @GetMapping
    public List<Equipo> listarEquipo() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalleEquipo(@PathVariable Long id) {
        Optional<Equipo> optionalEquipo = service.porIdConJugadores(id);
        return optionalEquipo.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEquipo(@PathVariable Long id) {
        if (service.porId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> crearEquipoLegacy(@Valid @RequestBody Equipo equipo, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(equipo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarEquipoLegacy(@Valid @RequestBody Equipo equipo, @PathVariable Long id) {
        Optional<Equipo> equipoOptional = service.porId(id);
        if (equipoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Equipo equipoModificado = equipoOptional.get();
        equipoModificado.setBanderaEquipo(equipo.getBanderaEquipo());
        equipoModificado.setNombreEstadio(equipo.getNombreEstadio());
        equipoModificado.setNombreEquipo(equipo.getNombreEquipo());
        equipoModificado.setUbicacionEstadio(equipo.getUbicacionEstadio());

        return ResponseEntity.ok(service.guardar(equipoModificado));
    }

    @PutMapping("/asignar-jugador/{equipoId}")
    public ResponseEntity<?> asignarJugador(@RequestBody Jugador jugador, @PathVariable Long equipoId) {
        return ejecutarOperacionJugador(() -> service.asignarJugador(jugador, equipoId), HttpStatus.CREATED);
    }

    @PostMapping("/crear-jugador/{equipoId}")
    public ResponseEntity<?> crearJugador(@RequestBody Jugador jugador, @PathVariable Long equipoId) {
        return ejecutarOperacionJugador(() -> service.crearJugador(jugador, equipoId), HttpStatus.CREATED);
    }

    @DeleteMapping("/eliminar-jugador/{equipoId}")
    public ResponseEntity<?> eliminarJugador(@RequestBody Jugador jugador, @PathVariable Long equipoId) {
        return ejecutarOperacionJugador(() -> service.eliminarJugador(jugador, equipoId), HttpStatus.OK);
    }

    @DeleteMapping("/eliminar-equipo-jugador/{id}")
    public ResponseEntity<?> eliminarEquipoJugadorPorId(@PathVariable Long id) {
        service.eliminarEquipoJugadorPorId(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> ejecutarOperacionJugador(
            java.util.function.Supplier<Optional<Jugador>> operation,
            HttpStatus successStatus) {
        try {
            Optional<Jugador> result = operation.get();
            return result.<ResponseEntity<?>>map(jugador -> ResponseEntity.status(successStatus).body(jugador))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Collections.singletonMap(
                            "mensaje",
                            "Error al comunicarse con el servicio de jugadores: " + ex.getMessage()));
        }
    }

    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error ->
                errores.put(error.getField(),
                        "El campo " + error.getField() + " " + error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }
}
