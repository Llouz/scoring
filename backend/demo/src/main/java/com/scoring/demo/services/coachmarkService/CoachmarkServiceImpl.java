package com.scoring.demo.services.coachmarkService;

import com.scoring.demo.DTOs.CoachmarkDto;
import com.scoring.demo.DTOs.GuideDto;
import com.scoring.demo.DTOs.UserDto;
import com.scoring.demo.entities.CoachmarkState;
import com.scoring.demo.entities.Guide;
import com.scoring.demo.entities.User;
import com.scoring.demo.repositories.CoachmarkStateRepository;
import com.scoring.demo.repositories.GuideRepository;
import com.scoring.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CoachmarkServiceImpl implements CoachmarkService {

    private final CoachmarkStateRepository coachmarkStateRepository;
    private final UserRepository userRepository;
    private final GuideRepository guideRepository;

    @Override
    public CoachmarkDto saveOrUpdateCoachmark(CoachmarkDto coachmarkDto) {
        UserDto userDto = coachmarkDto.getUser();
        GuideDto guideDto = coachmarkDto.getGuide();

        // 1. Récupération de l'utilisateur et du guide
        User user = userRepository.findByNomAndPrenom(userDto.getNom(), userDto.getPrenom())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Utilisateur introuvable : %s %s", userDto.getNom(), userDto.getPrenom())
                ));

        Guide guide = guideRepository.findByNom(guideDto.getNom())
                .orElseThrow(() -> new RuntimeException("Guide introuvable avec le nom : " + guideDto.getNom()));

        // 2. Recherche de la relation existante ou création d'une nouvelle
        CoachmarkState state = coachmarkStateRepository.findByUserUuidAndGuideUuid(user.getUuid(), guide.getUuid())
                .map(existingState -> {
                    // Si elle existe, on met à jour le score (seulement si un score est passé dans le DTO)
                    if (coachmarkDto.getScore() != null) {
                        existingState.setScore(coachmarkDto.getScore());
                    }
                    return existingState;
                })
                .orElseGet(() -> {
                    // Si elle n'existe pas, on crée une nouvelle instance
                    CoachmarkState newState = new CoachmarkState();
                    newState.setUser(user);
                    newState.setGuide(guide);
                    Integer initialScore = (coachmarkDto.getScore() != null) ? coachmarkDto.getScore() : 0;
                    newState.setScore(initialScore);
                    return newState;
                });

        // 3. Sauvegarde
        CoachmarkState savedState = coachmarkStateRepository.save(state);

        return convertToDto(savedState);
    }


    @Override
    public List<CoachmarkDto> getUserScores(UserDto userDto) {

        User user = userRepository.findByNomAndPrenom(userDto.getNom(), userDto.getPrenom())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Utilisateur introuvable : %s %s", userDto.getNom(), userDto.getPrenom())
                ));

        List<CoachmarkState> states = coachmarkStateRepository.findByUserUuid(user.getUuid());

        return states.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CoachmarkDto> getGuideUsers(GuideDto guideDto) {
        // 1. Récupération du guide par son nom pour valider son existence
        Guide guide = guideRepository.findByNom(guideDto.getNom())
                .orElseThrow(() -> new RuntimeException("Guide introuvable avec le nom : " + guideDto.getNom()));

        // 2. Récupération de toutes les relations associées à ce guide
        // (Adapte le nom de la méthode du repository selon ce que tu as défini, ex: findByGuide)
        List<CoachmarkState> states = coachmarkStateRepository.findByGuideUuid(guide.getUuid());

        // 3. Conversion de la liste d'entités en liste de DTOs
        return states.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CoachmarkDto convertToDto(CoachmarkState state) {
        CoachmarkDto dto = new CoachmarkDto();
        dto.setScore(state.getScore());

        if (state.getUser() != null) {
            UserDto uDto = new UserDto();
            uDto.setNom(state.getUser().getNom());
            uDto.setPrenom(state.getUser().getPrenom());
            dto.setUser(uDto);
        }

        if (state.getGuide() != null) {
            GuideDto gDto = new GuideDto();
            gDto.setNom(state.getGuide().getNom());
            dto.setGuide(gDto);
        }

        return dto;
    }



}