package WirChat.WirChatClient;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class Login {
    private ClientListener action;
    private JTextField registername;
    private JTextField registerid;
    private JTextField registerpassword;
    private JTextField login_id;
    private JTextField login_password;
    // 显示登录界面的方法
    public void showUI() {
        // 创建窗体对象
        javax.swing.JFrame jf = new javax.swing.JFrame();
        // 像素>分辨率
        jf.setSize(450, 550);
        jf.setTitle("登录界面");
        // 设置居中显示
        jf.setLocationRelativeTo(null);
        // 设置退出进程
        jf.setDefaultCloseOperation(3);

        // 流式布局管理器
        java.awt.FlowLayout flow = new java.awt.FlowLayout();
        // 设置窗体布局
        jf.setLayout(flow);

        // 加载图片
        javax.swing.ImageIcon image = new javax.swing.ImageIcon("C:\\Users\\ZDX\\Pictures\\Voto\\杰尼龟.jpg");
        // 标签
        javax.swing.JLabel jla = new javax.swing.JLabel(image);
        jf.add(jla);

        // 账号名
        javax.swing.JLabel user = new javax.swing.JLabel("账号:");
        jf.add(user);

        // 账号框(文本框)
        login_id = new javax.swing.JTextField();
        java.awt.Dimension dm = new java.awt.Dimension(380, 30);
        // 除了JFrame，其它组件设置大小都是该方法
        login_id.setPreferredSize(dm);
        jf.add(login_id);

        // 密码
        javax.swing.JLabel pass = new javax.swing.JLabel("密码:");
        jf.add(pass);

        login_password = new javax.swing.JPasswordField();
        // 除了JFrame，其它组件设置大小都是该方法
        login_password.setPreferredSize(dm);
        jf.add(login_password);

        // 按钮
        javax.swing.JButton jbu = new javax.swing.JButton("登录");
        jf.add(jbu);
        javax.swing.JButton jbu2 = new javax.swing.JButton("注册");
        jf.add(jbu2);

        action = new ClientListener(this);
        //给功能按钮添加动作监听器
        jbu.addActionListener(action);
        jbu2.addActionListener(action);

        // 设置可见
        jf.setVisible(true);
    }
    //注册界面
    public void register() {

        // 创建窗体对象
        javax.swing.JFrame jf = new javax.swing.JFrame();
        // 像素>分辨率
        jf.setSize(450, 550);
        jf.setTitle("注册界面");
        // 设置居中显示
        jf.setLocationRelativeTo(null);
        // 设置退出进程
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 流式布局管理器
        java.awt.FlowLayout flow = new java.awt.FlowLayout();
        // 设置窗体布局
        jf.setLayout(flow);

        java.awt.Dimension dm = new java.awt.Dimension(380, 30);
        javax.swing.JLabel userName = new javax.swing.JLabel("名字:");
        jf.add(userName);

        registername = new javax.swing.JTextField();
        registername.setPreferredSize(dm);
        jf.add(registername);
        // 账号名
        javax.swing.JLabel user = new javax.swing.JLabel("账号:");
        jf.add(user);

        // 账号框(文本框)
        registerid = new javax.swing.JTextField();
        // 除了JFrame，其它组件设置大小都是该方法
        registerid.setPreferredSize(dm);
        jf.add(registerid);

        // 账号名
        javax.swing.JLabel pass = new javax.swing.JLabel("密码:");
        jf.add(pass);

        registerpassword = new javax.swing.JTextField();
        // 除了JFrame，其它组件设置大小都是该方法
        registerpassword.setPreferredSize(dm);
        jf.add(registerpassword);

        javax.swing.JButton jbu = new javax.swing.JButton("完成");
        jf.add(jbu);
        //给功能按钮添加动作监听器
        jbu.addActionListener(action);

        // 设置可见
        jf.setVisible(true);
    }
    public void registerOk(){
        JOptionPane.showMessageDialog(null, "注册成功");
    }
    public void registerFail(){
        JOptionPane.showMessageDialog(null, "注册成功");
    }

    public void menu(){
        // 创建窗体对象
        javax.swing.JFrame jf = new javax.swing.JFrame();
        // 像素>分辨率
        jf.setSize(600, 800);
        jf.setTitle("菜单");
        // 设置居中显示
        jf.setLocationRelativeTo(null);
        // 设置退出进程
        jf.setDefaultCloseOperation(3);

        // 流式布局管理器
        FlowLayout flow = new FlowLayout(FlowLayout.LEADING,50,50);

        // 设置窗体布局
        jf.setLayout(flow);
        Font f=new Font("宋体",Font.PLAIN,20);
        JButton jbu = new javax.swing.JButton("公共聊天室");
        jbu.setPreferredSize(new Dimension(300,100));
        jbu.setFont(f);
        jbu.addActionListener(action);
        jf.add(jbu);

        JButton jbu2 = new javax.swing.JButton("好友列表");
        jbu2.setPreferredSize(new Dimension(300,100));
        jbu2.setFont(f);
        jbu2.addActionListener(action);
        jf.add(jbu2);

        JButton jbu3 = new javax.swing.JButton("退出");
        jbu3.setPreferredSize(new Dimension(300,100));
        jbu3.setFont(f);
        jbu3.addActionListener(action);
        jf.add(jbu3);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    //LinkedList<String> idlist,, HashMap<String,String> namelist

    public void publicChat(LinkedList<String>idlist){
        JFrame jf = new JFrame();
        // 像素>分辨率
        jf.setSize(650, 1000);
        jf.setTitle("聊天室");
        // 设置居中显示
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);

        FlowLayout flow = new FlowLayout(FlowLayout.CENTER,10,10);
        jf.setLayout(flow);
        Font f=new Font("宋体",Font.PLAIN,22);

        JTextArea jta=new JTextArea("聊天内容",25,35);
        jta.setLineWrap(true);    //设置文本域中的文本为自动换行
        jta.setForeground(Color.BLACK);    //设置组件的背景色
        jta.setFont(new Font("楷体",Font.PLAIN,20));    //修改字体样式
        jta.setBackground(Color.WHITE);    //设置按钮背景色
        jf.add(jta);

        int len = idlist.size();
        String[] sbf = new String[len];
        for (int i= 0;i<len;i++) {
            sbf[i] = idlist.get(i);
        }

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(200,600));
        JList<String> list = new JList(sbf);
        list.setFont(new Font("楷体",Font.PLAIN,20));
        scrollPane.setViewportView(list);
        jf.add(scrollPane);


        JButton jbu = new javax.swing.JButton("发送消息");
        jbu.setPreferredSize(new Dimension(150,50));
        jbu.setFont(f);
        jbu.addActionListener(action);
        jf.add(jbu);

        JButton jbu2 = new javax.swing.JButton("发送图片");
        jbu2.setPreferredSize(new Dimension(150,50));
        jbu2.setFont(f);
        jbu2.addActionListener(action);
        jf.add(jbu2);

        JButton jbu3 = new javax.swing.JButton("清屏");
        jbu3.setPreferredSize(new Dimension(150,50));
        jbu3.setFont(f);
        jbu3.addActionListener(action);
        jf.add(jbu3);


        JTextArea jta2=new JTextArea(10,50);
        JTextListener jtl = new JTextListener(jta2,"请输入内容");
        jta2.setLineWrap(true);    //设置文本域中的文本为自动换行
        jta2.setForeground(Color.BLACK);    //设置组件的背景色
        jta2.setFont(new Font("楷体",Font.PLAIN,20));    //修改字体样式
        jta2.setBackground(Color.WHITE);    //设置按钮背景色
        jta2.addFocusListener(jtl);
        jf.add(jta2);

        // 设置退出进程
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setVisible(true);

    };
    public void friendlist(){};
    public void loginFail(){JOptionPane.showMessageDialog(null, "登录失败!");}
    public void loginOK(){JOptionPane.showMessageDialog(null, "登录成功!");}
    public static void main(String[] args) {
        Login lo = new Login();
        lo.showUI();
        //lo.menu();
        LinkedList<String> list = new LinkedList<>();
        list.add("1");
        list.add("133");
        list.add("1222");
        //lo.publicChat(list);

    }

    public JTextField getRegisterid() {
        return registerid;
    }

    public JTextField getRegistername() {
        return registername;
    }

    public JTextField getRegisterpassword() {
        return registerpassword;
    }

    public JTextField getLogin_id() { return login_id; }

    public JTextField getLogin_password() { return login_password;}

}
