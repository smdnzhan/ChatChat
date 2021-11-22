package WirChat;


public class ReceiveM extends Thread{
    WCClient client;

    ReceiveM(WCClient client){
        this.client = client;
    }
    @Override
    public void run() {
        while (!client.getSocket().isClosed()){
            System.out.println("socket是否关闭："+client.getSocket().isClosed());
            this.client.receiveMessage();}
        System.out.println("客户端断开连接");
    }
}
