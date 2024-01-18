package com.example.engineer.Controller;

import com.example.engineer.Model.DTO.User.UserRegistrationDto;
import com.example.engineer.Service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> register(@RequestBody String userRegistrationDto) {
        return Optional.ofNullable(new Gson().fromJson(userRegistrationDto, UserRegistrationDto.class))
                .stream()
                .filter(userDto -> !userService.existsByEmail(userDto.getEmail()))
                .findFirst()
                .map(userDto -> {
                    userService.register(userDto);
                    return ResponseEntity.ok("User registered successfully");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken!"));
    }
}
