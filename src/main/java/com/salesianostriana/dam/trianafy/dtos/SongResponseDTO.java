package com.salesianostriana.dam.trianafy.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.views.PlaylistViews;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SongResponseDTO {

    @JsonView(PlaylistViews.SingleResponse.class)
    private Long id;
    @JsonView(PlaylistViews.SingleResponse.class)
    private String title;
    @JsonView(PlaylistViews.SingleResponse.class)
    private String artist;
    @JsonView(PlaylistViews.SingleResponse.class)
    private String album;
    @JsonView(PlaylistViews.SingleResponse.class)
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
