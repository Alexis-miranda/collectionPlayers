package com.proyectPlayers.msvc_jugadores.controllers;

import com.proyectPlayers.msvc_jugadores.models.dto.request.PlayerRequest;
import com.proyectPlayers.msvc_jugadores.models.dto.response.PageResponse;
import com.proyectPlayers.msvc_jugadores.models.dto.response.PlayerResponse;
import com.proyectPlayers.msvc_jugadores.services.JugadorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping({"/api/v1/players", ""})
public class JugadorController {

    private final JugadorService service;

    public JugadorController(JugadorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PageResponse<PlayerResponse>> listar(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return ResponseEntity.ok(service.listar(page, size));
    }

    // Kept as a compatibility contract for the existing Teams service.
    @GetMapping({"/by-ids", "/jugadores-por-equipo"})
    public ResponseEntity<List<PlayerResponse>> listarPorIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(service.listarPorIds(ids));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> detalle(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(service.porId(id));
    }

    @PostMapping
    public ResponseEntity<PlayerResponse> crear(@Valid @RequestBody PlayerRequest request) {
        PlayerResponse created = service.crear(request);
        return ResponseEntity.created(URI.create("/api/v1/players/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponse> actualizar(
            @PathVariable @Min(1) Long id, @Valid @RequestBody PlayerRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @Min(1) Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
