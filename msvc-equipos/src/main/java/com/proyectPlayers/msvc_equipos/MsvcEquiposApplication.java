package com.proyectPlayers.msvc_equipos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.proyectPlayers.msvc_equipos.clients")
public class MsvcEquiposApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcEquiposApplication.class, args);
	}

}
