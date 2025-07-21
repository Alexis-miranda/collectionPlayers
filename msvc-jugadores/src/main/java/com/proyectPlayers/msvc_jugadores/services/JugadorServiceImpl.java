package com.proyectPlayers.msvc_jugadores.services;

import com.proyectPlayers.msvc_jugadores.models.entity.Jugador;
import com.proyectPlayers.msvc_jugadores.repositories.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class JugadorServiceImpl implements JugadorService {

    @Autowired
    private JugadorRepository repository;

    @Override
    public List<Jugador> listar() {

        return (List<Jugador>) repository.findAll();
    }

    @Override
    public Optional<Jugador> porId(Long id) {


        return repository.findById(id);
    }

    @Override
    public Jugador guardar(Jugador jugador) {
        return repository.save(jugador);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);

    }

    @Override
    public List<Jugador> listarPorIds(Iterable<Long> ids) {


        return (List<Jugador>) repository.findAllById(ids);
    }
}
