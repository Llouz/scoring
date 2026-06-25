package com.scoring.demo.services.userService;

import com.scoring.demo.DTOs.UserDto;
import com.scoring.demo.entities.User;
import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(UserDto user);

    User getUserByNomAndPrenom(String nom, String prenom);

    List<UserDto> getAllUsers();

    void deleteUserByNomAndPrenom(String nom, String prenom);

}