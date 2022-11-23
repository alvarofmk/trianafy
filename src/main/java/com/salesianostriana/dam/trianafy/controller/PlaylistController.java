package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

}
