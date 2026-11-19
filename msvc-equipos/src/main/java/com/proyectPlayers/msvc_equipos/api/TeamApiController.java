package com.proyectPlayers.msvc_equipos.api;

import com.proyectPlayers.msvc_equipos.api.dto.PageResponse;
import com.proyectPlayers.msvc_equipos.api.dto.TeamRequest;
import com.proyectPlayers.msvc_equipados.api.dto.TeamResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamApiController {
    private final TeamApiService service;

    public TeamApiController(TeamApiService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<TeamResponse> list(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        return service.list(page, size);
    }

    @GetMapping("/{id}")
    public TeamResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<TeamResponse> create(@Valid @RequestBody TeamRequest request) {
        TeamResponse response = service.create(request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public TeamResponse update(@PathVariable Long id, @Valid @RequestBody TeamRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
