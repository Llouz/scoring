package com.scoring.demo.controllers;

import com.scoring.demo.DTOs.CoachmarkDto;
import com.scoring.demo.DTOs.GuideDto;
import com.scoring.demo.DTOs.UserDto;
import com.scoring.demo.entities.ApiResponse;
import com.scoring.demo.services.coachmarkService.CoachmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coachmarks")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CoachmarkController {

    private final CoachmarkService coachmarkService;

    // Nouvel endpoint combiné : Crée ou met à jour le score
    @PostMapping("/save-or-update")
    public ResponseEntity<ApiResponse> saveOrUpdateCoachmark(@RequestBody CoachmarkDto coachmarkDto) {
        CoachmarkDto resultDto = coachmarkService.saveOrUpdateCoachmark(coachmarkDto);
        ApiResponse response = new ApiResponse();
        response.setMessage("Relation Coachmark traitée et enregistrée avec succès");
        response.setData(resultDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getUserScores(@RequestParam String nom, @RequestParam String prenom) {
        UserDto userDto = new UserDto();
        userDto.setNom(nom);
        userDto.setPrenom(prenom);
        List<CoachmarkDto> states = coachmarkService.getUserScores(userDto);
        ApiResponse response = new ApiResponse();
        response.setMessage("Scores récupérés pour l'utilisateur spécifié");
        response.setData(states);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/guide")
    public ResponseEntity<ApiResponse> getGuideUsers(@RequestParam String nom) {
        GuideDto guideDto = new GuideDto();
        guideDto.setNom(nom);

        List<CoachmarkDto> states = coachmarkService.getGuideUsers(guideDto);

        ApiResponse response = new ApiResponse();
        response.setMessage("Utilisateurs et scores récupérés pour le guide spécifié");
        response.setData(states);
        return ResponseEntity.ok(response);
    }
}