package com.salesianostriana.dam.trianafy.dtos;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SongResponseDTO {

    private Long id;
    private String title;
    private String artist;
    private String album;
    private String year;

    public static SongResponseDTO of(Song song){
        String artistName = "";
        if(song.getArtist() != null){
            artistName = song.getArtist().getName();
        }
        return SongResponseDTO.builder()
                .id(song.getId())
                .title(song.getTitle())
                .artist(artistName)
                .album(song.getAlbum())
                .year(song.getYear())
                .build();
    }

}
