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
    private OutputStream os;
    private InputStream is;
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
            os = s.getOutputStream();
            is = s.getInputStream();
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
                DataOutputStream dos = new DataOutputStream(s.getOutputStream()); // 创建输出流
                dos.writeUTF(message ); // 输入写入文本
                dos.flush();
            }
            System.out.println("群发了消息："+message);
        }
        //给在线的人发 根据id找socket
        void sendMessageToPrivate(String message,String target) throws IOException {
            if (userlist.get(target)==null){
                System.out.println("找不到该在线用户");
            }else {
                DataOutputStream dos = new DataOutputStream(userlist.get(target).getOutputStream());
                dos.writeUTF(message);
                System.out.println("发送私聊信息给:"+target+" "+message);
                dos.flush();
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

        void sendByte(int i,String s) throws IOException {
            Socket ss = userlist.get(s);
            DataOutputStream dos = new DataOutputStream(ss.getOutputStream());
            dos.writeByte(i);
            dos.flush();
            System.out.println("向"+s+"发送了字节");
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
            DataInputStream dis = null;
            DataOutputStream dos = null;
            try {
                is = socket.getInputStream();
                dis = new DataInputStream(is);
                //dos = new DataOutputStream(os);
                while (true) {
                    //监听字节消息头
                    byte b = dis.readByte();
                    System.out.println("b的值为；"+b);
                    switch (b) {
                        //1.注册 格式为 REGISTER/ID/NAME/PASSWORD 1/ID/NAME/PASSWORD
                        case 1:
                            String id = dis.readUTF();
                            String name = dis.readUTF();
                            String password = dis.readUTF();
                            idlist = queryIds();
                            if (idlist.contains(id)) {
                                //feedback : 1/content
                                userlist.put(id, socket);
                                sendByte(1,id);
                                sendMessageToPrivate("该账号已被注册", id);
                                userlist.remove(id);
                                shutdown();
                            } else {
                                //userlist.put(id, socket);
                                addNewId(id, name, password);
                                System.out.println("有一个用户注册成功！");
                                System.out.println("已注册用户数量:" + idlist.size());
                                if (idlist.contains(id)) {
                                    //userlist.put(id, socket);
                                    sendByte(1,id);
                                    sendMessageToPrivate("注册成功！", id);
                                    //userlist.remove(id);
                                    //shutdown();
                                }
                            }
                            break;
                            //登录 2/id/password
                        case 2:
                            String log_id = dis.readUTF();
                            String log_pass = dis.readUTF();
                            idlist = queryIds();
                            idAndPsswrd = queryIdAndPsswrd();
                            namelist = queryNames();
                            if (!idlist.contains(log_id) || !idAndPsswrd.get(log_id).equals(log_pass)) {
                                System.out.println("登录失败");
                                userlist.put(log_id, socket);
                                sendByte(2,log_id);
                                sendMessageToPrivate("登录失败", log_id);
                                userlist.remove(log_id);
                            } else {
                                //Thread.sleep(100);
                                userlist.put(log_id, socket);
                                sendByte(2,log_id);
                                sendMessageToPrivate("登录成功！", log_id);
                                sendObject(namelist, socket);
                                sendMessageToPrivate(idToName(log_id), log_id);

                            }
                            break;
                        //退出
                            /*
                        case 23:{
                            System.out.println("服务器侦测到离开信号");
                            socketlist.remove(socket); // 集合删除此客户端连接
                            // 服务器向所有客户端接口发出退出通知
                            sendMessageToAllClient("一个用户已退出聊天室");
                            //socket.close();
                            serverThreadslist.remove(socket);
                            System.out.println("断开连接");}


                             */
                        //PRIVATE/NAME/CONTENT 1/SENDER/RECEIVER/CONTENT
                        case 3:
                            //根据target 再找id
                            String sender = dis.readUTF();
                            String receiver = dis.readUTF();
                            String content =dis.readUTF();
                            System.out.println("私聊发送者为：" + sender+"接收者为："+receiver);
                            NmAndId = queryNmandId();

                            String rec_id = NmAndId.get(receiver);
                            System.out.println("接收者id:"+rec_id);
                            if (rec_id == null) {
                                System.out.println("找不到该用户");
                                sendByte(3,rec_id);
                                String sen_id = NmAndId.get(sender);
                                sendMessageToPrivate("找不到该用户！",sen_id);
                            } else {
                                sendByte(3,rec_id);
                                sendMessageToPrivate(sender,rec_id);
                                sendMessageToPrivate(content, rec_id);
                                System.out.println("一条私聊消息已经被转发");
                            }
                            break;
                            //群发   4/sender/content 消息头+名字+内容
                        case 4:
                            String pblc_sender = dis.readUTF();
                            String pblc_content = dis.readUTF();

                            content = pblc_sender + "说：" + pblc_content;
                            for (Socket s :socketlist) {
                                DataOutputStream dops = new DataOutputStream(s.getOutputStream());
                                dops.writeByte(4);
                                dops.writeUTF(content);
                                dops.flush();
                            }
                            System.out.println(pblc_sender + " 群发消息：" + content);
                            break;

                        case 5:
                            //接收字节
                            try {
                                for (Socket s:socketlist) {
                                    if (s!=socket){
                                    dos = new DataOutputStream(s.getOutputStream());
                                    int x1 = dis.readInt();
                                    int y1 = dis.readInt();
                                    System.out.println("正在监听");
                                    System.out.println("x1:" + x1 + "y1: " + y1);
                                    dos.writeByte(5);
                                    dos.writeInt(x1);
                                    dos.writeInt(y1);
                                    dos.flush();
                                    //记得flush
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
