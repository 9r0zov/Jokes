package com.example.jokes.controller;

import com.example.jokes.model.Joke;
import com.example.jokes.service.JokeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JokeController {

    private final JokeService jokeService;

    @GetMapping("/jokes")
    public ResponseEntity<List<Joke>> getJoke(
            @Valid
            @RequestParam(defaultValue = "5")
            @Max(value = 100, message = "You can't get more than 100 jokes at once")
            Integer count) {
        return ResponseEntity.ok(jokeService.retrieveJokes(count));
    }

}
