package com.salesianostriana.dam.trianafy.dtos;

import com.salesianostriana.dam.trianafy.model.Playlist;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreatePlaylistResponseDTO {

    private Long id;
    private String name;
    private String description;

    public static CreatePlaylistResponseDTO of(Playlist playlist){
        return CreatePlaylistResponseDTO.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .build();
    }

}
