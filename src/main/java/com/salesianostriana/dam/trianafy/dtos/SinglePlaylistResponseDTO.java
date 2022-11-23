package com.salesianostriana.dam.trianafy.dtos;

import com.salesianostriana.dam.trianafy.model.Playlist;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Value
public class SinglePlaylistResponseDTO {

    private Long id;
    private String name;
    private String description;
    private List<SongResponseDTO> songs;

    public static SinglePlaylistResponseDTO of(Playlist playlist){
        return SinglePlaylistResponseDTO.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .songs(playlist.getSongs().stream().map(SongResponseDTO::of).collect(Collectors.toList()))
                .build();
    }

}
