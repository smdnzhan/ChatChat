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
    private LinkedList<String> idlist;
    private HashMap<Socket,Thread> severThreadslist;
    private HashMap<String,Socket> userlist; //id - socket  在线
    private HashMap<String,String> namelist; //name -id
    private HashMap<String,String> passwordlist; //id-password
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
        severThreadslist = new HashMap<>();
        socketlist = new LinkedList<>();
        userlist = new HashMap<>();
        namelist = new HashMap<>();
        passwordlist = new HashMap<>();
        idlist = new LinkedList<>();
    }

    private void startService() throws IOException {
        while(true) {
            Socket s = serverSocket.accept(); // 获得一个客户端连接 就启动一个新服务线程
            socketlist.add(s);
            Thread t = new Thread(new SeverThread(s));
            severThreadslist.put(s,t);
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
        void shutdown() throws IOException {
            socket.close();
            socketlist.remove(socket);
            Thread temp = severThreadslist.get(socket);
            temp.stop();
            severThreadslist.remove(socket);
            System.out.println("目前socket个数:"+socketlist.size());
            return;
        }
        @Override
        public void run() {
            BufferedReader buf = null;
            try {
                buf = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                while (true) {
                    String str = buf.readLine();
                    if (str != null) {
                        //1.注册 格式为 REGISTER/ID/NAME/PASSWORD
                        if (str.startsWith("REGISTER/")) {
                            String[] s = str.split("/");
                            String id = s[1];
                            String name = s[2];
                            String password = s[3];
                            if (idlist.contains(id)) {
                                userlist.put(id, socket);
                                sendMessageToPrivate("该账号已被注册", id);
                                userlist.remove(id);
                                shutdown();
                            } else {
                                //userlist.put(id, socket);
                                passwordlist.put(id, password);
                                namelist.put(name, id);
                                idlist.add(id);
                                System.out.println("有一个用户注册成功！");
                                System.out.println("已注册用户数量:" + idlist.size());
                                Thread.sleep(100);
                                if (idlist.contains(id)) {
                                    userlist.put(id, socket);
                                    sendMessageToPrivate("注册成功！", id);
                                    userlist.remove(id);
                                    shutdown();
                                }
                            }
                        } else if (str.startsWith("LOGIN")) {
                            String[] s = str.split("/");
                            String id = s[1];
                            String password = s[2];
                            if (!idlist.contains(id) || !passwordlist.get(id).equals(password)) {
                                System.out.println("登录失败");
                                userlist.put(id,socket);
                                sendMessageToPrivate("登录失败", id);
                                userlist.remove(id);
                            } else {
                                //Thread.sleep(100);
                                userlist.put(id,socket);
                                sendMessageToPrivate("登录成功！", id);
                            }
                        }
                        //2.退出
                        else if (str.equals("LEAVE")) {
                            System.out.println("服务器侦测到离开信号");
                            socketlist.remove(socket); // 集合删除此客户端连接
                            // 服务器向所有客户端接口发出退出通知
                            sendMessageToAllClient("一个用户已退出聊天室");
                            //socket.close();
                            severThreadslist.remove(socket);
                            System.out.println("断开连接");
                            return;
                        }
                        //PRIVATE/NAME/CONTENT
                        else if (str.startsWith("PRIVATE/")) {
                            String[] s = str.split("/");
                            //根据target 再找id
                            String target = s[1];
                            String content = s[2];
                            String id = namelist.get(target);
                            if (id == null) {
                                System.out.println("找不到该用户");
                            } else {
                                //System.out.println(s[1]);
                                //System.out.println(s[2]);
                                sendMessageToPrivate(content, target);
                            }
                        } else {
                            sendMessageToAllClient(str);
                            System.out.println("服务器向客户端群发了：" + str);
                        }
                    }if (str==null){
                        socket.close();
                        socketlist.remove(socket);
                        Thread temp = severThreadslist.get(socket);
                        temp.stop();
                        severThreadslist.remove(socket);
                        System.out.println("目前socket个数:"+socketlist.size());
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
