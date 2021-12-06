package WirChat.WirChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyMouseAdapter extends MouseAdapter {
    JList<String> list;
    JPopupMenu jpm;

    MyMouseAdapter(JList<String> list, JPopupMenu jpm){
        this.list = list;
        this.jpm = jpm;
    }
    @Override
    public void mouseReleased (MouseEvent e){
        if (e.isPopupTrigger()) {
            showPopup(e);

        }

    }
    @Override
    public void mousePressed (MouseEvent e){

    }


    private void showPopup(MouseEvent e) {
        if (e.isPopupTrigger()&&list.getSelectedIndex()!=-1) {
            //获取选择项的值
            Object selected = list.getModel().getElementAt(list.getSelectedIndex());
            System.out.println("选择的："+selected);
            //在鼠标右键处生成一个菜单
            jpm.show(e.getComponent(),e.getX(), e.getY());
        }
    }
}
