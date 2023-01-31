package com.salesianostriana.dam.trianafy.error.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ErrorInterface {

    HttpStatus getStatus();
    int getCode();
    String getMessage();
    String getPath();
    LocalDateTime getDate();
    List<SubErrorInterface> getSubErrors();

}
