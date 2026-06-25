import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FilterMatchMode, FilterService, SelectItem } from 'primeng/api';
import { TableModule } from 'primeng/table';

import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  standalone: true,
  selector: 'app-users',
  imports: [CommonModule, FormsModule, TableModule],
  providers: [FilterService],
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UserComponent implements OnInit {

  private readonly userService = inject(UserService);
  private readonly filterService = inject(FilterService);

  users: User[] = [];
  cols: Array<{ field: keyof User; header: string }> = [];
  matchModeOptions: SelectItem[] = [];
  customFilterName = 'custom-equals';
  form: User = { nom: '', prenom: '' };
  message = '';

  ngOnInit(): void {
    this.registerCustomFilter();
    this.cols = [
      { field: 'nom', header: 'Nom' },
      { field: 'prenom', header: 'Prénom' }
    ];
    this.matchModeOptions = [
      { label: 'Égal à', value: this.customFilterName },
      { label: 'Commence par', value: FilterMatchMode.STARTS_WITH },
      { label: 'Contient', value: FilterMatchMode.CONTAINS }
    ];
    this.loadUsers();
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

  loadUsers(): void {
    this.message = '';
    this.userService.getAllUsers().subscribe({
      next: (response) => {
        this.users = response.data || [];
      },
      error: () => (this.message = 'Impossible de charger la liste des utilisateurs.')
    });
  }

  createUser(): void {
    this.message = '';
    if (!this.form.nom.trim() || !this.form.prenom.trim()) {
      this.message = 'Nom et prénom sont requis pour créer un utilisateur.';
      return;
    }

    this.userService.createUser(this.form).subscribe({
      next: () => {
        this.message = 'Utilisateur créé avec succès.';
        this.form = { nom: '', prenom: '' };
        this.loadUsers();
        alert('Création réussie !');
      },
      error: () => (this.message = 'Erreur lors de la création de l utilisateur.')
    });
  }

  deleteUser(user: User): void {
    this.message = '';
    const confirmed = confirm(`Confirmer la suppression de ${user.nom} ${user.prenom} ?`);
    if (!confirmed) {
      return;
    }

    this.userService.deleteUser(user.nom, user.prenom).subscribe({
      next: () => {
        alert('Suppression réussie !');
        this.message = 'Utilisateur supprimé.';
        this.loadUsers();
      },
      error: () => (this.message = 'Erreur lors de la suppression de l utilisateur.')
    });
  }
}
