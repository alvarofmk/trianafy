package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.ElementType;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    private final SongService songService;

    @Operation(summary = "Obtiene todos los artistas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artistas encontrados",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": 1,
                                            "name": "Joaquín Sabina"
                                        },
                                        {
                                            "id": 2,
                                            "name": "Dua Lipa"
                                        },
                                        {
                                            "id": 3,
                                            "name": "Metallica"
                                        }
                                    ]
                                    """)) }),
            @ApiResponse(responseCode = "404", description = "No se encuentra ningún artista",
                    content = @Content) })
    @GetMapping("/artist/")
    public ResponseEntity<List<Artist>> getAllArtists(){
        List<Artist> result = this.artistService.findAll();
        if(!result.isEmpty()){
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtiene un artista por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 3,
                                        "name": "Metallica"
                                    }
                                    """))}),
            @ApiResponse(responseCode = "404", description = "No se encuentra el artista",
                    content = @Content) })
    @Parameter(description = "El id del artista a encontrar", name = "id", required = true)
    @GetMapping("/artist/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id){
        return ResponseEntity.of(artistService.findById(id));
    }

    @Operation(summary = "Crea un nuevo artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Artista creado con éxito",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 4,
                                        "name": "Muzz"
                                    }
                                    """)) }),
            @ApiResponse(responseCode = "400", description = "Los datos son incorrectos",
                    content = @Content) })
    @RequestBody(required = true, description = "Los datos del nuevo artista")
    @PostMapping("/artist/")
    public ResponseEntity<Artist> createArtist(@org.springframework.web.bind.annotation.RequestBody Artist artist){
        if(artist.getName() == ""){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.add(artist));
    }

    @Operation(summary = "Edita un artista por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista editado con éxito",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 3,
                                        "name": "Metallica"
                                    }
                                    """))}),
            @ApiResponse(responseCode = "404", description = "No se encuentra el artista",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Los datos son incorrectos",
                    content = @Content)
    })
    @Parameter(description = "El id del artista a modificar", name = "id", required = true)
    @RequestBody(required = true, description = "Los datos actualizados del artista")
    @PutMapping("/artist/{id}")
    public ResponseEntity<Artist> editArtist(@org.springframework.web.bind.annotation.RequestBody Artist artist, @PathVariable Long id){
        if(artist.getName() != ""){
            return ResponseEntity.of(
                    artistService.findById(id).map(artistEditing -> {
                        artistEditing.setName(artist.getName());
                        return artistService.edit(artistEditing);
                    })
            );
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Borra un artista por su id",
            description = "Si el artista a borrar ya tiene canciones asociadas, " +
                    "el método setea el artista a nulo en todas esas canciones antes de borrar definitivamente.")
    @ApiResponse(responseCode = "204", description = "Artista borrado con éxito",
            content = @Content)
    @Parameter(description = "El id del artista a borrar", name = "id", required = true)
    @DeleteMapping("/artist/{id}")
    public ResponseEntity<?> deleteArtist(@PathVariable Long id){
        if(artistService.existsById(id)){
            songService.findAll().stream().filter(song -> song.getArtist().getId() == id).forEach(song -> {
                song.setArtist(null);
                songService.edit(song);
            });
            artistService.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }

}
