package com.proyectPlayers.msvc_equipos.api;

import com.proyectPlayers.msvc_equipos.api.dto.TeamRequest;
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
class TeamApiServiceTest {
    @Mock EquipoRepository repository;
    @Mock TeamMapper mapper;
    @InjectMocks TeamApiService service;

    @Test
    void updateMissingTeamThrowsNotFound() {
        when(repository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(TeamNotFoundException.class,
                () -> service.update(123L, new TeamRequest("Team", null, null, null)));
        verify(repository, never()).save(any());
    }

    @Test
    void deleteMissingTeamThrowsNotFound() {
        when(repository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(TeamNotFoundException.class, () -> service.delete(123L));
        verify(repository, never()).delete(any(Equipo.class));
    }
}
