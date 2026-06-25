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

        User user = userRepository.findByNomAndPrenom(userDto.getNom(), userDto.getPrenom())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Utilisateur introuvable : %s %s", userDto.getNom(), userDto.getPrenom())
                ));

        Guide guide = guideRepository.findByNom(guideDto.getNom())
                .orElseThrow(() -> new RuntimeException("Guide introuvable avec le nom : " + guideDto.getNom()));

        CoachmarkState state = coachmarkStateRepository.findByUserUuidAndGuideUuid(user.getUuid(), guide.getUuid())
                .map(existingState -> {
                    if (coachmarkDto.getScore() != null) {
                        existingState.setScore(coachmarkDto.getScore());
                    }
                    return existingState;
                })
                .orElseGet(() -> {
                    CoachmarkState newState = new CoachmarkState();
                    newState.setUser(user);
                    newState.setGuide(guide);
                    Integer initialScore = (coachmarkDto.getScore() != null) ? coachmarkDto.getScore() : 0;
                    newState.setScore(initialScore);
                    return newState;
                });

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
        Guide guide = guideRepository.findByNom(guideDto.getNom())
                .orElseThrow(() -> new RuntimeException("Guide introuvable avec le nom : " + guideDto.getNom()));

        List<CoachmarkState> states = coachmarkStateRepository.findByGuideUuid(guide.getUuid());

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