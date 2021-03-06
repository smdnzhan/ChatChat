package WirChat.WirChatSever;

import WirChat.WirChatClient.VideoClient;
import WirChat.WirChatClient.VideoThread;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class VideoServer extends Thread{
    private ServerSocket serverSocket;
    BufferedInputStream dis;
    BufferedOutputStream dos;
    Graphics gg ;

    public static void main(String[] args) {
        VideoServer vs = new VideoServer();
        vs.run();
    }


    public VideoServer(){
        try {
            serverSocket = new ServerSocket(9999);
            System.out.println("服务器运行，等待客户端连接");
            Socket client = serverSocket.accept();
            System.out.println("等待客户连接");
            InputStream input = client.getInputStream();
            dis = new BufferedInputStream(input);
            OutputStream out = client.getOutputStream();
            dos = new BufferedOutputStream(out);

        } catch (IOException e) {
            e.printStackTrace();
        }

        JFrame jf = new JFrame();
        jf.setSize(600, 800);
        jf.setTitle("视频窗口");
        // 设置居中显示
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);
        jf.setVisible(true);
        gg = jf.getGraphics();

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void writeInt(BufferedOutputStream bos,int t) throws IOException {
        bos.write(t&0xFF);
        bos.write((t>>8)& 0xFF);
        bos.write((t>>16)& 0xFF);
        bos.write((t>>24)& 0xFF);
        bos.flush();
    }

    public static int readInt(BufferedInputStream bis) throws IOException {
        int r1 = bis.read();
        int r2 = bis.read()<<8;
        int r3 = bis.read()<<16;
        int r4 = bis.read()<<24;

        int res = r1|r2|r3|r4;
        System.out.println("res:"+res);
        return res;
    }

    // convert byte[] to BufferedImage
    public static BufferedImage toBufferedImage(byte[] bytes)
            throws IOException {

        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bi = ImageIO.read(is);
        return bi;

    }
    @Override
    public void run() {
        while (true){
            BufferedImage image;
            try {
                int i = readInt(dis);
                byte[] b = new byte[i];
                dis.read(b);
                image = toBufferedImage(b);
                gg.drawImage(image,0,0,null);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    }
}
