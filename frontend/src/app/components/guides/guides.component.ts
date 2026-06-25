import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FilterMatchMode, FilterService, SelectItem } from 'primeng/api';
import { TableModule } from 'primeng/table';

import { GuideService } from '../../services/guide.service';
import { Guide } from '../../models/guide.model';

@Component({
  standalone: true,
  selector: 'app-guides',
  imports: [CommonModule, FormsModule, TableModule],
  providers: [FilterService],
  templateUrl: './guides.component.html',
  styleUrls: ['./guides.component.css']
})
export class GuideComponent implements OnInit {

  private readonly guideService = inject(GuideService);
  private readonly filterService = inject(FilterService);

  guides: Guide[] = [];
  cols: Array<{ field: keyof Guide; header: string }> = [];
  matchModeOptions: SelectItem[] = [];
  customFilterName = 'custom-equals';
  form: Guide = { nom: '' };
  message = '';

  ngOnInit(): void {
    this.registerCustomFilter();
    this.cols = [
      { field: 'nom', header: 'Nom' }
    ];
    this.matchModeOptions = [
      { label: 'Égal à', value: this.customFilterName },
      { label: 'Commence par', value: FilterMatchMode.STARTS_WITH },
      { label: 'Contient', value: FilterMatchMode.CONTAINS }
    ];
    this.loadGuides();
  }

  private registerCustomFilter(): void {
    this.filterService.register(this.customFilterName, (value: unknown, filter: unknown): boolean => {
      if (filter === undefined || filter === null || filter.toString().trim() === '') {
        return true;
      }
      if (value === undefined || value === null) {
        return false;
      }
      return value.toString().toLowerCase() === filter.toString().toLowerCase();
    });
  }

  loadGuides(): void {
    this.message = '';
    this.guideService.getAllGuides().subscribe({
      next: (response) => {
        this.guides = response || [];
      },
      error: () => (this.message = 'Impossible de charger la liste des guides.')
    });
  }

  createGuide(): void {
    this.message = '';
    if (!this.form.nom.trim()) {
      this.message = 'Le nom du guide est requis.';
      return;
    }

    this.guideService.createGuide(this.form).subscribe({
      next: () => {
        this.message = 'Guide créé avec succès.';
        this.form = { nom: '' };
        this.loadGuides();
        alert('Création réussie !');
      },
      error: () => (this.message = 'Erreur lors de la création du guide.')
    });
  }

  deleteGuide(guide: Guide): void {
    this.message = '';
    const confirmed = confirm(`Confirmer la suppression du guide ${guide.nom} ?`);
    if (!confirmed) {
      return;
    }

    this.guideService.deleteGuide(guide.nom).subscribe({
      next: () => {
        alert('Suppression réussie !');
        this.message = 'Guide supprimé.';
        this.loadGuides();
      },
      error: () => (this.message = 'Erreur lors de la suppression du guide.')
    });
  }
}