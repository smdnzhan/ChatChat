package WirChat;

import java.io.*;
import java.net.Socket;

public class WCClient {
    private Socket socket; // 客户端套接字
    private BufferedReader bufferedReader; // 字节流读取套接字输入流
    private PrintWriter pwriter; // 字节流写入套接字输出流
    private String id;
    //构造方法
    WCClient(String string,int port,String name) throws Exception{
        socket = new Socket(string,port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pwriter = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
        this.id = name;
        SendM s = new SendM(this);
        s.start();
        ReceiveM r = new ReceiveM(this);
        r.start();

    }

    public static void main(String[] args) throws Exception {
        WCClient client = new WCClient("127.0.0.1",8888,"小明");

    }

    public final void sendMessage(String str) throws IOException {
        pwriter.println(str+"    ----来自用户:"+id);
        pwriter.flush();
    }

    final String receiveMessage() {
        try {
            String str = bufferedReader.readLine();
            System.out.println("收到服务器信息："+str);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final void close() {
        try {
            pwriter.close();
            bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }
}
