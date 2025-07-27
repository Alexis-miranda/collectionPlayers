# ⚽ CollectionPlayers

Proyecto de práctica de **microservicios con Spring Boot**, inspirado en el fútbol. Se divide en dos servicios: **Jugadores** y **Equipos**, comunicados mediante **OpenFeign** y desplegados con **Docker**.

---

## 🚀 Características principales

- Arquitectura basada en microservicios.
- Comunicación REST entre servicios con Feign.
- Contenerización con Docker.
- CRUD básico para jugadores y equipos.
- Uso de PostgreSQL como base de datos.

---

## 🏗️ Estructura

- `msvc-jugadores`: gestiona jugadores.
- `msvc-equipos`: gestiona equipos, incluyendo relación con jugadores.

---

## ▶️ Cómo correrlo con docker una vez descargado.


- docker build -t jugadores ./msvc-jugadores
- docker build -t equipos ./msvc-equipos

- docker run -p 8005:8005 jugadores
- docker run -p 8006:8006 equipos

## 🛠 Tecnologías

- Java + Spring Boot  
- Spring Data JPA  
- OpenFeign  
- Docker  
- postgresql   
- Postman

---

## 📌 Estado

- ✅ Funcional   (./msvc-jugadores)  - En proceso (./msvc-equipos)
- 🛠 Próximamente: campeonatos, estadísticas, y  seguridad.

