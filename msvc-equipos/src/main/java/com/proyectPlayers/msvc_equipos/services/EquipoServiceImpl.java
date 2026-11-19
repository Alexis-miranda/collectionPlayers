package com.proyectPlayers.msvc_equipos.services;

import com.proyectPlayers.msvc_equipos.clients.JugadorClientRest;
import com.proyectPlayers.msvc_equipos.dto.PageResponse;
import com.proyectPlayers.msvc_equipos.dto.TeamRequest;
import com.proyectPlayers.msvc_equipos.dto.TeamResponse;
import com.proyectPlayers.msvc_equipos.exceptions.TeamNotFoundException;
import com.proyectPlayers.msvc_equipos.mappers.TeamMapper;
import com.proyectPlayers.msvc_equipos.models.Jugador;
import com.proyectPlayers.msvc_equipos.models.entity.Equipo;
import com.proyectPlayers.msvc_equipos.models.entity.EquipoJugador;
import com.proyectPlayers.msvc_equipos.repositories.EquipoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository repository;
    private final JugadorClientRest client;
    private final TeamMapper mapper;

    public EquipoServiceImpl(EquipoRepository repository, JugadorClientRest client, TeamMapper mapper) {
        this.repository = repository;
        this.client = client;
        this.mapper = mapper;
    }

    // Modern, versioned Teams API

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TeamResponse> listarPaginado(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("nombreEquipo").ascending());
        Page<Equipo> teams = repository.findAll(pageable);
        return new PageResponse<>(
                teams.map(mapper::toResponse).getContent(),
                teams.getNumber(),
                teams.getSize(),
                teams.getTotalElements(),
                teams.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponse detalleDto(Long id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id)));
    }

    @Override
    @Transactional
    public TeamResponse crear(TeamRequest request) {
        Equipo team = mapper.toEntity(request);
        return mapper.toResponse(repository.save(team));
    }

    @Override
    @Transactional
    public TeamResponse actualizar(Long id, TeamRequest request) {
        Equipo team = repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));
        mapper.updateEntity(team, request);
        return mapper.toResponse(repository.save(team));
    }

    // Existing operations retained for backwards compatibility

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Equipo> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Equipo guardar(Equipo equipo) {
        return repository.save(equipo);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Equipo equipo = repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));
        repository.delete(equipo);
    }

    @Override
    @Transactional
    public void eliminarEquipoJugadorPorId(Long id) {
        repository.eliminarEquipoJugadorPorId(id);
    }

    @Override
    @Transactional
    public Optional<Jugador> asignarJugador(Jugador jugador, Long equipoId) {
        Optional<Equipo> optionalEquipo = repository.findById(equipoId);
        if (optionalEquipo.isEmpty()) {
            return Optional.empty();
        }

        Jugador existingPlayer = client.detalle(jugador.getId());
        Equipo equipo = optionalEquipo.get();

        EquipoJugador association = new EquipoJugador();
        association.setJugadorId(existingPlayer.getId());
        equipo.addequipoJugadorList(association);
        repository.save(equipo);

        return Optional.of(existingPlayer);
    }

    @Override
    @Transactional
    public Optional<Jugador> crearJugador(Jugador jugador, Long equipoId) {
        Optional<Equipo> optionalEquipo = repository.findById(equipoId);
        if (optionalEquipo.isEmpty()) {
            return Optional.empty();
        }

        // Legacy behavior retained temporarily; player ownership will be reviewed
        // separately when the Teams-to-Players integration is redesigned.
        Jugador createdPlayer = client.crear(jugador);
        Equipo equipo = optionalEquipo.get();

        EquipoJugador association = new EquipoJugador();
        association.setJugadorId(createdPlayer.getId());
        equipo.addequipoJugadorList(association);
        repository.save(equipo);

        return Optional.of(createdPlayer);
    }

    @Override
    @Transactional
    public Optional<Jugador> eliminarJugador(Jugador jugador, Long equipoId) {
        Optional<Equipo> optionalEquipo = repository.findById(equipoId);
        if (optionalEquipo.isEmpty()) {
            return Optional.empty();
        }

        Jugador existingPlayer = client.detalle(jugador.getId());
        Equipo equipo = optionalEquipo.get();

        EquipoJugador association = new EquipoJugador();
        association.setJugadorId(existingPlayer.getId());
        equipo.removeEquipoJugadores(association);
        repository.save(equipo);

        return Optional.of(existingPlayer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Equipo> porIdConJugadores(Long id) {
        Optional<Equipo> optionalEquipo = repository.findById(id);
        if (optionalEquipo.isEmpty()) {
            return Optional.empty();
        }

        Equipo equipo = optionalEquipo.get();
        if (!equipo.getEquipoJugadorList().isEmpty()) {
            List<Long> ids = equipo.getEquipoJugadorList().stream()
                    .map(EquipoJugador::getJugadorId)
                    .collect(Collectors.toList());
            List<Jugador> players = client.obtenerJugadorPorEquipo(ids);
            equipo.setJugadoresList(players);
        }
        return Optional.of(equipo);
    }
}
