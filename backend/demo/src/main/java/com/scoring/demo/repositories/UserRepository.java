package com.scoring.demo.repositories;

import com.scoring.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByNomAndPrenom(String nom, String prenom);

    void deleteByNomAndPrenom(String nom, String prenom);
}