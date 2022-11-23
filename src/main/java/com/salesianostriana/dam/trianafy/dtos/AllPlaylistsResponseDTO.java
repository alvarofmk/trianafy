package com.salesianostriana.dam.trianafy.dtos;

import com.salesianostriana.dam.trianafy.model.Playlist;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AllPlaylistsResponseDTO {

    private Long id;
    private String name;
    private String description;
    private long numberOfSongs;

    public static AllPlaylistsResponseDTO of(Playlist playlist){
        return AllPlaylistsResponseDTO.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .numberOfSongs(playlist.getSongs().size())
                .build();
    }

}
