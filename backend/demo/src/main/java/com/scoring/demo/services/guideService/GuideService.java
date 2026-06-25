package com.scoring.demo.services.guideService;

import com.scoring.demo.DTOs.GuideDto;
import com.scoring.demo.entities.Guide;
import java.util.List;

public interface GuideService {

    Guide createGuide(GuideDto guideDto);

    Guide getGuideByNom(String nom);

    List<GuideDto> getAllGuides();

    void deleteGuideByNom(String nom);
}