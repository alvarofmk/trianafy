package com.salesianostriana.dam.trianafy.dtos;

import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.validation.annotation.ArtistExists;
import com.salesianostriana.dam.trianafy.validation.annotation.SongIsNew;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;

@Builder
@Value
@SongIsNew(title = "title", artistId = "artistId", message = "que ase")
public class SongRequestDTO {

    @NotBlank
    private String title;

    @NotNull
    @ArtistExists
    private Long artistId;

    private String album;

    @NotBlank
    private String year;

}
