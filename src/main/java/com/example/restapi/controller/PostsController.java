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
@RequestMapping("/posts")
public class PostsController {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://jsonplaceholder.typicode.com/posts")
            .defaultHeaders(httpHeaders -> {
                httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            })
            .build();

    @GetMapping({"", "/"})
    public Post[] getAllBy(
            Post post
    ) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParamIfPresent("userId", Optional.ofNullable(post.userId))
                        .queryParamIfPresent("id", Optional.ofNullable(post.id))
                        .queryParamIfPresent("title", Optional.ofNullable(post.title))
                        .queryParamIfPresent("body", Optional.ofNullable(post.body))
                        .build()
                )
                .retrieve()
                .bodyToMono(Post[].class)
                .block();
    }

    @GetMapping({"/{id}", "/{id}/"})
    @Cacheable(cacheNames = "posts", key = "#id")
    public Post getById(
            @PathVariable Integer id
    ) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Post.class)
                .block();
    }

    @GetMapping({"/{postId}/comments", "/{postId}/comments/"})
    public Comment[] getCommentsById(
            @PathVariable Integer postId,
            Comment comment
    ) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{postId}/comments")
                        .queryParamIfPresent("id", Optional.ofNullable(comment.id))
                        .queryParamIfPresent("name", Optional.ofNullable(comment.name))
                        .queryParamIfPresent("email", Optional.ofNullable(comment.email))
                        .queryParamIfPresent("body", Optional.ofNullable(comment.body))
                        .build(postId)
                )
                .retrieve()
                .bodyToMono(Comment[].class)
                .block();
    }

    @PostMapping()
    @CachePut(cacheNames = "posts", key = "#body.id")
    public Post postPost(
            @RequestBody Post body
    ) {
        return webClient
                .post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Post.class)
                .block();
    }

    @PutMapping({"/{id}"})
    @CachePut(cacheNames = "posts", key = "#id")
    public Post putPost(
            @RequestBody Post body,
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
                .bodyToMono(Post.class)
                .block();
    }

    @DeleteMapping({"/{id}"})
    @CacheEvict(cacheNames = "posts", key = "#id")
    public Post deletePost(
            @PathVariable Integer id
    ) {
        return webClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Post.class)
                .block();
    }

    public static class Post {
        public Integer userId;
        public Integer id;
        public String title;
        public String body;
    }

    public static class Comment {
        public Integer postId;
        public Integer id;
        public String name;
        public String email;
        public String body;
    }
}
