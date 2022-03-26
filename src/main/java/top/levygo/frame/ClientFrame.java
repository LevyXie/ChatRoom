package top.levygo.frame;

import top.levygo.client.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * @description：会话界面
 * @author：LevyXie
 * @create：2022-03-25 14:46
 */
public class ClientFrame extends JFrame {
    //初始化面板组件
    public JTextArea talkArea = new JTextArea();
    public JTextArea allUser = new JTextArea();
    public JScrollPane talkPane = new JScrollPane(talkArea);
    public JScrollPane allUserPane = new JScrollPane(allUser);
    public JPanel south = new JPanel();
    public JTextField tMessage = new JTextField(35);
    public JButton bSend = new JButton("发送");
    public JComboBox<String> userBox = new JComboBox<>();

    public ChatClient client;

    //动态获取到的在线用户的存储数组
    public String[] onlineUsers;

    public ClientFrame(String senderName,ChatClient client) {
        this.client = client;

        try {
            //读取配置文件中所有用户,用于用于userBox显示
            Properties userInfo = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/userInfo.properties");
            userInfo.load(fis);
            Set<String> users = userInfo.stringPropertyNames();
            allUser.append("所有用户\n");
            for (String user : users) {
                if(!user.equals(senderName)){

                    allUser.append(user + System.lineSeparator());
                }else {
                    allUser.append(user + "(我)" +System.lineSeparator());
                }

            }
            fis.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "用户信息读取错误，请检查配置文件");
        }

        this.setTitle("CHAT聊天室:" + senderName);
        this.setSize(500, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        talkArea.setEditable(false);
        allUser.setEditable(false);
        userBox.addItem("占位符");//采用的WebLookAndFeel插件,需在构造器中初始化,否则空指针

        south.add(tMessage);
        south.add(bSend);
        allUser.setColumns(12);
        this.setLayout(new BorderLayout());
        this.add(userBox,BorderLayout.NORTH);
        this.add(talkPane,BorderLayout.CENTER);
        this.add(south,BorderLayout.SOUTH);
        this.add(allUserPane,BorderLayout.WEST);

        bSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(userBox.getSelectedIndex() == 0){
                    JOptionPane.showMessageDialog(ClientFrame.this, "请选择聊天对象");
                    return;
                }
                if(tMessage.getText().contains("%@%")){//防止用户输入分隔符
                    JOptionPane.showMessageDialog(ClientFrame.this,"您发送的信息包含非法字符，请重新输入");
                    return;
                }
                if(tMessage.getText().equals("")){
                    return;
                }
                String receiverName = (String) userBox.getSelectedItem();
                String sendMessage = receiverName + ":" + senderName + ":" + tMessage.getText();
                String myMessage = senderName + "(我):" +  tMessage.getText();
                if("公屏".equals(receiverName)){
                    myMessage += "(公屏消息)";
                }
                talkArea.append(myMessage + System.lineSeparator());

                client.pw.println(sendMessage);
                client.pw.flush();
                tMessage.setText("");
            }
        });

        //启动获取信息的线程
        GetMessage getMessage = new GetMessage(senderName,this.userBox);
        new Thread(getMessage).start();

    }
    class GetMessage implements Runnable{
        String senderName;
        JComboBox<String> userBox;

        public GetMessage(String senderName, JComboBox<String> userBox) {
            this.senderName = senderName;
            this.userBox = userBox;
        }

        @Override
        public void run() {
            while(true){
                try {
                    String message = client.br.readLine();
                    //因传输过来的message包含其他用户发送的信息，和客户端广播的在线用户列表，做一个简单粗暴的提取
                    if(message.contains("%@%")){
                        //客户端广播的在线用户列表的情况
                        userBox.removeAllItems();
                        userBox.addItem("在线用户列表：请选择聊天对象");
                        userBox.addItem("公屏");
                        onlineUsers = message.split("%@%");
                        for (String user : onlineUsers) {
                            if(!user.equals(senderName)){
                                userBox.addItem(user);
                            }
                        }
                    }else {
                        //其他用户发送过来的信息的情况
                        talkArea.append(message + System.lineSeparator());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ClientFrame.this, "服务器异常,将退出应用,请稍后重试。");
                    System.exit(1);
                }
            }
        }
    }
}
