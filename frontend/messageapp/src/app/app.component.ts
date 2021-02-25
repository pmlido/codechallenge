import { Component, OnInit } from '@angular/core';
import { MessageService } from './service/message.service'
import { CcMessageInfo } from './model/cc-message-info';
import { FormBuilder } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {

  activeUser: string;

  messageForm = this.formBuilder.group({
    message: ''
  });

  public messages : CcMessageInfo[]

  constructor(private messageService : MessageService,
            private formBuilder: FormBuilder) {
      this.activeUser = messageService.getUserId();
  }

  ngOnInit() {
    this.getAllMessages();
  }

  onSubmit() {
    if(this.messageForm.value.message) {
      this.messageService.createMessage(this.messageForm.value.message)
        .then(r => this.getAllMessages());
      
      this.messageForm.reset();
    }
  }
  
  onRefresh() {
    this.getAllMessages();
  }

  onDelete(msgId : string) {
    this.messageService.deleteMessage(msgId)
    .then(r => this.getAllMessages());
  }


  onUpdate(msgId : string) {
    Swal.fire({
      html: '<input id="swal-input1" class="swal2-input">' ,
      title: 'Update Text',
      showConfirmButton: true,
      preConfirm: () => {
        const messagetText = (document.getElementById('swal-input1') as HTMLInputElement).value;
        return messagetText;
      }      
    }).then(
      (result) => {
        this.messageService.updateMessage(msgId, result.value)
        .then(r => this.getAllMessages());
      }
     )
  }

  public getAllMessages() {
    this.messageService.getMessages()
      .then(messages => {
        this.messages = messages;
        this.messages.sort((a, b) => b.timestamp - a.timestamp);
        });
  }

}
