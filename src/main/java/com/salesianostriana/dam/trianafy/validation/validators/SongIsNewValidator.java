package com.salesianostriana.dam.trianafy.validation.validators;

import com.salesianostriana.dam.trianafy.service.SongService;
import com.salesianostriana.dam.trianafy.validation.annotation.SongIsNew;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SongIsNewValidator implements ConstraintValidator<SongIsNew, Object> {

    private String title;
    private long artistId;

    private SongService songService;

    @Override
    public void initialize(SongIsNew constraintAnnotation) {
        this.title = constraintAnnotation.title();
        this.artistId = Long.parseLong(constraintAnnotation.artistId());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return title != null && artistId != 0 ? !songService.checkSongAndArtist(title, artistId) : false;
    }
}
