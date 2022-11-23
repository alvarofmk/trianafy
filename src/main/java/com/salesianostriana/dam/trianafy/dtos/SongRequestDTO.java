package com.salesianostriana.dam.trianafy.dtos;

import com.salesianostriana.dam.trianafy.model.Song;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SongRequestDTO {

    private String title;
    private Long artistId;
    private String album;
    private String year;

}
