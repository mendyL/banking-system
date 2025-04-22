import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService as PrimeMessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { PartnerService } from '../../../core/services/partner.service';
import { PartnerFormComponent } from '../partner-form/partner-form.component';
import { Partner } from '../../../core/models/partner.model';

@Component({
  selector: 'app-partner-list',
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    CardModule,
    ProgressSpinnerModule,
    ConfirmDialogModule,
    ToastModule,
    DialogModule,
    PartnerFormComponent
  ],
  providers: [ConfirmationService, PrimeMessageService],
  templateUrl: './partner-list.component.html',
  styleUrl: './partner-list.component.scss'
})
export class PartnerListComponent implements OnInit {
  private readonly partnerService = inject(PartnerService);
  private readonly primeMessageService = inject(PrimeMessageService);
  private readonly confirmationService = inject(ConfirmationService);

  
  readonly partners = this.partnerService.partners;
  readonly loading = this.partnerService.isLoading;
  readonly error = this.partnerService.error;
  
  showPartnerDialog = false;
  
  ngOnInit(): void {
    this.loadPartners();
  }
  
  loadPartners(): void {
    this.partnerService.loadPartners().subscribe();
  }
  
  openNewPartnerDialog(): void {
    this.showPartnerDialog = true;
  }
  
  hideDialog(): void {
    this.showPartnerDialog = false;
  }
  
  savePartner(partner: Partner): void {
    this.partnerService.addPartner(partner).subscribe({
      next: () => {
        this.primeMessageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Partner added successfully'
        });
        this.hideDialog();
      },
      error: (err) => {
        this.primeMessageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to add partner'
        });
        console.error('Error adding partner:', err);
      }
    });
  }

  confirmDeletePartner(partner: Partner): void {
    this.confirmationService.confirm({
      message: `Are you sure you want to delete the partner "${partner.alias}"?`,
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        if (partner.id) {
          this.deletePartner(partner.id);
        }
      }
    });
  }
  
  deletePartner(id: string): void {
    this.partnerService.deletePartner(id).subscribe({
      next: () => {
        this.primeMessageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Partner deleted successfully'
        });
      },
      error: (err) => {
        this.primeMessageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to delete partner'
        });
        console.error('Error deleting partner:', err);
      }
    });
  }
  
}
