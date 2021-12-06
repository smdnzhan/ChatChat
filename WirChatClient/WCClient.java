package WirChat.WirChatClient;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class WCClient {
    private Socket socket; // 客户端套接字
    private BufferedReader bufferedReader; // 字节流读取套接字输入流
    private PrintWriter pwriter; // 字节流写入套接字输出流
    private String id;
    private String name;
    private String password;
    private SendM sendM;
    private ReceiveM receiveM;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    //构造方法
    public WCClient(String string, int port) throws Exception{
        socket = new Socket(string,port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pwriter = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
        //sendM = new SendM(this);
        //sendM .start();
        //receiveM = new ReceiveM(this);
        //receiveM.start();

    }

    public static void main(String[] args) throws Exception {
        WCClient client = new WCClient("127.0.0.1",8888);
    }


    public final void sendMessage(String str) throws IOException {
        pwriter.println(str);
        pwriter.flush();
    }

    public LinkedList<String> receiveUserlist() throws IOException, ClassNotFoundException {
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("客户端序列化流正常");
        LinkedList<String> list ;
        list = (LinkedList<String>) ois.readObject();
        return list;
    }
    final String receiveMessage() {
        try {
            String str = bufferedReader.readLine();
            if(str!=null){
            System.out.println("收到服务器信息："+str);
            return str;}
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
    public String getId(){return id;}
    public void setId(String id){this.id = id;}
    public void setPassword(String password){this.password = password;}
    public String getPassword(){return password;}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public SendM getSendM(){return sendM;}
    public ReceiveM getReceiveM(){return receiveM;}
}
