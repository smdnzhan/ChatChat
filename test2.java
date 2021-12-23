package WirChat;

import WirChat.WirChatClient.WCClient;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class test2 {
    public static void main(String[] args) throws IOException {
        for (int i = 0;i<9;i++){
            File file = new File("D:\\java\\JavaJAVA\\src\\WirChat\\Emoji\\"+i+".jpg");
            Image img = ImageIO.read(file);
            Image scaled = img.getScaledInstance(50,50,Image.SCALE_DEFAULT);
        }
    }
}
