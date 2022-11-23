package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    private final SongService songService;

    @GetMapping("/artist/")
    public ResponseEntity<List<Artist>> getAllArtists(){
        List<Artist> result = this.artistService.findAll();
        if(!result.isEmpty()){
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/artist/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id){
        return ResponseEntity.of(artistService.findById(id));
    }

    @PostMapping("/artist/")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist){
        if(artist.getName() == ""){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.add(artist));
    }

    @PutMapping("/artist/{id}")
    public ResponseEntity<Artist> editArtist(@RequestBody Artist artist, @PathVariable Long id){
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
