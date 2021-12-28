package WirChat.WirChatClient;


import WirChat.WirChatSever.VideoServer;
import sun.awt.windows.WPrinterJob;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
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

    private void insertIcon(int i) {
        StyledDocument doc = login.getJta().getStyledDocument();
        login.getJta().setCaretPosition(doc.getLength()); // 设置插入位置
        ImageIcon ii = new ImageIcon(login.emojis.get(i).getScaledInstance(120,80,0));
        login.getJta().insertIcon(ii); // 插入图片

    }
    @Override
    public void run() {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(attributeSet, false);
        StyleConstants.setFontSize(attributeSet, 20);
        StyleConstants.setFontFamily(attributeSet, "宋体");
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
                String[] strs = text3.split("##");
                StyledDocument sd = login.getJta().getStyledDocument();
                login.getJta().setText("");
                for (String s:strs){
                    System.out.println(s);
                    if (s.equals("0") || s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4") || s.equals("5") || s.equals("6") || s.equals("7") || s.equals("8")){
                        insertIcon(Integer.parseInt(s));

                    }
                    else{
                        try {
                            sd.insertString(sd.getLength(),s, attributeSet);
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //login.getJta().setText(text3);
                break;
            }
            //私聊
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
