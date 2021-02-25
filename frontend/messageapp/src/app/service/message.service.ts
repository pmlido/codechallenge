import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CcMessageInfo } from '../model/cc-message-info';
import { environment } from '../../environments/environment';
import { CcMessage } from '../model/cc-message';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private userId: string;

  constructor(
    private httpClient: HttpClient) {
      this.userId = 'user-' + this.getRandomInt();
  }

  public getUserId() : string {
    return this.userId;
  }

  public getMessages(): Promise<CcMessageInfo[]> {
    return this.httpClient
      .get(`${environment.backendBaseUrl}/message`, {headers: this.getHeaders()})
      .toPromise()
      .then(response => response as CcMessageInfo[]);
  }

  public createMessage(messageText: string): Promise<any> {
    return this.httpClient
      .post(`${environment.backendBaseUrl}/message`, 
            new CcMessage(messageText),
            {headers: this.getHeaders()})
            .toPromise();
  }

  public deleteMessage(msgId: string): Promise<any> {
    return this.httpClient
      .delete(`${environment.backendBaseUrl}/message/${msgId}`,
            {headers: this.getHeaders()})
            .toPromise();
  }

  public updateMessage(msgId: string, messageText: string): Promise<any> {
    return this.httpClient
      .put(`${environment.backendBaseUrl}/message/${msgId}`,
            new CcMessage(messageText),
            {headers: this.getHeaders()})
            .toPromise();
  }

  private getHeaders() : HttpHeaders {
    return new HttpHeaders({
      'X-UserId' : `${this.userId}`
    });
  }
  
  private getRandomInt() {
    return Math.floor(Math.random() * Math.floor(100));
  }  

}
