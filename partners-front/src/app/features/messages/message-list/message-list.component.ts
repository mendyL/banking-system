import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { MessageService } from '../../../core/services/message.service';
import { MessageDetailComponent } from '../message-detail/message-detail.component';
import { DialogModule } from 'primeng/dialog';
import { Message } from '../../../core/models/message.model';
import { MessageModule } from 'primeng/message';


@Component({
  selector: 'app-message-list',
  standalone: true,
  imports: [
    CommonModule, 
    TableModule, 
    ButtonModule, 
    CardModule, 
    ProgressSpinnerModule,
    DialogModule,
    MessageDetailComponent,
    MessageModule
  ],
  templateUrl: './message-list.component.html',
  styleUrl: './message-list.component.scss'
})
export class MessageListComponent implements OnInit {

  private readonly messageService = inject(MessageService);
  
  // Expose signals
  readonly messages = this.messageService.messages;
  readonly loading = this.messageService.loading;
  readonly error = this.messageService.error;
  readonly selectedMessage = this.messageService.selectedMessage;
  
  showDetailDialog = false;
  
  ngOnInit(): void {
    this.loadMessages();
  }
  
  loadMessages(): void {
    this.messageService.getMessages().subscribe();
  }
  
  showMessageDetail(message: Message): void {
    this.messageService.selectedMessage.set(message);
    this.showDetailDialog = true;
  }
  
  hideDialog(): void {
    this.showDetailDialog = false;
    this.messageService.selectedMessage.set(null);
  }

}
