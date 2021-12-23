package WirChat.WirChatClient;

import WirChat.WirChatSever.VideoServer;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class VideoClient extends Thread{
    Socket server;
    BufferedOutputStream dos;
    BufferedInputStream dis ;

    public static void main(String[] args) {
        VideoClient vc = new VideoClient();
        vc.run();
    }


    VideoClient(){
        try {
            server = new Socket("127.0.0.1", 9999);
            System.out.println("连接服务器成功");
            InputStream input = server.getInputStream();
            dis = new BufferedInputStream(input);
            OutputStream out = server.getOutputStream();
            dos = new BufferedOutputStream(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    @Override
    public void run() {
        final Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);
        final JFrame window = new JFrame("摄像头");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e)
            {
                webcam.close();
                window.dispose();
            }
        });
        while(true){
            window.add(panel, BorderLayout.CENTER);
            window.setResizable(true);
            window.pack();
            window.setVisible(true);
            BufferedImage image = webcam.getImage();
            //将照通过网络发送
            trans(image);
            try {
                ImageIO.write(image,"jpg",dos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void trans(BufferedImage image) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        try {
            VideoServer.writeInt(dos,w);
            VideoServer.writeInt(dos,h);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        for (int i = 0; i < h; i ++) {
            for(int j = 0 ; j < w ; j ++ ) {
                int a= image.getRGB(j,i);
                try {
                    VideoServer.writeInt(dos,a);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
