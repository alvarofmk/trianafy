package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

}
