package top.levygo.client;

import java.io.*;
import java.net.Socket;

/**
 * @description：客户端
 * @author：LevyXie
 * @create：2022-03-25 14:47
 */
public class ChatClient {
    public Socket socket;
    public BufferedReader br;
    public PrintWriter pw;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    public BufferedInputStream bis;


    public ChatClient() {
        try {
            socket = new Socket("127.0.0.1",8008);
            System.out.println("连接服务器成功");
            //输出流，用于输出文字和对象
            OutputStream ops = socket.getOutputStream();
            pw = new PrintWriter(ops);
            oos = new ObjectOutputStream(ops);
            //输入流，用于获取文字和对象
            InputStream is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            bis = new BufferedInputStream(is);
            ois = new ObjectInputStream(is);

        } catch (Exception e) {
            System.out.println("连接服务器失败");
        }

    }
}
