# Java Spring Rest TrianaFy API

REST API basada en Java Spring, Spring Boot, Hibernate y JPA, Spring HATEOAS, y documentación con Swagger API docs.

## Endpoints

Tanto los cuerpos de las peticiones como sus respuestas están en formato JSON.
Aquí podéis encontrar un resumen de los endpoints, para toda la información, más abajo encontraréis el enlace a la documentación a la que podréis acceder con el proyecto en marcha, además de una descarga de la colección para postman.


```
/artist
  GET / - Obtener lista de artistas
  GET /{id} - Obtener artista por id
  POST / - Añadir artista
  PUT /{id} - Actualizar artista
  DELETE /{id} - Borrar artista

/song
  GET / - Obtener lista de canciones
  GET /{id} - Obtener canción por id
  POST / - Añadir canción
  PUT /{id} - Actualizar canción
  DELETE /{id} - Borrar canción

/list
  GET / - Obtener lista de playlists
  GET /{id} - Obtener playlist por id
  POST / - Añadir playlist
  PUT /{id} - Actualizar playlist
  DELETE /{id} - Borrar playlist
  GET /{id}/song/ - Petición idéntica a /list/{id}
  GET /{id}/song/{idCancion} - Petición idéntica a /song/{id}
  
  POST /{id}/song/{idCancion} - Añade una canción a la playlist
  DELETE /{id}/song/{idCancion} - Borra una canción de la playlist

```

## Documentación

```
- Docs - [Swagger](http://localhost:8080/docs.html)

```

## Colección de postman

```
- [Archivo exportado en JSON](https://github.com/alvarofmk/trianafy/blob/master/FrancoMartinezAlvaro-Trianafy.postman_collection)

```

## Stay in touch

- Author - [Álvaro Franco Martínez](https://github.com/alvarofmk)
