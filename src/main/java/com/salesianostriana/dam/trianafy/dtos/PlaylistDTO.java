package com.salesianostriana.dam.trianafy.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.views.PlaylistViews;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class PlaylistDTO {

    @JsonView({PlaylistViews.CreateResponse.class, PlaylistViews.GeneralResponse.class, PlaylistViews.SingleResponse.class})
    private Long id;

    @JsonView({PlaylistViews.CreateRequest.class, PlaylistViews.CreateResponse.class, PlaylistViews.GeneralResponse.class, PlaylistViews.SingleResponse.class})
    private String name;

    @JsonView({PlaylistViews.CreateRequest.class, PlaylistViews.CreateResponse.class, PlaylistViews.SingleResponse.class})
    private String description;

    @JsonView(PlaylistViews.SingleResponse.class)
    private List<SongResponseDTO> songs;

    @JsonView({PlaylistViews.GeneralResponse.class})
    private long numberOfSongs;

    public static PlaylistDTO of(Playlist playlist){
        return PlaylistDTO.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .songs(playlist.getSongs().stream().map(SongResponseDTO::of).collect(Collectors.toList()))
                .numberOfSongs(playlist.getSongs().size())
                .build();
    }

}
