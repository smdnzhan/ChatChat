package WirChat.WirChatClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

public class ClientListener implements ActionListener {
    private Login login;
    private LinkedList<String> demolist = new LinkedList<>();
    //登入界面
    public ClientListener(Login login) {
        this.login = login;
        demolist.add("大熊");
        demolist.add("胖虎");
        demolist.add("静香");
        demolist.add("小夫");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();
        if("注册".equals(name)) {
            login.register();
        }
        else if("登录".equals(name)) {
            WCClient client = null;
            try {
                client = new WCClient("127.0.0.1",8888);
                String id = login.getLogin_id().getText();
                String password = login.getLogin_password().getText();
                //包装成识别格式
                String message = "LOGIN/"+id+"/"+password;
                Thread.sleep(100);
                client.getSendM().start();
                client.getReceiveM().start();
                try {
                    client.sendMessage(message);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                String str = client.receiveMessage();
                //System.out.println(client.receiveMessage());
                if (str.equals("登录失败")){ //??
                    login.loginFail();
                    client.getSendM().stop();
                    client.getReceiveM().stop();
                }if (str.equals("登录成功！")){
                    System.out.println("在执行登录成功语句");
                    login.loginOK();
                    login.menu();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
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
                String message = "REGISTER/"+id+"/"+username+"/"+password;
                client.setId(id);
                client.setName(username);
                client.setPassword(password);
                client.getSendM().start();
                client.getReceiveM().start();
                try {
                    client.sendMessage(message);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                //注册流程结束 就关闭Socket
                if (client.receiveMessage().startsWith("该账号已被注册")){ //??
                    login.registerFail();
                    client.getSendM().stop();
                    client.getReceiveM().stop();
                    client.close();
                }else {login.registerOk();
                    client.getSendM().stop();
                    client.getReceiveM().stop();
                        client.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }




        }
        else if("公共聊天室".equals(name)){
            login.publicChat(demolist);
        }
    }
}