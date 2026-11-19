package com.proyectPlayers.msvc_equipos.services;

import com.proyectPlayers.msvc_equipos.clients.JugadorClientRest;
import com.proyectPlayers.msvc_equipos.dto.TeamRequest;
import com.proyectPlayers.msvc_equipos.exceptions.TeamNotFoundException;
import com.proyectPlayers.msvc_equipos.mappers.TeamMapper;
import com.proyectPlayers.msvc_equipos.models.entity.Equipo;
import com.proyectPlayers.msvc_equipos.repositories.EquipoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipoServiceImplTest {

    @Mock
    EquipoRepository repository;

    @Mock
    JugadorClientRest jugadorClient;

    @Mock
    TeamMapper mapper;

    @InjectMocks
    EquipoServiceImpl service;

    @Test
    void actualizarEquipoInexistenteLanzaNotFound() {
        when(repository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class,
                () -> service.actualizar(123L, new TeamRequest("Team", null, null, null)));

        verify(repository, never()).save(any());
    }

    @Test
    void eliminarEquipoInexistenteLanzaNotFound() {
        when(repository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> service.eliminar(123L));

        verify(repository, never()).delete(any(Equipo.class));
    }
}
