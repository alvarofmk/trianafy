package com.salesianostriana.dam.trianafy.dtos;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SingleSongResponseDTO {

    private Long id;
    private String title;
    private String album;
    private String year;
    private SongResponseArtistDTO artist;

    public static SingleSongResponseDTO of(Song song){
        return SingleSongResponseDTO.builder()
                .id(song.getId())
                .title(song.getTitle())
                .album(song.getAlbum())
                .year(song.getYear())
                .artist(SongResponseArtistDTO.of(song.getArtist()))
                .build();
    }

}
