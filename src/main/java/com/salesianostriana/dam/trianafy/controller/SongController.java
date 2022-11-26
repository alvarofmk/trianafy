package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dtos.SingleSongResponseDTO;
import com.salesianostriana.dam.trianafy.dtos.SongRequestDTO;
import com.salesianostriana.dam.trianafy.dtos.SongResponseDTO;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;
    private final ArtistService artistService;
    private final PlaylistService playlistService;

    @Operation(summary = "Obtiene todas las canciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canciones encontrados",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SongResponseDTO.class)),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": 4,
                                            "title": "19 días y 500 noches",
                                            "artist": "Joaquín Sabina",
                                            "album": "19 días y 500 noches",
                                            "year": "1999"
                                        },
                                        {
                                            "id": 5,
                                            "title": "Donde habita el olvido",
                                            "artist": "Joaquín Sabina",
                                            "album": "19 días y 500 noches",
                                            "year": "1999"
                                        }
                                    ]
                                    """)) }),
            @ApiResponse(responseCode = "404", description = "No se encuentra ninguna canción",
                    content = @Content) })
    @GetMapping("/song/")
    public ResponseEntity<List<SongResponseDTO>> getAllSongs(){
        List<Song> result = songService.findAll();
        if(!result.isEmpty()){
            return ResponseEntity.ok(result.stream().map(SongResponseDTO::of).collect(Collectors.toList()));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtiene una cancion por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canción encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SingleSongResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 9,
                                        "title": "Enter Sandman",
                                        "album": "Metallica",
                                        "year": "1991",
                                        "artist": {
                                            "id": 3,
                                            "artist": "Metallica"
                                        }
                                    }
                                    """))}),
            @ApiResponse(responseCode = "404", description = "No se encuentra la canción",
                    content = @Content) })
    @Parameter(description = "El id de la canción a encontrar", name = "id", required = true)
    @GetMapping("/song/{id}")
    public ResponseEntity<SingleSongResponseDTO> getSongById(@PathVariable Long id){
        return ResponseEntity.of(songService.findById(id).map(SingleSongResponseDTO::of));
    }

    @Operation(summary = "Crea una nueva canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Canción creada con éxito",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SongResponseDTO.class)),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 13,
                                        "title": "Master of puppets",
                                        "artist": "Metallica",
                                        "album": "Master of puppets",
                                        "year": "1996"
                                    }
                                    """)) }),
            @ApiResponse(responseCode = "400", description = "Los datos son incorrectos",
                    content = @Content)
    })
    @RequestBody(required = true, description = "Los datos de la nueva canción")
    @PostMapping("/song/")
    public ResponseEntity<SongResponseDTO> createSong(@org.springframework.web.bind.annotation.RequestBody SongRequestDTO songRequest){
        if(songRequest.getTitle() != "" && songRequest.getArtistId()!= null){
            if(artistService.existsById(songRequest.getArtistId())){
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(SongResponseDTO.of(songService.add(songService.toSong(songRequest))));
            }
        }
        return ResponseEntity.badRequest().build();

    }

    @Operation(summary = "Edita una canción por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canción editada con éxito",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SongResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 13,
                                        "title": "Master of puppets",
                                        "artist": "Metallica",
                                        "album": "Master of puppets", 
                                        "year": "1997"
                                    }
                                    """))}),
            @ApiResponse(responseCode = "404", description = "No se encuentra la canción",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Los datos son incorrectos",
                    content = @Content)
    })
    @Parameter(description = "El id de la canción a modificar", name = "id", required = true)
    @RequestBody(required = true, description = "Los datos actualizados de la canción")
    @PutMapping("/song/{id}")
    public ResponseEntity<SongResponseDTO> editSong(@org.springframework.web.bind.annotation.RequestBody SongRequestDTO songRequest, @PathVariable Long id){
        if(artistService.existsById(songRequest.getArtistId()) && songRequest.getTitle() != ""){
            return ResponseEntity.of(
                    songService.findById(id).map(songToEdit -> {
                        songToEdit.setArtist(artistService.findById(songRequest.getArtistId()).get());
                        songToEdit.setYear(songRequest.getYear());
                        songToEdit.setAlbum(songRequest.getAlbum());
                        songToEdit.setTitle(songRequest.getTitle());
                        return SongResponseDTO.of(songService.edit(songToEdit));
                    })
            );
        }else{
            return ResponseEntity.badRequest().build();
        }

    }

    @Operation(summary = "Borra una canción por su id",
            description = "Si la canción a borrar está añadida en alguna playlists, el método elimina todas" +
                    "las instancias de la canción de cualquier playlist antes de borrarla de la base de datos," +
                    "para evitar un error de integridad referencial.")
    @ApiResponse(responseCode = "204", description = "Canción borrada con éxito",
            content = @Content)
    @Parameter(description = "El id de la canción a borrar", name = "id", required = true)
    @DeleteMapping("/song/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id){
        if(songService.existsById(id)){
            Song toDelete = songService.findById(id).get();
            playlistService.findAll().stream().filter(playlist -> playlist.getSongs().contains(toDelete)).forEach(playlist -> {
                while (playlist.getSongs().contains(toDelete)){
                    playlist.deleteSong(toDelete);
                }
                playlistService.edit(playlist);
            });
            songService.delete(toDelete);
        }
        return ResponseEntity.noContent().build();
    }

}
