package WirChat.WirChatClient;


import java.io.IOException;
import java.util.Scanner;

public class SendM extends Thread{
    WCClient client;

    SendM(WCClient client){
        this.client = client;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while (true){
            //发送内容
            System.out.println("输入聊天内容: ");
            while (sc.hasNext()){
                String string = sc.nextLine();
                try {
                    client.sendMessage(string);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (string.equals("LEAVE")){
                    System.out.println("即将断开");
                    sc.close();
                    break;
                }
            }
        }
    }
}
