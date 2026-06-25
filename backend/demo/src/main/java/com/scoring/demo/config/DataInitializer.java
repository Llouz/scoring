package com.scoring.demo.config;

import com.scoring.demo.DTOs.CoachmarkDto;
import com.scoring.demo.DTOs.GuideDto;
import com.scoring.demo.DTOs.UserDto;
import com.scoring.demo.repositories.CoachmarkStateRepository;
import com.scoring.demo.repositories.GuideRepository;
import com.scoring.demo.repositories.UserRepository;
import com.scoring.demo.services.coachmarkService.CoachmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GuideRepository guideRepository;
    private final CoachmarkService coachmarkService;
    private final CoachmarkStateRepository coachmarkStateRepository;

    @Override
    public void run(String... args) throws Exception {

        // 1. On évite de dupliquer les données à chaque redémarrage (DevTools)
        if (userRepository.count() > 0 || guideRepository.count() > 0) {
            System.out.println("👉 Base de données déjà initialisée. Passage de l'étape de création.");
            return;
        }

        System.out.println("🚀 Début de l'initialisation des données...");

        // 2. Initialisation des 20 Utilisateurs
        for (int i = 1; i <= 20; i++) {
            com.scoring.demo.entities.User user = new com.scoring.demo.entities.User();
            user.setNom("Nom" + i);
            user.setPrenom("Prenom" + i);
            userRepository.save(user);
        }
        System.out.println("✅ 20 utilisateurs créés avec succès !");

        // 3. Initialisation des 20 Guides
        for (int i = 1; i <= 20; i++) {
            com.scoring.demo.entities.Guide guide = new com.scoring.demo.entities.Guide();
            guide.setNom("Guide_" + i);
            guideRepository.save(guide);
        }
        System.out.println("✅ 20 guides créés avec succès !");

        // 4. Initialisation de 12 CoachmarkStates avec la nouvelle méthode combinée
        for (int i = 1; i <= 12; i++) {
            CoachmarkDto coachmarkDto = new CoachmarkDto();

            // Préparation du UserDto correspondant aux utilisateurs créés au-dessus
            UserDto uDto = new UserDto();
            uDto.setNom("Nom" + i);
            uDto.setPrenom("Prenom" + i);

            // Préparation du GuideDto correspondant
            GuideDto gDto = new GuideDto();
            gDto.setNom("Guide_" + i);

            coachmarkDto.setUser(uDto);
            coachmarkDto.setGuide(gDto);
            coachmarkDto.setScore(i % 5); // Génère des scores distribués entre 0 et 4

            // Appel de la NOUVELLE logique métier de ton service
            coachmarkService.saveOrUpdateCoachmark(coachmarkDto);
        }
        System.out.println("✅ 12 CoachmarkStates initialisés avec succès !");
        System.out.println("🎉 Initialisation complète terminée !");
    }
}