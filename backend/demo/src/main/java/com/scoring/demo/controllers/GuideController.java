package com.scoring.demo.controllers;

import com.scoring.demo.DTOs.GuideDto;
import com.scoring.demo.entities.Guide;
import com.scoring.demo.services.guideService.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guides")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // À ajuster selon tes configurations Angular si nécessaire
public class GuideController {

    private final GuideService guideService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createGuide(@RequestBody GuideDto guideDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Guide savedGuide = guideService.createGuide(guideDto);
            response.put("data", savedGuide);
            response.put("message", "Guide créé avec succès");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("data", null);
            response.put("message", "Erreur lors de la création du guide : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> getGuideByNom(@RequestParam String nom) {
        Map<String, Object> response = new HashMap<>();
        try {
            Guide guide = guideService.getGuideByNom(nom);
            response.put("data", guide);
            response.put("message", "Guide trouvé");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("data", null);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<GuideDto>> getAllGuides() {
        List<GuideDto> guides = guideService.getAllGuides();
        return ResponseEntity.ok(guides);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteGuideByNom(@RequestParam String nom) {
        Map<String, Object> response = new HashMap<>();
        try {
            guideService.deleteGuideByNom(nom);
            response.put("data", null);
            response.put("message", "Guide supprimé avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("data", null);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}