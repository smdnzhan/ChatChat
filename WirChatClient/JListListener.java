package WirChat.WirChatClient;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JListListener implements ListSelectionListener {
    JList<String> list;
    //JMouseAdapter ma;
    MyPopupMenu jpm;
    JListListener(JList<String> list) {
        this.list = list;
    }
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()){
            String s = list.getSelectedValue();
            System.out.println("选中的为："+list.getSelectedValue());
            System.out.println("完成一次选中");
        }
    }
    }

