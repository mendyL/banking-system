import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressSpinnerModule } from 'primeng/progressspinner';

@Component({
  selector: 'app-loading-spinner',
  standalone: true,
  imports: [CommonModule, ProgressSpinnerModule],
  template: `
    <div class="flex justify-content-center align-items-center" [style.height]="height">
      <p-progressSpinner [style]="{ width: '50px', height: '50px' }"></p-progressSpinner>
      @if (message) {
        <span  class="ml-2">{{ message }}</span>
      }
    </div>
  `,
  styles: []
})
export class LoadingSpinnerComponent {
  @Input() message = 'Loading...';
  @Input() height = '100px';
}
