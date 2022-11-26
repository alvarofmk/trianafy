package com.salesianostriana.dam.trianafy.dtos;

import com.salesianostriana.dam.trianafy.model.Artist;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SongResponseArtistDTO {

    private Long id;
    private String artist;

    public static SongResponseArtistDTO of(Artist artist){
        return SongResponseArtistDTO.builder()
                .artist(artist.getName())
                .id(artist.getId())
                .build();
    }

}
