package WirChat.WirChatSever;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class WCServer {
    private ServerSocket serverSocket;
    private LinkedList<Socket> socketlist;
    private LinkedList<String> idlist;
    private LinkedList<String> namelist;
    private HashMap<Socket,Thread> serverThreadslist;
    private HashMap<String,Socket> userlist; //id - socket  在线
    private HashMap<String,String> idAndPsswrd;
    private HashMap<String,String> NmAndId;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private Connection connection = null;
    private PreparedStatement prestatement = null;
    private ResultSet resultSet = null;
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
        serverThreadslist = new HashMap<>();
        socketlist = new LinkedList<>();
        userlist = new HashMap<>();
    }

    private void startService() throws IOException {
        while(true) {
            Socket s = serverSocket.accept(); // 获得一个客户端连接 就启动一个新服务线程
            socketlist.add(s);
            Thread t = new Thread(new SeverThread(s));
            serverThreadslist.put(s,t);
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
        //给在线的人发 根据id找socket
        void sendMessageToPrivate(String message,String target) throws IOException {
            if (userlist.get(target)==null){
                System.out.println("找不到该在线用户");
            }else {
                PrintWriter pw = new PrintWriter(userlist.get(target).getOutputStream());
                pw.println(message);
                System.out.println("发送私聊信息给:"+target+" "+message);
                pw.flush();
            }
        }
        void sendObject(Object o,Socket s) throws IOException {

                ois = new ObjectInputStream(s.getInputStream());
                oos = new ObjectOutputStream(s.getOutputStream());
                System.out.println("列表发送成功");
                oos.writeObject(o);
                oos.flush();

        }


        void shutdown() throws IOException {
            socket.close();
            socketlist.remove(socket);
            serverThreadslist.remove(socket);
            System.out.println("目前socket个数:"+socketlist.size());
            System.out.println("目前serverSocket个数："+serverThreadslist.size());

        }

        HashMap<String,String> queryIdAndPsswrd(){
            HashMap<String,String>  map =null;
            try {
                connection = JdbcUtils.getConnection();
                String sql = "SELECT id,PASSWORD FROM ACCOUNT";
                prestatement = connection.prepareStatement(sql);
                resultSet = prestatement.executeQuery();
                map = new HashMap<>();
                while (resultSet.next()){
                    map.put(resultSet.getString(1),resultSet.getString(2));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                try {
                    JdbcUtils.release(connection,prestatement,resultSet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return map;
        }
        HashMap<String,String> queryNmandId(){
            HashMap<String,String>  map =null;
            try {
                connection = JdbcUtils.getConnection();
                String sql = "SELECT id,NAME FROM ACCOUNT";
                prestatement = connection.prepareStatement(sql);
                resultSet = prestatement.executeQuery();
                map = new HashMap<>();
                while (resultSet.next()){
                    map.put(resultSet.getString(2),resultSet.getString(1));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                try {
                    JdbcUtils.release(connection,prestatement,resultSet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return map;
        }
        LinkedList<String> queryIds(){
            LinkedList<String> list =null;
            try {
                connection = JdbcUtils.getConnection();
                String sql = "SELECT id FROM ACCOUNT";
                prestatement = connection.prepareStatement(sql);
                resultSet = prestatement.executeQuery();
                list = new LinkedList<>();
                while (resultSet.next()){
                    list.add(resultSet.getString(1));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                try {
                    JdbcUtils.release(connection,prestatement,resultSet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
        String idToName(String id){
            String s = null;
            try {
                connection = JdbcUtils.getConnection();
                String sql = "SELECT NAME FROM ACCOUNT WHERE id="+"'"+id+"'";
                prestatement = connection.prepareStatement(sql);
                resultSet = prestatement.executeQuery();

                while (resultSet.next()){
                   s=(resultSet.getString(1));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                try {
                    JdbcUtils.release(connection,prestatement,resultSet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return s;
        }
        LinkedList<String> queryNames(){
            LinkedList<String> list =null;
            try {
                connection = JdbcUtils.getConnection();
                String sql = "SELECT NAME FROM ACCOUNT";
                prestatement = connection.prepareStatement(sql);
                resultSet = prestatement.executeQuery();
                list = new LinkedList<>();
                while (resultSet.next()){
                    list.add(resultSet.getString(1));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                try {
                    JdbcUtils.release(connection,prestatement,resultSet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return list;
        }


        void addNewId(String id,String name,String password)  {
            Connection connection = null;
            PreparedStatement prestatement = null;
            ResultSet resultSet = null;

            try {
                connection = JdbcUtils.getConnection();
                String sql = "INSERT	INTO	ACCOUNT(id,`NAME`,`PASSWORD`) VALUES(?,?,?)";
                //String sql2 = "DELETE FROM users where id = 4";
                //预编译 不执行
                prestatement = connection.prepareStatement(sql);
                prestatement.setString(1,id);//id 给第一个id复制
                prestatement.setString(2,name);
                prestatement.setString(3,password);
                int i = prestatement.executeUpdate();
                if (i>0){
                    System.out.println("更改成功");
                }else {
                    System.out.println("插入失败");
                }
            }
            catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            try {
                JdbcUtils.release(connection,prestatement,resultSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                            idlist = queryIds();
                            if (idlist.contains(id)) {
                                userlist.put(id, socket);
                                sendMessageToPrivate("该账号已被注册", id);
                                userlist.remove(id);
                                shutdown();
                            } else {
                                //userlist.put(id, socket);
                                addNewId(id,name,password);
                                System.out.println("有一个用户注册成功！");
                                System.out.println("已注册用户数量:" + idlist.size());
                                if (idlist.contains(id)) {
                                    //userlist.put(id, socket);
                                    sendMessageToPrivate("注册成功！", id);
                                    //userlist.remove(id);
                                    //shutdown();
                                }
                            }
                        } else if (str.startsWith("LOGIN")) {
                            String[] s = str.split("/");
                            String id = s[1];
                            String password = s[2];
                            idlist = queryIds();
                            idAndPsswrd = queryIdAndPsswrd();
                            namelist = queryNames();
                            if (!idlist.contains(id) || !idAndPsswrd.get(id).equals(password)) {
                                System.out.println("登录失败");
                                userlist.put(id,socket);
                                sendMessageToPrivate("登录失败", id);
                                userlist.remove(id);
                            } else {
                                //Thread.sleep(100);
                                userlist.put(id,socket);
                                sendMessageToPrivate("登录成功！", id);
                                sendObject(namelist,socket);
                                sendMessageToPrivate(idToName(id),id);

                            }
                        }
                        //2.退出
                        else if (str.equals("LEAVE")) {
                            System.out.println("服务器侦测到离开信号");
                            socketlist.remove(socket); // 集合删除此客户端连接
                            // 服务器向所有客户端接口发出退出通知
                            sendMessageToAllClient("一个用户已退出聊天室");
                            //socket.close();
                            serverThreadslist.remove(socket);
                            System.out.println("断开连接");
                        }
                        //PRIVATE/NAME/CONTENT
                        else if (str.startsWith("PRIVATE/")) {
                            String[] s = str.split("/");
                            //根据target 再找id
                            String target = s[1];
                            String content = s[2];
                            System.out.println("得到的名字是："+target);
                            NmAndId = queryNmandId();
                            for (String name:NmAndId.keySet()) {
                                System.out.print(name);
                            }
                            String id = NmAndId.get(target);
                            System.out.println(id);
                            if (id == null) {
                                System.out.println("找不到该用户");
                            } else {
                                content = "PRIVATE/"+idToName(id)+"/"+content;
                                //System.out.println(s[1]);
                                //System.out.println(s[2]);
                                sendMessageToPrivate(content, id);
                            }
                        } else if (str.startsWith("PUBLIC/")) {
                            String[] s = str.split("/");
                            String id = s[1];
                            String content = s[2];
                            String name = idToName(id);
                            content = name+"说："+content;
                            content = "PUBLIC/"+content;
                            sendMessageToAllClient(content);
                            System.out.println(id+" 群发消息：" + content);
                        }
                    }if (str==null){
                      System.out.println("目前socket个数:"+socketlist.size());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
