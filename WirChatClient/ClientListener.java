package WirChat.WirChatClient;

import WirChat.WirChatSever.ByteThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ClientListener implements ActionListener {
    private Login login;
    private WCClient client;
    private LinkedList<String> alluserlist;
    private ReceiveM t;
    ArrayList<Image> imglist;



    private String talkto;
    //登入界面
    public ClientListener(Login login) {
        this.login = login;

    }
    public String getTalkto() {
        return talkto;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();
        if("注册".equals(name)) {
            login.register();
        }
        else if("登录".equals(name)) {
            try {
                client = new WCClient("127.0.0.1",8888);
                String id = login.getLogin_id().getText();
                String password = login.getLogin_password().getText();
                //包装成识别格式
                try {
                    client.sendByte(2);
                    client.sendMessage(id);
                    client.sendMessage(password);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                byte b = client.dis.readByte();
                if (b==2){
                    String feedback = client.receiveMessage();
                    System.out.println("feedback:" + feedback);
                    if (feedback.equals("登录失败")) { //??
                        login.loginFail();
                    }
                    if (feedback.startsWith("登录成功！")) {
                        System.out.println("在执行登录成功语句");
                        client.setId(id);
                        login.loginOK();
                        login.menu();
                        alluserlist = client.receiveUserlist();
                        String username = client.receiveMessage();
                        client.setName(username);
                    }
                }
            } catch (Exception ioException){
                    ioException.printStackTrace();
                }
        }
        else if("完成".equals(name)) {
            //连接服务器，发送注册信息
            WCClient client = null;
            try {
                client = new WCClient("127.0.0.1",8888);
                String id = login.getRegisterid().getText();
                String username = login.getRegistername().getText();
                String password = login.getRegisterpassword().getText();
                //包装成识别格式

                client.setId(id);
                client.setName(username);
                client.setPassword(password);
                try {
                    client.sendByte(1);
                    client.sendMessage(id);
                    client.sendMessage(username);
                    client.sendMessage(password);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                byte b = client.dis.readByte();
                if (b==1){
                //获得服务器反馈
                String feedback = client.receiveMessage();
                if (feedback.startsWith("该账号已被注册")){ //??
                    login.registerFail();
                    client.close();
                }else {login.registerOk();
                        client.close();
                }
            } }catch (Exception exception) {
                exception.printStackTrace();
            }

        }
        else if("公共聊天室".equals(name)){

            login.publicChat(client.getName(), alluserlist);
            t = new ReceiveM(client,login);
            t.start();

        }
        //群聊
        else if("发送消息".equals(name)){
            String text = login.getJta2().getText();
            try {
            client.sendByte(4);
            client.sendMessage(client.getName());
            client.sendMessage(text);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            login.getJta2().setText("");
        }
        else if("发起私聊".equals(name)){
            talkto = login.getList().getSelectedValue();
            login.privateChat(talkto);

        }
        else if("发送私聊".equals(name)){
            String updated;
            String text = login.getPrivateMessage().getText();
            try {
                client.sendByte(3);
                client.sendMessage(client.getName());
                client.sendMessage(talkto);
                client.sendMessage(text);
                System.out.println("私聊信息发送成功");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //清除输入的内容
            login.getPrivateMessage().setText("");
            String origin = login.getPrivateContent().getText();
            updated = origin+"你说:"+text;
            login.getPrivateContent().setText(updated);
        }

        else if("视频私聊".equals(name)){
            try {
                client.sendByte(6);
                client.sendMessage(client.getName());
                client.sendMessage(talkto);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        else if("你画我猜".equals(name)) {
            boolean flag = login.getMml().isFlag();
            if (flag) {
                login.getMml().setFlag(false);
            } else {
                try {
                    login.getMml().setClient(client);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                login.getMml().setFlag(!login.getMml().isFlag());
            }
        }
        else if("表情包".equals(name)){
            boolean flag = login.em.flag;
            login.em.setFlag(!flag);
            login.em.setVisible(!flag);
            System.out.println("flag:"+flag);
        }
    }
}
