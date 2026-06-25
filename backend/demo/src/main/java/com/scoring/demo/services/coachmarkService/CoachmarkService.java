package com.scoring.demo.services.coachmarkService;

import com.scoring.demo.DTOs.CoachmarkDto;
import com.scoring.demo.DTOs.GuideDto;
import com.scoring.demo.DTOs.UserDto;
import java.util.List;

public interface CoachmarkService {

    // Nouvelle méthode combinée (Sauvegarde ou Mise à jour)
    CoachmarkDto saveOrUpdateCoachmark(CoachmarkDto coachmarkDto);

    // Récupération des scores de l'utilisateur
    List<CoachmarkDto> getUserScores(UserDto user);

    List<CoachmarkDto> getGuideUsers(GuideDto guideDto);
}