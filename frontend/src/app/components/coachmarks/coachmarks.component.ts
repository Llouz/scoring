import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CoachmarkService } from '../../services/coachmark.service';
import { GuideService } from '../../services/guide.service';
import { UserService } from '../../services/user.service';
import { Coachmark } from '../../models/coachmark.model';
import { Guide } from '../../models/guide.model';
import { User } from '../../models/user.model';

@Component({
  standalone: true,
  selector: 'app-coachmarks',
  imports: [CommonModule, FormsModule],
  templateUrl: './coachmarks.component.html',
  styleUrls: ['./coachmarks.component.css']
})
export class CoachmarkComponent implements OnInit {
  private readonly coachmarkService = inject(CoachmarkService);
  private readonly userService = inject(UserService);
  private readonly guideService = inject(GuideService);

  showScores = true;
  users: User[] = [];
  guides: Guide[] = [];
  associateUserSuggestions: User[] = [];
  updateUserSuggestions: User[] = [];
  searchUserSuggestions: User[] = [];
  associateGuideSuggestions: Guide[] = [];
  updateGuideSuggestions: Guide[] = [];

  searchNom = '';
  searchPrenom = '';
  searchGuideNom = '';
  userScores: Coachmark[] = [];
  message = '';

  // ← NOUVEAU : indique si on est en mode édition
  isEditMode = false;

  associate: Coachmark = {
    user: { nom: '', prenom: '' },
    guide: { nom: '' },
    score: 0
  };

  update: Coachmark = {
    user: { nom: '', prenom: '' },
    guide: { nom: '' },
    score: 0
  };

  ngOnInit(): void {
    this.message = '';
    this.loadUsers();
    this.loadGuides();
  }

  private loadUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (response) => {
        this.users = response.data || [];
      },
      error: () => {
        this.message = 'Impossible de charger les utilisateurs pour les suggestions.';
      }
    });
  }

  private loadGuides(): void {
    this.guideService.getAllGuides().subscribe({
      next: (response) => {
        this.guides = response || [];
      },
      error: () => {
        this.message = 'Impossible de charger les guides pour les suggestions.';
      }
    });
  }

  private getUserSuggestions(nom: string, prenom: string): User[] {
    const nomQuery = nom.trim().toLowerCase();
    const prenomQuery = prenom.trim().toLowerCase();

    if (!nomQuery && !prenomQuery) {
      return [];
    }

    return this.users
      .filter((user) => {
        const matchesNom = nomQuery ? user.nom.toLowerCase().startsWith(nomQuery) : true;
        const matchesPrenom = prenomQuery ? user.prenom.toLowerCase().startsWith(prenomQuery) : true;
        return matchesNom && matchesPrenom;
      })
      .slice(0, 8);
  }

  private getGuideSuggestions(query: string): Guide[] {
    const normalized = query.trim().toLowerCase();
    if (!normalized) {
      return [];
    }

    return this.guides
      .filter((guide) => guide.nom.toLowerCase().startsWith(normalized))
      .slice(0, 8);
  }

  updateUserSuggestionList(target: 'associate' | 'update' | 'search'): void {
    const targetNom =
      target === 'associate'
        ? this.associate.user.nom
        : target === 'update'
        ? this.update.user.nom
        : this.searchNom;
    const targetPrenom =
      target === 'associate'
        ? this.associate.user.prenom
        : target === 'update'
        ? this.update.user.prenom
        : this.searchPrenom;

    const suggestions = this.getUserSuggestions(targetNom, targetPrenom);

    if (target === 'associate') {
      this.associateUserSuggestions = suggestions;
    } else if (target === 'update') {
      this.updateUserSuggestions = suggestions;
    } else {
      this.searchUserSuggestions = suggestions;
    }
  }

  selectSuggestedUser(user: User, target: 'associate' | 'update' | 'search'): void {
    if (target === 'associate') {
      this.associate.user.nom = user.nom;
      this.associate.user.prenom = user.prenom;
      this.associateUserSuggestions = [];
    } else if (target === 'update') {
      this.update.user.nom = user.nom;
      this.update.user.prenom = user.prenom;
      this.updateUserSuggestions = [];
    } else {
      this.searchNom = user.nom;
      this.searchPrenom = user.prenom;
      this.searchUserSuggestions = [];
    }
  }

  updateGuideSuggestionList(target: 'associate' | 'update'): void {
    const query = target === 'associate' ? this.associate.guide.nom : this.update.guide.nom;
    const suggestions = this.getGuideSuggestions(query);

    if (target === 'associate') {
      this.associateGuideSuggestions = suggestions;
    } else {
      this.updateGuideSuggestions = suggestions;
    }
  }

  selectSuggestedGuide(guide: Guide, target: 'associate' | 'update'): void {
    if (target === 'associate') {
      this.associate.guide.nom = guide.nom;
      this.associateGuideSuggestions = [];
    } else {
      this.update.guide.nom = guide.nom;
      this.updateGuideSuggestions = [];
    }
  }

  onSearchUserInput(): void {
    if (this.searchGuideNom.trim()) {
      this.searchGuideNom = '';
    }
    this.updateUserSuggestionList('search');
  }

  onSearchGuideInput(): void {
    if (this.searchNom.trim() || this.searchPrenom.trim()) {
      this.searchNom = '';
      this.searchPrenom = '';
    }
  }

  findScores(): void {
    this.message = '';

    if (this.searchGuideNom.trim()) {
      this.coachmarkService.getGuideUsers(this.searchGuideNom).subscribe({
        next: (response) => {
          this.userScores = response.data || [];
          this.message = this.userScores.length ? 'Scores récupérés.' : 'Aucun score trouvé pour ce guide.';
        },
        error: () => (this.message = 'Erreur lors de la récupération des scores.')
      });
      return;
    }

    if (!this.searchNom.trim() || !this.searchPrenom.trim()) {
      this.message = 'Nom et prénom sont requis pour récupérer les scores.';
      return;
    }

    this.coachmarkService.getUserScores(this.searchNom, this.searchPrenom).subscribe({
      next: (response) => {
        this.userScores = response.data || [];
        this.message = this.userScores.length ? 'Scores récupérés.' : 'Aucun score trouvé pour cet utilisateur.';
      },
      error: () => (this.message = 'Erreur lors de la récupération des scores.')
    });
  }

  // ← NOUVEAU : remplit le formulaire du haut et active le mode édition
  editScore(score: Coachmark): void {
    this.associate = {
      user: { nom: score.user.nom, prenom: score.user.prenom },
      guide: { nom: score.guide.nom },
      score: score.score
    };
    this.isEditMode = true;
    this.message = '';
    // Scroll vers le formulaire
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  // ← NOUVEAU : annule le mode édition
  cancelEdit(): void {
    this.isEditMode = false;
    this.associate = {
      user: { nom: '', prenom: '' },
      guide: { nom: '' },
      score: 0
    };
    this.message = '';
  }

  associateUser(): void {
    this.message = '';
    if (!this.associate.user.nom.trim() || !this.associate.user.prenom.trim() || !this.associate.guide.nom.trim()) {
      this.message = 'Tous les champs sont requis pour créer ou mettre à jour un score.';
      return;
    }

    this.coachmarkService.saveOrUpdate(this.associate).subscribe({
      next: () => {
        if (this.isEditMode) {
          alert('Modification réussie !');
          this.message = 'Score modifié avec succès.';
          // Met à jour la ligne dans la liste sans refaire une requête
          const idx = this.userScores.findIndex(
            s => s.user.nom === this.associate.user.nom &&
                 s.user.prenom === this.associate.user.prenom &&
                 s.guide.nom === this.associate.guide.nom
          );
          if (idx !== -1) {
            this.userScores[idx].score = this.associate.score;
          }
          this.isEditMode = false;
        } else {
          alert('Création réussie !');
          this.message = 'Association créée avec succès.';
        }
        this.associate = { user: { nom: '', prenom: '' }, guide: { nom: '' }, score: 0 };
      },
      error: () => (this.message = this.isEditMode
        ? 'Erreur lors de la modification du score.'
        : 'Erreur lors de l\'association utilisateur-guide.')
    });
  }

  updateScore(): void {
    this.message = '';
    if (!this.update.user.nom.trim() || !this.update.user.prenom.trim() || !this.update.guide.nom.trim()) {
      this.message = 'Tous les champs sont requis pour mettre à jour le score.';
      return;
    }

    this.coachmarkService.saveOrUpdate(this.update).subscribe({
      next: () => {
        alert('Mise à jour réussie !');
        this.message = 'Score mis à jour avec succès.';
      },
      error: () => (this.message = 'Erreur lors de la mise à jour du score.')
    });
  }
}