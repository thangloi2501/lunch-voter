import { Injectable } from '@angular/core';
declare var SockJS: any;
declare var Stomp: any;
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root',
})
export class WebsocketService {
    private stompClient: any;

    connect(destination: string, callback: (message: any) => void) {
        const socket = new SockJS(environment.wsURL);
        this.stompClient = Stomp.over(socket);
        const that = this;
        this.stompClient.connect({}, () => {
            console.log('Connected to WebSocket');
            that.stompClient.subscribe(destination, (message: any) => {
                callback(JSON.parse(message.body));
            });

        });
    }

    disconnect() {
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.disconnect();
            console.log('Disconnected from WebSocket');
        }
    }

    subscribe(destination: string, callback: (message: any) => void) {
        if (this.stompClient) {
            this.stompClient.subscribe(destination, (message: any) => {
                callback(JSON.parse(message.body));
            });
        }
    }

    send(destination: string, body: any) {
        if (this.stompClient) {
            this.stompClient.send(destination, {}, JSON.stringify(body));
        }
    }
}
