package WirChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class WCServer {
    private ServerSocket serverSocket;
    private LinkedList<Socket> socketlist;

    public static void main(String[] args) throws IOException {
        WCServer server = new WCServer();
        server.startService();
    }

    //构造方法
    WCServer() {
        try {
            serverSocket = new ServerSocket(8888);
            System.out.println("服务器运行，等待客户端连接");
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketlist = new LinkedList<>();
    }

    private void startService() throws IOException {
        int i=0;
        while(true) {
            i++;
            Socket s = serverSocket.accept(); // 获得一个客户端连接 就启动一个新服务线程
            System.out.print( i+" 用户进入聊天室\n" );
            socketlist.add(s);
            Thread t = new Thread(new SeverThread(s));
            t.start();
        }
    }
    class SeverThread implements Runnable{
        Socket socket;
        SeverThread(Socket socket){
            this.socket = socket;
        }

        //输出文本方法
        void sendMessageToAllClient(String message) throws IOException {
            for (Socket s : socketlist) { // 循环集合中所有的客户端连接
                PrintWriter pw = new PrintWriter(s.getOutputStream() ); // 创建输出流
                pw.println(message ); // 输入写入文本
                pw.flush();
            }
        }

        @Override
        public void run() {
            BufferedReader buf = null;
            try {
                buf = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                while (true) {
                    String str = buf.readLine();
                    if(str==null||str.equals("LEAVE")) {
                        System.out.println("服务器侦测到离开信号");
                        socketlist.remove(socket); // 集合删除此客户端连接
                        // 服务器向所有客户端接口发出退出通知
                        sendMessageToAllClient("一个用户已退出聊天室");
                        socket.close();
                        System.out.println("断开连接");
                        return;
                    }
                    System.out.println("服务器向客户端输出为："+str);
                    sendMessageToAllClient(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
