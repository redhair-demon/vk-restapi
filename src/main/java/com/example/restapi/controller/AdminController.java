package com.example.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JdbcUserDetailsManager jdbcUserDetailsManager;

    @PostMapping("/users")
    public Boolean createUser(
            @RequestBody User user
    ) {
        List<GrantedAuthority> grantedAuthorities = user.authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.username,
                passwordEncoder.encode(user.password),
                grantedAuthorities
                );
        jdbcUserDetailsManager.createUser(userDetails);
        return true;
    }

    @DeleteMapping("/users/{username}")
    public Boolean deleteUser(
            @PathVariable String username
    ) {
        jdbcUserDetailsManager.deleteUser(username);
        return true;
    }

    public static class User {
        public String username;
        public String password;
        public List<String> authorities;
    }
}
