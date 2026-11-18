package com.proyectPlayers.msvc_jugadores.config;

import com.proyectPlayers.msvc_jugadores.clients.EquipoClientRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.client.support.RestClientAdapter;

@Configuration
public class HttpClientsConfiguration {

    @Bean
    EquipoClientRest equipoClient(
            RestClient.Builder builder,
            @Value("${services.equipos.base-url}") String baseUrl) {
        RestClient restClient = builder.baseUrl(baseUrl).build();
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return factory.createClient(EquipoClientRest.class);
    }
}
