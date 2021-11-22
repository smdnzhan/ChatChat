package WirChat;


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
        while (!client.getSocket().isClosed()){
            System.out.println("输入聊天内容: ");
            //发送内容
            try {
                client.sendMessage(sc.next());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (sc.hasNextLine()){
                if (sc.next().equals("LEAVE")){
                    System.out.println("即将断开");
                    sc.close();
                    client.close();
                    break;
                }
            }
        }
        return;
    }
}
