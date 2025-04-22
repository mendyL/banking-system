import { Component, EventEmitter, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { ButtonModule } from 'primeng/button';
import { Direction, Partner, ProcessedFlowType } from '../../../core/models/partner.model';
import { SelectModule } from 'primeng/select';
import { FloatLabelModule } from 'primeng/floatlabel';


@Component({
  selector: 'app-partner-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    SelectModule,
    ButtonModule,
    InputTextModule,
    InputGroupModule,
    InputGroupAddonModule,
    FloatLabelModule
  ],
  templateUrl: './partner-form.component.html',
  styleUrl: './partner-form.component.scss'
})
export class PartnerFormComponent {
  @Output() savePartner = new EventEmitter<Partner>();
  @Output() cancelForm = new EventEmitter<void>();
  
  private readonly fb = inject(FormBuilder);
  
  partnerForm: FormGroup = this.fb.group({
    alias: ['', [Validators.required]],
    type: ['', [Validators.required]],
    direction: [Direction.INBOUND, [Validators.required]],
    application: [''],
    processedFlowType: [ProcessedFlowType.MESSAGE, [Validators.required]],
    description: ['', [Validators.required]]
  });
  
  directionOptions = [
    { label: 'Inbound', value: Direction.INBOUND },
    { label: 'Outbound', value: Direction.OUTBOUND }
  ];
  
  flowTypeOptions = [
    { label: 'Message', value: ProcessedFlowType.MESSAGE },
    { label: 'Alerting', value: ProcessedFlowType.ALERTING },
    { label: 'Notification', value: ProcessedFlowType.NOTIFICATION }
  ];
  
  onSubmit(): void {
    if (this.partnerForm.valid) {
      this.savePartner.emit(this.partnerForm.value);
      this.partnerForm.reset();
    } else {
      Object.keys(this.partnerForm.controls).forEach(key => {
        const control = this.partnerForm.get(key);
        control?.markAsTouched();
      });
    }
  }
  
  onCancel(): void {
    this.cancelForm.emit();
  }
}
