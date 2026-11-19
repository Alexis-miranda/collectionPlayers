package com.proyectPlayers.msvc_jugadores.services;

import com.proyectPlayers.msvc_jugadores.exceptions.ResourceNotFoundException;
import com.proyectPlayers.msvc_jugadores.mappers.PlayerMapper;
import com.proyectPlayers.msvc_jugadores.models.dto.request.PlayerRequest;
import com.proyectPlayers.msvc_jugadores.models.dto.response.PageResponse;
import com.proyectPlayers.msvc_jugadores.models.dto.response.PlayerResponse;
import com.proyectPlayers.msvc_jugadores.models.entity.Jugador;
import com.proyectPlayers.msvc_jugadores.repositories.JugadorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JugadorServiceImpl implements JugadorService {

    private final JugadorRepository repository;
    private final PlayerMapper mapper;

    public JugadorServiceImpl(JugadorRepository repository, PlayerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PlayerResponse> listar(int page, int size) {
        Page<Jugador> result = repository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nombre").and(Sort.by("id"))));
        return new PageResponse<>(
                result.getContent().stream().map(mapper::toResponse).toList(),
                result.getNumber(), result.getSize(), result.getTotalElements(),
                result.getTotalPages(), result.isFirst(), result.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerResponse porId(Long id) {
        return mapper.toResponse(findPlayer(id));
    }

    @Override
    public PlayerResponse crear(PlayerRequest request) {
        Jugador jugador = mapper.toEntity(request);
        return mapper.toResponse(repository.save(jugador));
    }

    @Override
    public PlayerResponse actualizar(Long id, PlayerRequest request) {
        Jugador jugador = findPlayer(id);
        mapper.updateEntity(jugador, request);
        return mapper.toResponse(repository.save(jugador));
    }

    @Override
    public void eliminar(Long id) {
        Jugador jugador = findPlayer(id);
        repository.delete(jugador);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerResponse> listarPorIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return repository.findAllByIdIn(ids).stream().map(mapper::toResponse).toList();
    }

    private Jugador findPlayer(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player " + id + " was not found."));
    }
}
