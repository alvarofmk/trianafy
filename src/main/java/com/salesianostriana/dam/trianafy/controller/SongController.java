package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dtos.SongRequestDTO;
import com.salesianostriana.dam.trianafy.dtos.SongResponseDTO;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
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

    @GetMapping("/song/")
    public ResponseEntity<List<SongResponseDTO>> getAllSongs(){
        List<Song> result = songService.findAll();
        if(!result.isEmpty()){
            return ResponseEntity.ok(result.stream().map(SongResponseDTO::of).collect(Collectors.toList()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/song/{id}")
    public ResponseEntity<SongResponseDTO> getSongById(@PathVariable Long id){
        return ResponseEntity.of(songService.findById(id).map(SongResponseDTO::of));
    }

    @PostMapping("/song/")
    public ResponseEntity<SongResponseDTO> createSong(@RequestBody SongRequestDTO songRequest){
        if(artistService.existsById(songRequest.getArtistId())){
            if(songRequest.getTitle() != "" && artistService.existsById(songRequest.getArtistId())){
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(SongResponseDTO.of(songService.add(songService.toSong(songRequest))));
            }else{
                ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.notFound().build();

    }

    @PutMapping("/song/{id}")
    public ResponseEntity<SongResponseDTO> editSong(@RequestBody SongRequestDTO songRequest, @PathVariable Long id){
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

    @DeleteMapping("/song/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id){
        if(songService.existsById(id)){
            songService.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }

}
