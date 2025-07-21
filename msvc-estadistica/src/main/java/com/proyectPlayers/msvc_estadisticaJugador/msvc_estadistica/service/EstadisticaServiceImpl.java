package com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.service;

import com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.models.entity.EstadisticaJugador;
import com.proyectPlayers.msvc_estadisticaJugador.msvc_estadistica.repositories.EstadisticaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EstadisticaServiceImpl implements EstadisticaService {

    @Autowired
    private EstadisticaRepository repository;


    @Override
    @Transactional(readOnly = true)
    public List<EstadisticaJugador> listar() {
        return (List<EstadisticaJugador>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EstadisticaJugador> porId(Long id) {

        return repository.findById(id);
    }

    @Override
    @Transactional
    public EstadisticaJugador guardar(EstadisticaJugador jugador) {
        return repository.save(jugador);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);

    }

    @Override
    @Transactional
    public Optional<EstadisticaJugador> Modificarestadistica(Long id) {
        return Optional.empty();
    }
}
