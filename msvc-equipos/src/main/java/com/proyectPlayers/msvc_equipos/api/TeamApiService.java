package com.proyectPlayers.msvc_equipos.api;

import com.proyectPlayers.msvc_equipos.api.dto.PageResponse;
import com.proyectPlayers.msvc_equipos.api.dto.TeamRequest;
import com.proyectPlayers.msvc_equipos.api.dto.TeamResponse;
import com.proyectPlayers.msvc_equipos.models.entity.Equipo;
import com.proyectPlayers.msvc_equipos.repositories.EquipoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamApiService {
    private final EquipoRepository repository;
    private final TeamMapper mapper;

    public TeamApiService(EquipoRepository repository, TeamMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public PageResponse<TeamResponse> list(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("nombreEquipo").ascending());
        Page<Equipo> teams = repository.findAll(pageable);
        return new PageResponse<>(teams.map(mapper::toResponse).getContent(),
                teams.getNumber(), teams.getSize(), teams.getTotalElements(), teams.getTotalPages());
    }

    @Transactional(readOnly = true)
    public TeamResponse get(Long id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id)));
    }

    @Transactional
    public TeamResponse create(TeamRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public TeamResponse update(Long id, TeamRequest request) {
        Equipo team = repository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        mapper.updateEntity(team, request);
        return mapper.toResponse(repository.save(team));
    }

    @Transactional
    public void delete(Long id) {
        Equipo team = repository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        repository.delete(team);
    }
}
