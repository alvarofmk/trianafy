package com.salesianostriana.dam.trianafy.service;


import com.salesianostriana.dam.trianafy.dtos.SongRequestDTO;
import com.salesianostriana.dam.trianafy.dtos.SongResponseDTO;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository repository;
    private final ArtistService artistService;

    public Song add(Song song) {
        return repository.save(song);
    }

    public Optional<Song> findById(Long id) {
        return repository.findById(id);
    }

    public List<Song> findAll() {
        return repository.findAll();
    }

    public Song edit(Song song) {
        return repository.save(song);
    }

    public void delete(Song song) {
        repository.delete(song);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) { return repository.existsById(id); }

    public Song toSong(SongRequestDTO songDTO){
        return Song.builder()
                .title(songDTO.getTitle())
                .album(songDTO.getAlbum())
                .year(songDTO.getYear())
                .artist(artistService.findById(songDTO.getArtistId()).get())
                .build();
    }

}
