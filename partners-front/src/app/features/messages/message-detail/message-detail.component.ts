import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Message } from '../../../core/models/message.model';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';

@Component({
  selector: 'app-message-detail',
  standalone: true,
  imports: [CommonModule, CardModule, DividerModule],
  templateUrl: './message-detail.component.html',
  styleUrl: './message-detail.component.scss'
})
export class MessageDetailComponent {
  @Input({ required: true }) message!: Message;
}
