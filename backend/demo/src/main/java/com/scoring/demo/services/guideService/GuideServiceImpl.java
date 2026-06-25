package com.scoring.demo.services.guideService;

import com.scoring.demo.DTOs.GuideDto;
import com.scoring.demo.entities.Guide;
import com.scoring.demo.repositories.GuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GuideServiceImpl implements GuideService {

    private final GuideRepository guideRepository;

    @Override
    public Guide createGuide(GuideDto guideDto) {
        if (guideRepository.findByNom(guideDto.getNom()).isPresent()) {
            throw new RuntimeException("Un guide avec le nom '" + guideDto.getNom() + "' existe déjà.");
        }

        Guide guide = new Guide();
        guide.setNom(guideDto.getNom());

        return guideRepository.save(guide);
    }

    @Override
    public Guide getGuideByNom(String nom) {
        return guideRepository.findByNom(nom)
                .orElseThrow(() -> new RuntimeException("Guide introuvable avec le nom : " + nom));
    }

    @Override
    public List<GuideDto> getAllGuides() {
        List<Guide> guides = guideRepository.findAll();

        return guides.stream()
                .map(guide -> {
                    GuideDto dto = new GuideDto();
                    dto.setNom(guide.getNom());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGuideByNom(String nom) {
         Guide guide = guideRepository.findByNom(nom)
                .orElseThrow(() -> new RuntimeException("Impossible de supprimer : Guide introuvable avec le nom : " + nom));

        guideRepository.deleteByNom(nom);
    }
}