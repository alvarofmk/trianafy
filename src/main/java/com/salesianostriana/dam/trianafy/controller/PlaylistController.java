package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dtos.AllPlaylistsResponseDTO;
import com.salesianostriana.dam.trianafy.dtos.CreatePlaylistRequestDTO;
import com.salesianostriana.dam.trianafy.dtos.SinglePlaylistResponseDTO;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping("/list/")
    public ResponseEntity<List<AllPlaylistsResponseDTO>> getAllPlaylists(){
        List<Playlist> result = this.playlistService.findAll();
        if(!result.isEmpty()){
            return ResponseEntity.ok(result.stream().map(AllPlaylistsResponseDTO::of).collect(Collectors.toList()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<SinglePlaylistResponseDTO> getPlaylistById(@PathVariable Long id){
        return ResponseEntity.of(playlistService.findById(id).map(SinglePlaylistResponseDTO::of));
    }

    @PostMapping("/list/")
    public ResponseEntity<AllPlaylistsResponseDTO> createPlaylist(@RequestBody CreatePlaylistRequestDTO createPlaylistRequest){
        if(createPlaylistRequest.getName() != ""){
            return ResponseEntity.status(HttpStatus.CREATED).body(AllPlaylistsResponseDTO.of(playlistService.add(createPlaylistRequest.toPlaylist())));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/list/{id}")
    public ResponseEntity<AllPlaylistsResponseDTO> updatePlaylist(@RequestBody CreatePlaylistRequestDTO createPlaylistRequest, @PathVariable Long id){
        if(createPlaylistRequest.getName() != ""){
            return ResponseEntity.of(
                    playlistService.findById(id).map(playlistToEdit -> {
                        playlistToEdit.setName(createPlaylistRequest.getName());
                        playlistToEdit.setDescription(createPlaylistRequest.getDescription());
                        return AllPlaylistsResponseDTO.of(playlistService.edit(playlistToEdit));
                    })
            );
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/list/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id){
        if (playlistService.existsById(id)){
            playlistService.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }

}
