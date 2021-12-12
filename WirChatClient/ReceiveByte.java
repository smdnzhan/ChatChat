package WirChat.WirChatClient;

import java.io.InputStream;
import java.io.OutputStream;

public class ReceiveByte extends Thread{
    private InputStream is;
    private OutputStream os;

    ReceiveByte(InputStream is){
        this.is = is;
    }

    public int readInt() throws Exception{
        int i1 = is.read();
        System.out.println("i1:"+i1);
        int i2 = is.read()<<8;
        System.out.println("i2:"+i2);
        int i3 = is.read()<<16;
        System.out.println("i3:"+i3);
        int i4 = is.read()<<24;
        System.out.println("i4:"+i4);
        int res = i1 | i2 | i3 | i4;
        System.out.println("读到的整数为："+res);
        return res;
    }

    @Override
    public void run() {

        while (true){
            try {
                System.out.println("正在监听");
                int x1 = readInt();
                int y1 = readInt();
                int x2 = readInt();
                int y2 = readInt();
                System.out.println("x1:"+x1+"y1: "+y1);
                System.out.println("x2:"+x2+"y2:"+y2);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
