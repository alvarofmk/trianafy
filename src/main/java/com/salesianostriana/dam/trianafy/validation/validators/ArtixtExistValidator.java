package com.salesianostriana.dam.trianafy.validation.validators;

import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.validation.annotation.ArtistExists;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ArtixtExistValidator implements ConstraintValidator<ArtistExists, Long> {

    @Autowired
    private ArtistService artistService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value != null ? artistService.existsById(value) : false;
    }
}
