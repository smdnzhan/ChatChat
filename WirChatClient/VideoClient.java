package WirChat.WirChatClient;

import WirChat.WirChatSever.VideoServer;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.util.ImageUtils;
import com.sun.imageio.plugins.common.ImageUtil;

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

        }
    }

    private void trans(BufferedImage image)  {
        byte []b = new byte[]{};
        try {
            b = toByteArray(image,"jpg");
            int len = b.length;
            //先发数组长度
            VideoServer.writeInt(dos,len);
            //再发数组
            dos.write(b);
            System.out.println(b.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // bufferedimage -> byte[]
    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        return baos.toByteArray();

    }


}
