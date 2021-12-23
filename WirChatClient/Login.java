package WirChat.WirChatClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Login {


    private ClientListener action;
    private JTextField registername;
    private JTextField registerid;
    private JTextField registerpassword;
    private JTextField login_id;
    private JTextField login_password;
    private JTextArea jta;
    private JTextArea jta2;
    private JTextArea privateContent;
    private JTextArea privateMessage;
    private JList<String> list;
    private String privateTarget;
    ArrayList<Image> emojis;
    JButton emojibtt;
    EmojiMenu em;

    public MyMouseListener getMml() {
        return mml;
    }

    private MyMouseListener mml;



    public Login(){
        loadEmojis();

    }

    void loadEmojis()  {
        emojis = new ArrayList<>();
        for (int i = 0;i<9;i++){
            File file = new File("D:\\java\\JavaJAVA\\src\\WirChat\\Emoji\\"+"emoji"+i+".jpg");
            Image img = null;
            try {
                img = ImageIO.read(file);
                Image scaled = img.getScaledInstance(75,50,Image.SCALE_DEFAULT);
                emojis.add(scaled);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
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

    public void emojiChose(){}

    public void publicChat(String username, LinkedList<String>idlist){
        JFrame jf = new JFrame();

        // 像素>分辨率
        jf.setSize(650, 1000);
        jf.setTitle("聊天室 "+username);
        // 设置居中显示
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);

        FlowLayout flow = new FlowLayout(FlowLayout.CENTER,10,10);
        jf.setLayout(flow);
        Font f=new Font("宋体",Font.PLAIN,18);

        jta=new JTextArea(25,35);
        jta.setLineWrap(true);    //设置文本域中的文本为自动换行
        jta.setForeground(Color.BLACK);    //设置组件的背景色
        jta.setFont(new Font("楷体",Font.PLAIN,20));    //修改字体样式
        jta.setBackground(Color.WHITE);    //设置按钮背景色
        jta.setEditable(false);
        jf.add(jta);
        mml = new MyMouseListener(jta);
        jta.addMouseMotionListener(mml);
        jta.addMouseListener(mml);



        int len = idlist.size();
        String[] sbf = new String[len];
        for (int i= 0;i<len;i++) {
            sbf[i] = idlist.get(i);
        }

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(200,600));
        list = new JList(sbf);
        //ListSelectionListener lsl = new JListListener(list);
        MyPopupMenu mpm = new MyPopupMenu(action);
        MyMouseAdapter mma = new MyMouseAdapter(list,mpm.jpu);
        list.addMouseListener(mma);
        list.setFont(new Font("楷体",Font.PLAIN,20));
        scrollPane.setViewportView(list);
        jf.add(scrollPane);


        JButton jbu = new javax.swing.JButton("发送消息");
        jbu.setPreferredSize(new Dimension(120,50));
        jbu.setFont(f);
        jbu.addActionListener(action);
        jf.add(jbu);

        JButton jbu2 = new javax.swing.JButton("发送图片");
        jbu2.setPreferredSize(new Dimension(120,50));
        jbu2.setFont(f);
        jbu2.addActionListener(action);
        jf.add(jbu2);

        JButton jbu3 = new javax.swing.JButton("你画我猜");
        jbu3.setPreferredSize(new Dimension(120,50));
        jbu3.setFont(f);
        jbu3.addActionListener(action);
        jf.add(jbu3);

        emojibtt= new JButton("表情包");
        emojibtt.setPreferredSize(new Dimension(120,50));
        emojibtt.setFont(f);
        emojibtt.addActionListener(action);
        jf.add(emojibtt);

        JListListener jll = new JListListener();
        em = new EmojiMenu(emojis,emojibtt,jll);
        jll.setList(em.jList);
        jll.setLogin(this);
        jll.setImglist(emojis);

        jta2=new JTextArea(10,50);
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

        Graphics g = jta.getGraphics();
        mml.setG(g);

       ;
    }

    public void privateChat(String targetname){
        JFrame jf = new JFrame();
        privateTarget = targetname;
        // 像素>分辨率
        jf.setSize(650, 1000);
        jf.setTitle("正与:"+targetname+"聊天");
        // 设置居中显示
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);

        FlowLayout flow = new FlowLayout(FlowLayout.CENTER,10,10);
        jf.setLayout(flow);
        Font f=new Font("宋体",Font.PLAIN,22);

        privateContent=new JTextArea(25,50);
        privateContent.setLineWrap(true);    //设置文本域中的文本为自动换行
        privateContent.setForeground(Color.BLACK);    //设置组件的背景色
        privateContent.setFont(new Font("楷体",Font.PLAIN,20));    //修改字体样式
        privateContent.setBackground(Color.WHITE);    //设置按钮背景色
        privateContent.setEditable(false);
        jf.add(privateContent);


        JButton jbu = new javax.swing.JButton("发送私聊");
        jbu.setPreferredSize(new Dimension(150,50));
        jbu.setFont(f);
        jbu.addActionListener(action);
        jf.add(jbu);

        JButton jbu2 = new javax.swing.JButton("私聊图片");
        jbu2.setPreferredSize(new Dimension(150,50));
        jbu2.setFont(f);
        jbu2.addActionListener(action);
        jf.add(jbu2);

        JButton jbu3 = new javax.swing.JButton("视频私聊");
        jbu3.setPreferredSize(new Dimension(150,50));
        jbu3.setFont(f);
        jbu3.addActionListener(action);
        jf.add(jbu3);


        privateMessage=new JTextArea(10,50);
        JTextListener jtl = new JTextListener(jta2,"请输入内容");
        privateMessage.setLineWrap(true);    //设置文本域中的文本为自动换行
        privateMessage.setForeground(Color.BLACK);    //设置组件的背景色
        privateMessage.setFont(new Font("楷体",Font.PLAIN,20));    //修改字体样式
        privateMessage.setBackground(Color.WHITE);    //设置按钮背景色
        privateMessage.addFocusListener(jtl);
        jf.add(privateMessage);


        // 设置退出进程
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setVisible(true);
    }


    public void friendlist(){}
    public void loginFail(){JOptionPane.showMessageDialog(null, "登录失败!");}
    public void loginOK(){JOptionPane.showMessageDialog(null, "登录成功!");}
    public static void main(String[] args) {
        Login lo = new Login();
        //lo.privateChat("张三");
        lo.showUI();
        //LinkedList<String> list = new LinkedList<>();

        //lo.publicChat("我",list);

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

    public JTextArea getJta2() {
        return jta2;
    }

    public JTextArea getJta() { return jta; }

    public JTextArea getPrivateContent() {
        return privateContent;
    }

    public JTextArea getPrivateMessage() {
        return privateMessage;
    }

    public JList<String> getList() {
        return list;
    }

    public String getPrivateTarget(){return privateTarget;}
    public ClientListener getAction() {
        return action;
    }
}
