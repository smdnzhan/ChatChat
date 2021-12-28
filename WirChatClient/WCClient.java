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
    private InputStream is;
    private OutputStream os;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    DataInputStream dis ;
    DataOutputStream dos;


    //构造方法
    public WCClient(String string, int port) throws Exception{
        socket = new Socket(string,port);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(dis));
        //pwriter = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
        //sendM = new SendM(this);
        //sendM .start();
        //receiveM = new ReceiveM(this);
        //receiveM.start();

    }

    public static void main(String[] args) throws Exception {
        WCClient client = new WCClient("127.0.0.1",8888);
    }
    public void writeInt(OutputStream os,int i)throws Exception {
        os.write(i);
        os.write(i>>8);
        os.write(i>>16);
        os.write(i>>24);
        os.flush();
    }

    public final void sendMessage(String str) throws IOException {
        dos.writeUTF(str);
        dos.flush();
        System.out.println("客户端向服务器发送"+str);
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
            String str = dis.readUTF();
            System.out.println("收到服务器信息："+str);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    final void sendByte(int i) throws IOException {
        dos.writeByte(i);
        dos.flush();
        System.out.println("发送了消息头："+i);
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
}
