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
@RequestMapping("/users")
public class UsersController {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://jsonplaceholder.typicode.com/users")
            .defaultHeaders(httpHeaders -> {
                httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            })
            .build();

    @GetMapping({"", "/"})
    public User[] getAllBy(
            User user
    ) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParamIfPresent("id", Optional.ofNullable(user.id))
                        .queryParamIfPresent("name", Optional.ofNullable(user.name))
                        .queryParamIfPresent("username", Optional.ofNullable(user.username))
                        .queryParamIfPresent("email", Optional.ofNullable(user.email)).queryParamIfPresent("phone", Optional.ofNullable(user.phone))
                        .queryParamIfPresent("website", Optional.ofNullable(user.website)).build()
                )
                .retrieve()
                .bodyToMono(User[].class)
                .block();
    }

    @GetMapping({"/{id}", "/{id}/"})
    @Cacheable(cacheNames = "users", key = "#id")
    public User getById(
        @PathVariable Integer id
    ) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }

    @GetMapping({"/{id}/albums", "/{id}/albums/"})
    public AlbumsController.Album[] getAlbumsById(
            @PathVariable Integer id,
            AlbumsController.Album album
    ) {
        album.id = id;
        return new AlbumsController().getAllBy(album);
    }

    @GetMapping({"/{id}/todos", "/{id}/todos/"})
    public Todo[] getTodosById(
            @PathVariable Integer id,
            Todo todo
    ) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/todos")
                        .queryParamIfPresent("id", Optional.ofNullable(todo.id))
                        .queryParamIfPresent("title", Optional.ofNullable(todo.title))
                        .queryParamIfPresent("completed", Optional.ofNullable(todo.completed))
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Todo[].class)
                .block();
    }

    @GetMapping({"/{id}/posts", "/{id}/posts/"})
    public PostsController.Post[] getPostsById(
            @PathVariable Integer id,
            PostsController.Post post
    ) {
        post.userId = id;
        return new PostsController().getAllBy(post);
    }

    @PostMapping()
    @CachePut(cacheNames = "users", key = "#user.id")
    public User postUser(
            @RequestBody User user
    ) {
        return webClient
                .post()
                .bodyValue(user)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }

    @PutMapping("/{id}")
    @CachePut(cacheNames = "users", key = "#id")
    public User putUser(
            @PathVariable Integer id,
            @RequestBody User user
    ) {
        return webClient
                .put()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .build(id)
                )
                .bodyValue(user)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }

    @DeleteMapping("/{id}")
    @CacheEvict(cacheNames = "users", key = "#id")
    public User deleteUser(
            @PathVariable Integer id
    ) {
        return webClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }

    public static class User {
        public Integer id;
        public String name;
        public String username;
        public String email;
        public Address address;
        public String phone;
        public String website;
        public Company company;

        public static class Address {
            public String street;
            public String suite;
            public String city;
            public String zipcode;
            public Geo geo;

            public static class Geo {
                public String lat;
                public String lng;
            }
        }
        public static class Company {
            public String name;
            public String catchPhone;
            public String bs;
        }
    }

    public static class Todo {
        public Integer userId;
        public Integer id;
        public String title;
        public Boolean completed;
    }
}
