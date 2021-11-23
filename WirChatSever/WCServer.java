package WirChat.WirChatSever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class WCServer {
    private ServerSocket serverSocket;
    private LinkedList<Socket> socketlist;
    private HashMap<String,Socket> userlist;

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
        userlist = new HashMap<>();
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

        void sendMessageToPrivate(String message,String target) throws IOException {
            if (userlist.get(target)==null){
                System.out.println("该用户不存在");
            }else {
                PrintWriter pw = new PrintWriter(userlist.get(target).getOutputStream());
                pw.println(message);
                System.out.println("发送私聊信息给:"+target+" "+message);
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
                    //1.注册 格式为
                    if (str.startsWith("REGISTER:")){
                        String id = str.split(":")[1];
                        userlist.put(id,socket);
                        System.out.println("有一个用户注册成功！");
                        System.out.println("已注册用户数量:"+userlist.size());

                        Thread.sleep(100);
                        if(userlist.containsKey(id)){
                        sendMessageToPrivate("注册成功！",id);}
                    }
                    //2.退出
                    else if(str.equals("LEAVE")) {
                        System.out.println("服务器侦测到离开信号");
                        socketlist.remove(socket); // 集合删除此客户端连接
                        // 服务器向所有客户端接口发出退出通知
                        sendMessageToAllClient("一个用户已退出聊天室");
                        socket.close();
                        System.out.println("断开连接");
                        return;
                    }
                    //PRIVATE:小明:早上好
                    else if (str.startsWith("PRIVATE/")){
                        String[] s = str.split("/");
                        String target = s[1];
                        String content = s[2];
                        //System.out.println(s[1]);
                        //System.out.println(s[2]);
                        sendMessageToPrivate(content,target);
                    }else {
                    sendMessageToAllClient(str);
                        System.out.println("服务器向客户端群发了："+str);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
