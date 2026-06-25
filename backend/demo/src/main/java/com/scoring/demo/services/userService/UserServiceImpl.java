package com.scoring.demo.services.userService;

import com.scoring.demo.DTOs.UserDto;
import com.scoring.demo.entities.User;
import com.scoring.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(UserDto userDto) {

         boolean exists = userRepository.findByNomAndPrenom(userDto.getNom(), userDto.getPrenom()).isPresent();

        if (exists) {
            throw new RuntimeException(
                    String.format("L'utilisateur '%s %s' existe déjà.", userDto.getNom(), userDto.getPrenom())
            );
        }

        User user = new User();
        user.setNom(userDto.getNom());
        user.setPrenom(userDto.getPrenom());

        return userRepository.save(user);
    }

    @Override
    public User getUserByNomAndPrenom(String nom, String prenom) {
        return userRepository.findByNomAndPrenom(nom, prenom)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Utilisateur introuvable avec le nom '%s' et prénom '%s'", nom, prenom)
                ));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setNom(user.getNom());
                    dto.setPrenom(user.getPrenom());
                    return dto;
                })
                .toList();
    }

    @Override
    public void deleteUserByNomAndPrenom(String nom, String prenom) {

        User user = userRepository.findByNomAndPrenom(nom, prenom)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Impossible de supprimer : Utilisateur introuvable (%s %s)", nom, prenom)
                ));

        userRepository.deleteByNomAndPrenom(nom, prenom);
    }
}