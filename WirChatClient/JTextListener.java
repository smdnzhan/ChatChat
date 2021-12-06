package WirChat.WirChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextListener implements FocusListener {
    private String hintText;          //提示文字
    private JTextArea textField;

    public JTextListener(JTextArea area,String hintText) {
        this.textField=area;
        this.hintText=hintText;
        textField.setText(hintText);   //默认直接显示
        textField.setForeground(Color.GRAY);
    }
    @Override
    public void focusGained(FocusEvent e) {
        //获取焦点时，清空提示内容
        String temp=textField.getText();
        if(temp.equals(hintText)){
            textField.setText("");
            textField.setForeground(Color.BLACK);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        //失去焦点时，没有输入内容，显示提示内容
        String temp=textField.getText();
        if(temp.equals("")) {
            textField.setForeground(Color.GRAY);
            textField.setText(hintText);
        }
    }
}
