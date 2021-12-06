package WirChat.WirChatClient;


import javax.swing.*;
import java.awt.*;


public class MyPopupMenu extends JPopupMenu {
    public JPopupMenu jpu;
    MyPopupMenu(){
        jpu = new JPopupMenu();
        // Create the popup menu items
        JMenuItem jmiAdd = new JMenuItem("添加好友");
        JMenuItem jmiPrivate = new JMenuItem("发起私聊");
        jmiAdd.setFont(new Font("楷体",Font.PLAIN,20));
        jmiPrivate.setFont(new Font("楷体",Font.PLAIN,20));
        // Add the menu items to the popup menu
        jpu.add(jmiAdd);
        jpu.addSeparator();
        jpu.add(jmiPrivate);

    }
    }
