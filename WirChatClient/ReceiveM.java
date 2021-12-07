package WirChat.WirChatClient;


import sun.awt.windows.WPrinterJob;

public class ReceiveM extends Thread{
    WCClient client;
    Login login;
    String text1;
    ReceiveM(WCClient client,Login login){
        this.client = client;
        this.login = login;
    }
    @Override
    public void run() {
        while (true) {
            text1 = this.client.receiveMessage();
            System.out.println("收到消息：" + text1);
            if (text1.startsWith("PUBLIC")) {
                //群聊显示在聊天室里
                String[] s = text1.split("/");
                text1 = s[1];
                String text2 = login.getJta().getText();
                String text3 = text2 + '\n' + text1 + '\n';
                login.getJta().setText(text3);

            }
            else if(text1.startsWith("PRIVATE")){
                String[] s = text1.split("/");
                text1 = s[2];
                text1 = login.getAction().getTalkto()+"对你说："+text1;
                String text2 = login.getPrivateContent().getText();
                String text3 = text2 + '\n' + text1 + '\n';
                login.getPrivateContent().setText(text3);
            }
        }
    }
}
