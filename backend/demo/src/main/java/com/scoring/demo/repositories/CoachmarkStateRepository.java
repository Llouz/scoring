package com.scoring.demo.repositories;

import com.scoring.demo.entities.CoachmarkState;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional; // 👈 Assure-toi que cet import est présent
import java.util.UUID;

public interface CoachmarkStateRepository extends JpaRepository<CoachmarkState, Long> {

    Optional<CoachmarkState> findByUserUuidAndGuideUuid(UUID userUuid, UUID guideUuid);

    List<CoachmarkState> findByUserUuid(UUID userUuid);

    List<CoachmarkState> findByGuideUuid(UUID uuid);
}