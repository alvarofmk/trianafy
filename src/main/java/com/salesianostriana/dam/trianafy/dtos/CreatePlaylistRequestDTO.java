package com.salesianostriana.dam.trianafy.dtos;

import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;

@Builder
@Value
public class CreatePlaylistRequestDTO {

    private String name;
    private String description;

    public Playlist toPlaylist(){
        return Playlist.builder()
                .songs(new ArrayList<Song>())
                .name(this.name)
                .description(this.description)
                .build();
    }

}
