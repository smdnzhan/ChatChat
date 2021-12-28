package WirChat.WirChatClient;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

public class JListListener implements ListSelectionListener {
    JList<Image> list;
    ArrayList<Image> imglist;
    Login login;

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public JList<Image> getList() {
        return list;
    }

    public ArrayList<Image> getImglist() {
        return imglist;
    }

    public void setImglist(ArrayList<Image> imglist) {
        this.imglist = imglist;
    }

    public void setList(JList<Image> list) {
        this.list = list;
    }

    public JListListener() {

    }
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()){
       int index = list.getSelectedIndex();
       JTextPane jta = login.getJta2();
       String s1 = jta.getText();
       jta.setText(s1+"##"+index+"##");
    }}
    }

