package com.salesianostriana.dam.trianafy.service;


import com.salesianostriana.dam.trianafy.dtos.SongRequestDTO;
import com.salesianostriana.dam.trianafy.dtos.SongResponseDTO;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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

    public Song findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("No se ha encontrado la canción con id " + id));
    }

    public List<Song> findAll() {
        List<Song> result = repository.findAll();
        if(result.isEmpty())
            throw new EntityNotFoundException("No existe ninguna canción.");
        return result;
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

    public boolean checkSongAndArtist(String songName, Long artistId){
        return repository.existsByTitleAndArtistId(songName, artistId);
    }

}
