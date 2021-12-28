package WirChat.WirChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class EmojiMenu extends JPopupMenu {
    ArrayList<Image> list;
    JList jList;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    boolean flag = false;


    public EmojiMenu(ArrayList<Image> list, JListListener jll){
        this.list = list;
        DefaultListModel<ImageIcon> model = new DefaultListModel<>();
        for (Image img:list) {
            ImageIcon imgg = new ImageIcon(img);
            model.addElement(imgg);
        }
        jList = new JList<ImageIcon>(model);
        jList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        jList.setVisibleRowCount(2);
        this.add(jList);
        jList.addListSelectionListener(jll);

    }

}
