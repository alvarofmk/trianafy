package com.salesianostriana.dam.trianafy.validation.validators;

import com.salesianostriana.dam.trianafy.service.SongService;
import com.salesianostriana.dam.trianafy.validation.annotation.SongIsNew;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SongIsNewValidator implements ConstraintValidator<SongIsNew, Object> {

    private String title;
    private String artistId;

    @Autowired
    private SongService songService;

    @Override
    public void initialize(SongIsNew constraintAnnotation) {
        this.title = constraintAnnotation.title();
        this.artistId = constraintAnnotation.artistId();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String title = (String) PropertyAccessorFactory.forBeanPropertyAccess(value).getPropertyValue(this.title);
        Long artist = (Long) PropertyAccessorFactory.forBeanPropertyAccess(value).getPropertyValue(this.artistId);
        return title != null && artistId != null ? !songService.checkSongAndArtist(title, artist) : false;
    }
}
