package com.scoring.demo.repositories;

import com.scoring.demo.entities.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface GuideRepository extends JpaRepository<Guide, UUID> {

    Optional<Guide> findByNom(String nom);

    void deleteByNom(String nom);
}