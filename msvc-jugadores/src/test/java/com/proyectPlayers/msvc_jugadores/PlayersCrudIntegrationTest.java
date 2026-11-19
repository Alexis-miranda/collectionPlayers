package com.proyectPlayers.msvc_jugadores;

import tools.jackson.databind.ObjectMapper;
import com.proyectPlayers.msvc_jugadores.models.dto.request.PlayerRequest;
import com.proyectPlayers.msvc_jugadores.repositories.JugadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:playerscrud;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class PlayersCrudIntegrationTest {

    private static final String API = "/api/v1/players";

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired JugadorRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void crudLifecycleReturnsOnlyResponseDto() throws Exception {
        String request = objectMapper.writeValueAsString(validPlayer());

        mockMvc.perform(post(API).contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(API + "/")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nombreCompleto", is("Alexis Miranda")))
                .andExpect(jsonPath("$.dorsal", is(10)))
                .andExpect(jsonPath("$.hibernateLazyInitializer").doesNotExist());

        Long id = repository.findAll().getFirst().getId();

        mockMvc.perform(get(API + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Alexis")))
                .andExpect(jsonPath("$.nacionalidad", is("España")));

        mockMvc.perform(put(API + "/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PlayerRequest(
                                "Alexis", "Miranda", LocalDate.of(2000, 1, 1), "España",
                                "MIDFIELDER", "RIGHT", 178, 8, true))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dorsal", is(8)));

        mockMvc.perform(get(API).param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements", is(1)));

        mockMvc.perform(delete(API + "/" + id)).andExpect(status().isNoContent());
        mockMvc.perform(get(API + "/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void rejectsInvalidPlayerPayload() throws Exception {
        mockMvc.perform(post(API).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"", "dorsal":120, "fechaNacimiento":"2999-01-01"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Validation failed")))
                .andExpect(jsonPath("$.errors.nombre", notNullValue()))
                .andExpect(jsonPath("$.errors.dorsal", notNullValue()));
    }

    @Test
    void legacyBulkLookupRemainsCompatibleWithTeamsClient() throws Exception {
        var player = new com.proyectPlayers.msvc_jugadores.models.entity.Jugador();
        player.setNombre("Jugador");
        player.setDorsal(9);
        repository.save(player);
        Long id = repository.findAll().getFirst().getId();

        mockMvc.perform(get("/jugadores-por-equipo").param("ids", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(id.intValue())))
                .andExpect(jsonPath("$[0].nombre", is("Jugador")));
    }

    private PlayerRequest validPlayer() {
        return new PlayerRequest("Alexis", "Miranda", LocalDate.of(2000, 1, 1),
                "España", "FORWARD", "RIGHT", 178, 10, true);
    }
}
