package com.proyectPlayers.msvc_equipos.services;

import com.proyectPlayers.msvc_equipos.clients.JugadorClientRest;
import com.proyectPlayers.msvc_equipos.models.Jugador;
import com.proyectPlayers.msvc_equipos.models.entity.Equipo;
import com.proyectPlayers.msvc_equipos.models.entity.EquipoJugador;
import com.proyectPlayers.msvc_equipos.repositories.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipoServiceImpl implements EquipoService {

    @Autowired
    private EquipoRepository repository;

    @Autowired(required=true)
    private JugadorClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> listar() {
        return (List<Equipo>) repository.findAll();
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
    public void eliminar(Long id) {
        repository.deleteById(id);

    }

    @Override
    public void eliminarEquipoJugadorPorId(Long id) {
        repository.eliminarEquipoJugadorPorId(id);
    }

    @Override
    @Transactional
    public Optional<Jugador> asignarJugador(Jugador jugador, Long equipoId) {

        Optional<Equipo> o = repository.findById(equipoId);
        if (o.isPresent()) {
            Jugador JugadorMsvc = client.detalle(jugador.getId());

            System.out.println(JugadorMsvc.getNombre());

            Equipo equipo = o.get();
            EquipoJugador equipoJugador = new EquipoJugador();
            equipoJugador.setJugadorId(JugadorMsvc.getId());

            equipo.addequipoJugadorList(equipoJugador);
            repository.save(equipo);
            return Optional.of(JugadorMsvc);
        }

        return Optional.empty();
    }




    @Override
    @Transactional
    public Optional<Jugador> crearJugador(Jugador jugador, Long equipoId) {
        //recibe un codigo de equipo y lo busca en la bbdd
        Optional<Equipo> o = repository.findById(equipoId);

        if (o.isPresent()) {
            Jugador jugadorNuevo = client.crear(jugador);//lo crea bien

            Equipo equipo = o.get();
            EquipoJugador equipoJugador = new EquipoJugador();
            equipoJugador.setJugadorId(jugadorNuevo.getId());

            equipo.addequipoJugadorList(equipoJugador);

            repository.save(equipo);
            return Optional.of(jugadorNuevo);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Jugador> eliminarJugador(Jugador jugador, Long equipoId) {


        Optional<Equipo> o = repository.findById(equipoId);
        if (o.isPresent()) {
            Jugador jugadorMsvc = client.detalle(jugador.getId());

            Equipo equipo = o.get();
            EquipoJugador equipoJugador = new EquipoJugador();
            equipoJugador.setJugadorId(jugadorMsvc.getId());

            equipo.removeEquipoJugadores(equipoJugador);
            repository.save(equipo);
            return Optional.of(jugadorMsvc);
        }


        return Optional.empty();
    }


    @Transactional(readOnly = true)
    public Optional<Equipo> porIdConJugadores(Long id) {
        Optional<Equipo> o = repository.findById(id);
        if (o.isPresent()) {
            Equipo equipo = o.get();
            if (!equipo.getEquipoJugadorList().isEmpty()) {
                List<Long> ids = equipo.getEquipoJugadorList().stream().map(cu -> cu.getJugadorId())
                        .collect(Collectors.toList());

                List<Jugador> jugadorList = client.obtenerJugadorPorEquipo(ids);
                equipo.setJugadoresList(jugadorList);
            }
            return Optional.of(equipo);
        }
        return Optional.empty();
    }
}
