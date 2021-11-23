package WirChat;


public class ReceiveM extends Thread{
    WCClient client;

    ReceiveM(WCClient client){
        this.client = client;
    }
    @Override
    public void run() {
        while (true){
            this.client.receiveMessage();}
    }
}
