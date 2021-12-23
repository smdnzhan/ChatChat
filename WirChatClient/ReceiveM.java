package WirChat.WirChatClient;


import WirChat.WirChatSever.VideoServer;
import sun.awt.windows.WPrinterJob;

import java.awt.*;
import java.io.IOException;

public class ReceiveM extends Thread{
    WCClient client;
    Login login;
    String text1;
    byte b;
    Graphics g;
    ReceiveM(WCClient client,Login login){
        this.client = client;
        this.login = login;
        this.g = login.getJta().getGraphics();
    }
    @Override
    public void run() {

        while (true) {
            try {
                b = client.dis.readByte();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("收到字节：" + b);
            switch(b){
            case 4: {
                //群聊显示在聊天室里
                text1 = client.receiveMessage();
                String text2 = login.getJta().getText();
                String text3 = text2 + '\n' + text1 + '\n';
                login.getJta().setText(text3);
                break;
            }
                case 3 :{
                    String sender = client.receiveMessage();
                    String content = client.receiveMessage();
                text1 = sender+"对你说："+content;
                String text2 = login.getPrivateContent().getText();
                String text3 = text2 + '\n' + text1 + '\n';
                login.getPrivateContent().setText(text3);
                break;
            }
                case 5:{
                    try {
                        int x1 = client.dis.readInt();
                        int y1 = client.dis.readInt();
                        g.drawLine(x1,y1,x1,y1);
                        System.out.println("收到坐标"+x1+" "+y1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case 7:{
                    VideoServer vs = new VideoServer();
                    vs.run();
                }
                case 8:{
                    VideoClient vc = new VideoClient();
                    vc.run();
                }
        }
    }
    }
}
