package com.example.restapi.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/albums")
public class AlbumsController {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://jsonplaceholder.typicode.com/albums")
            .defaultHeaders(httpHeaders -> {
                httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            })
            .build();

    @GetMapping({"", "/"})
    public Album[] getAllBy(
            Album album
    ) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParamIfPresent("userId", Optional.ofNullable(album.userId))
                        .queryParamIfPresent("id", Optional.ofNullable(album.id))
                        .queryParamIfPresent("title", Optional.ofNullable(album.title))
                        .build()
                )
                .retrieve()
                .bodyToMono(Album[].class)
                .block();
    }

    @GetMapping({"/{id}", "/{id}/"})
    @Cacheable(cacheNames = "albums", key = "#id")
    public Album getById(
            @PathVariable Integer id
    ) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Album.class)
                .block();
    }

    @GetMapping({"/{id}/photos", "/{id}/photos/"})
    public Photo[] getPhotosById(
            @PathVariable Integer id,
            Photo photo
    ) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/photos")
                        .queryParamIfPresent("id", Optional.ofNullable(photo.id))
                        .queryParamIfPresent("title", Optional.ofNullable(photo.title))
                        .queryParamIfPresent("url", Optional.ofNullable(photo.url))
                        .queryParamIfPresent("thumbnailUrl", Optional.ofNullable(photo.thumbnailUrl))
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Photo[].class)
                .block();
    }

    @PostMapping()
    @CachePut(cacheNames = "albums", key = "#body.id")
    public Album postAlbum(
            @RequestBody Album body
    ) {
        return webClient
                .post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Album.class)
                .block();
    }

    @PutMapping({"/{id}"})
    @CachePut(cacheNames = "albums", key = "#id")
    public Album putAlbum(
            @RequestBody Album body,
            @PathVariable Integer id
    ) {
        return webClient
                .put()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .build(id)
                )
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Album.class)
                .block();
    }

    @DeleteMapping({"/{id}"})
    @CacheEvict(cacheNames = "albums", key = "#id")
    public Album deleteAlbum(
            @PathVariable Integer id
    ) {
        return webClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Album.class)
                .block();
    }

    public static class Album {
        public Integer userId;
        public Integer id;
        public String title;
    }

    public static class Photo {
        public Integer albumId;
        public Integer id;
        public String title;
        public String url;
        public String thumbnailUrl;
    }
}
