package com.salesianostriana.dam.trianafy.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesianostriana.dam.trianafy.dtos.*;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import com.salesianostriana.dam.trianafy.views.PlaylistViews;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;
    private final SongService songService;

    @Operation(summary = "Obtiene todas las playlists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlists encontradas",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AllPlaylistsResponseDTO.class)),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": 12,
                                            "name": "Random",
                                            "numberOfSongs": 4
                                        },
                                        {
                                            "id": 25,
                                            "name": "Electro",
                                            "numberOfSongs": 457
                                        }
                                    ]
                                    """)) }),
            @ApiResponse(responseCode = "404", description = "No se encuentra ninguna playlist",
                    content = @Content) })
    @JsonView(PlaylistViews.GeneralResponse.class)
    @GetMapping("/list/")
    public ResponseEntity<List<PlaylistDTO>> getAllPlaylists(){
        List<Playlist> result = this.playlistService.findAll();
        if(!result.isEmpty()){
            return ResponseEntity.ok(result.stream().map(PlaylistDTO::of).collect(Collectors.toList()));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtiene una playlist por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist encontrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SinglePlaylistResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 12,
                                        "name": "Random",
                                        "description": "Una lista muy loca",
                                        "songs": [
                                            {
                                                "id": 9,
                                                "title": "Enter Sandman",
                                                "artist": "Metallica",
                                                "album": "Metallica",
                                                "year": "1991"
                                            },
                                            {
                                                "id": 8,
                                                "title": "Love Again",
                                                "artist": "Dua Lipa",
                                                "album": "Future Nostalgia",
                                                "year": "2021"
                                            }
                                        ]
                                    }
                                    """))}),
            @ApiResponse(responseCode = "404", description = "No se encuentra la playlist",
                    content = @Content) })
    @Parameter(description = "El id de la playlist a encontrar", name = "id", required = true)
    @JsonView(PlaylistViews.SingleResponse.class)
    @GetMapping("/list/{id}")
    public ResponseEntity<PlaylistDTO> getPlaylistById(@PathVariable Long id){
        return ResponseEntity.of(playlistService.findById(id).map(PlaylistDTO::of));
    }

    @Operation(summary = "Crea una nueva playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Playlist creada con éxito",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CreatePlaylistResponseDTO.class)),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 13,
                                        "name": "Electro",
                                        "description": "Liquid DnB etc"
                                    }
                                    """)) }),
            @ApiResponse(responseCode = "400", description = "Los datos son incorrectos",
                    content = @Content) })
    @RequestBody(required = true, description = "Los datos de la nueva playlist")
    @JsonView(PlaylistViews.CreateResponse.class)
    @PostMapping("/list/")
    public ResponseEntity<PlaylistDTO> createPlaylist(@org.springframework.web.bind.annotation.RequestBody CreatePlaylistRequestDTO createPlaylistRequest){
        if(createPlaylistRequest.getName() != ""){
            return ResponseEntity.status(HttpStatus.CREATED).body(PlaylistDTO.of(playlistService.add(createPlaylistRequest.toPlaylist())));
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Edita una playlist por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist editada con éxito",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AllPlaylistsResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 13,
                                        "name": "Electro",
                                        "numberOfSongs": 0
                                    }
                                    """))}),
            @ApiResponse(responseCode = "404", description = "No se encuentra la playlist",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Los datos son incorrectos",
                    content = @Content)
    })
    @Parameter(description = "El id de la playlist a modificar", name = "id", required = true)
    @RequestBody(required = true, description = "Los datos actualizados de la playlist")
    @JsonView(PlaylistViews.GeneralResponse.class)
    @PutMapping("/list/{id}")
    public ResponseEntity<PlaylistDTO> updatePlaylist(@org.springframework.web.bind.annotation.RequestBody CreatePlaylistRequestDTO createPlaylistRequest, @PathVariable Long id){
        if(createPlaylistRequest.getName() != ""){
            return ResponseEntity.of(
                    playlistService.findById(id).map(playlistToEdit -> {
                        playlistToEdit.setName(createPlaylistRequest.getName());
                        playlistToEdit.setDescription(createPlaylistRequest.getDescription());
                        return PlaylistDTO.of(playlistService.edit(playlistToEdit));
                    })
            );
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Borra una playlist por su id")
    @ApiResponse(responseCode = "204", description = "Playlist borrada con éxito",
            content = @Content)
    @Parameter(description = "El id de la playlist a borrar", name = "id", required = true)
    @DeleteMapping("/list/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id){
        if (playlistService.existsById(id)){
            playlistService.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtiene una cancion de una playlist por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canción encontrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 9,
                                        "title": "Enter Sandman",
                                        "album": "Metallica",
                                        "year": "1991",
                                        "artist": {
                                            "id": 3,
                                            "name": "Metallica"
                                        }
                                    }
                                    """))}),
            @ApiResponse(responseCode = "404", description = "No se encuentra la canción",
                    content = @Content) })
    @Parameters(value = {
            @Parameter(description = "El id de la playlist", name = "id", required = true),
            @Parameter(description = "El id de la canción a encontrar", name = "idSong", required = true),
    })
    @GetMapping("/list/{id}/song/{idSong}")
    public ModelAndView getSongFromPlaylist(@PathVariable Long idSong){
        return new ModelAndView("redirect:/song/" + idSong);
    }

    @Operation(summary = "Obtiene una playlist por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist encontrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SinglePlaylistResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 12,
                                        "name": "Random",
                                        "description": "Una lista muy loca",
                                        "songs": [
                                            {
                                                "id": 9,
                                                "title": "Enter Sandman",
                                                "artist": "Metallica",
                                                "album": "Metallica",
                                                "year": "1991"
                                            },
                                            {
                                                "id": 8,
                                                "title": "Love Again",
                                                "artist": "Dua Lipa",
                                                "album": "Future Nostalgia",
                                                "year": "2021"
                                            }
                                        ]
                                    }
                                    """))}),
            @ApiResponse(responseCode = "404", description = "No se encuentra la playlist",
                    content = @Content) })
    @Parameter(description = "El id de la playlist a encontrar", name = "id", required = true)
    @GetMapping("/list/{id}/song/")
    public ModelAndView getSongsFromPlaylist(@PathVariable Long id){
        return new ModelAndView("redirect:/list/" + id);
    }

    @Operation(summary = "Añade una canción a una playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canción añadida con éxito",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SinglePlaylistResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 12,
                                        "name": "Random",
                                        "description": "Una lista muy loca",
                                        "songs": [
                                            {
                                                "id": 9,
                                                "title": "Enter Sandman",
                                                "artist": "Metallica",
                                                "album": "Metallica",
                                                "year": "1991"
                                            },
                                            {
                                                "id": 6,
                                                "title": "A mis cuarenta y diez",
                                                "artist": "Joaquín Sabina",
                                                "album": "19 días y 500 noches",
                                                "year": "1999"
                                            }
                                        ]
                                    }
                                    """))}),
            @ApiResponse(responseCode = "404", description = "No se encuentra la playlist o la canción",
                    content = @Content) })
    @Parameters(value = {
            @Parameter(description = "El id de la playlist", name = "id", required = true),
            @Parameter(description = "El id de la canción a añadir", name = "idSong", required = true),
    })
    @JsonView(PlaylistViews.SingleResponse.class)
    @PostMapping("/list/{id}/song/{idSong}")
    public ResponseEntity<PlaylistDTO> addSongToPlaylist(@PathVariable Long id, @PathVariable Long idSong){
        if(songService.existsById(idSong)){
            return ResponseEntity.of(
                    playlistService.findById(id).map(playlist -> {
                        playlist.addSong(songService.findById(idSong).get());
                        return PlaylistDTO.of(playlistService.edit(playlist));
                    })
            );
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Borra una canción de una playlist",
            description = "Esta petición borra todas las instancias de la canción en la playlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Canción borrada con éxito",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encuentra la playlist o la canción",
                    content = @Content) })
    @Parameters(value = {
            @Parameter(description = "El id de la playlist", name = "id", required = true),
            @Parameter(description = "El id de la canción a borrar", name = "idSong", required = true),
    })
    @DeleteMapping("/list/{id}/song/{idSong}")
    public ResponseEntity<?> removeSongFromPlaylist(@PathVariable Long id, @PathVariable Long idSong){
            return playlistService.findById(id).map(playlist -> {
                Song toRemove = songService.findById(idSong).get();
                while(playlist.getSongs().contains(toRemove)){
                    playlist.deleteSong(toRemove);
                }
                playlistService.edit(playlist);
                return ResponseEntity.noContent().build();
            }).orElse(ResponseEntity.notFound().build());
    }

}
