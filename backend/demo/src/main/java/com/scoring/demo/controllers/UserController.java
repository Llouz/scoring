package com.scoring.demo.controllers;

import com.scoring.demo.DTOs.UserDto;
import com.scoring.demo.entities.ApiResponse;
import com.scoring.demo.entities.User;
import com.scoring.demo.services.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserDto userDto) {
        User createdUser = userService.createUser(userDto);
        ApiResponse response = new ApiResponse();
        response.setMessage("Utilisateur créé avec succès");
        response.setData(createdUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getUserByNomAndPrenom(
            @RequestParam String nom,
            @RequestParam String prenom) {

        User user = userService.getUserByNomAndPrenom(nom, prenom);
        ApiResponse response = new ApiResponse();
        response.setMessage("Utilisateur récupéré avec succès");
        response.setData(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        ApiResponse response = new ApiResponse();
        response.setMessage("Liste des utilisateurs récupérée avec succès");
        response.setData(users);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteUserByNomAndPrenom(
            @RequestParam String nom,
            @RequestParam String prenom) {

        userService.deleteUserByNomAndPrenom(nom, prenom);
        ApiResponse response = new ApiResponse();
        response.setMessage("Utilisateur supprimé avec succès");
        response.setData(null);
        return ResponseEntity.ok(response);
    }
}