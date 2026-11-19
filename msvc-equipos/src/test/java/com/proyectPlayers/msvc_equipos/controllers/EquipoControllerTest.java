package com.proyectPlayers.msvc_equipos.controllers;

import com.proyectPlayers.msvc_equipos.dto.PageResponse;
import com.proyectPlayers.msvc_equipos.dto.TeamRequest;
import com.proyectPlayers.msvc_equipos.dto.TeamResponse;
import com.proyectPlayers.msvc_equipos.exceptions.TeamNotFoundException;
import com.proyectPlayers.msvc_equipos.services.EquipoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EquipoController.class)
class EquipoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EquipoService service;

    @Test
    void listarPaginadoDevuelvePaginaDeTeams() throws Exception {
        TeamResponse team = new TeamResponse(1L, "Argentina", "https://example.com/ar.png",
                "Monumental", "Buenos Aires");
        when(service.listarPaginado(0, 20))
                .thenReturn(new PageResponse<>(List.of(team), 0, 20, 1, 1));

        mockMvc.perform(get("/api/v1/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Argentina"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(service).listarPaginado(0, 20);
    }

    @Test
    void consultarTeamInexistenteDevuelveProblemDetail404() throws Exception {
        when(service.detalleDto(99L)).thenThrow(new TeamNotFoundException(99L));

        mockMvc.perform(get("/api/v1/teams/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Team not found"))
                .andExpect(jsonPath("$.detail").value("Team not found: 99"));
    }

    @Test
    void crearTeamValidoDevuelve201YLocation() throws Exception {
        when(service.crear(any(TeamRequest.class)))
                .thenReturn(new TeamResponse(7L, "Chile", null, "Nacional", "Santiago"));

        mockMvc.perform(post("/api/v1/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Chile",
                                  "stadiumName": "Nacional",
                                  "stadiumLocation": "Santiago"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/teams/7")))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Chile"));
    }

    @Test
    void crearTeamSinNombreDevuelve400ConDetalleDeValidacion() throws Exception {
        mockMvc.perform(post("/api/v1/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": " "
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void actualizarTeamDevuelveDtoActualizado() throws Exception {
        when(service.actualizar(eq(7L), any(TeamRequest.class)))
                .thenReturn(new TeamResponse(7L, "Chile actualizado", null, null, null));

        mockMvc.perform(put("/api/v1/teams/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Chile actualizado"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Chile actualizado"));
    }

    @Test
    void eliminarTeamDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/v1/teams/7"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(7L);
    }
}
