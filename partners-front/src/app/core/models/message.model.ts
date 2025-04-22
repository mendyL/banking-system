export interface Message {
    id: string; 
    messageId: string;
    payload: string;
    partnerId: string | null;
    receivedAt: string;  
  }
  