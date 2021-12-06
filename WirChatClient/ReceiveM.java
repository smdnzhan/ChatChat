package WirChat.WirChatClient;


public class ReceiveM extends Thread{
    WCClient client;
    Login login;
    ReceiveM(WCClient client,Login login){
        this.client = client;
        this.login = login;
    }
    @Override
    public void run() {
        while (true){
            String text1 = this.client.receiveMessage();
            System.out.println("收到消息："+text1);
            String text2 = login.getJta().getText();
            String text3 = text2+'\n'+text1+'\n';
            login.getJta().setText(text3);}
    }
}
