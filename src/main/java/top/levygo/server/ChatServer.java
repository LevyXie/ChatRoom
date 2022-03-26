package top.levygo.server;

import top.levygo.pojo.Account;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * @description：服务器
 * @author：LevyXie
 * @create：2022-03-25 14:52
 */
public class ChatServer {

    //定义服务器socket
    private ServerSocket server;
    //定义线程安全的hashtable用于存储client和username的映射关系
    private Hashtable<String, Socket> clientConnection = new Hashtable<>();
    //判断是否建立对话线程
    private boolean exitMessageThread = false;

    //构造器
    public ChatServer() {
        try {
            //建立服务器socket
            server = new ServerSocket(8008);
            System.out.println("服务器成功启动，后台运行中~");

        } catch (Exception e) {
            System.out.println("服务器启动失败~");
        }
    }

    //对外暴露的执行方法
    public void startServer(){
        LoginHandler loginHandler = this.new LoginHandler();
        new Thread(loginHandler).start();
    }

    //更新在线用户的方法,供客户端登入和登出时调用，广播在线用户列表
    //flag为true时，为用户登入;flag为false时，为用户登出;
    private void updateOnlineUser(boolean flag, String username){
        try {
            //原计划采用ObjectOutputStream传递信息,但OOS在单个线程中存在锁机制,无法实现广播,采用简单粗暴的PW进行广播
            Collection<Socket> clients = clientConnection.values();
            Set<String> onlineUserList = clientConnection.keySet();
            for (Socket tClient : clients) {
                OutputStream ops = tClient.getOutputStream();
                PrintWriter pw = new PrintWriter(ops);
                if(flag){
                    pw.println("公共消息：" + username + "上线了！");
                }else {
                    pw.println("公共消息：" + username + "下线了！");
                }
                StringBuilder users = new StringBuilder();
                for (String user : onlineUserList) {
                    users.append(user);
                    users.append("%@%");//分隔符
                }
                pw.println(users);
                pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监测登录状态的线程，使登录客户端和服务主机建立连接
    class LoginHandler implements Runnable{
        Socket client;
        @Override
        public void run() {
            while (true){
                try {
                    //从主机接收连接，建立客户端
                    client = server.accept();
                    //定义输入和输出流
                    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
                    ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                    //获取登录账号
                    Account account = (Account) ois.readObject();
                    //对比登录账号和properties文件中的登录信息，成功则将账号置于map中
                    String username = account.getUsername();

                    //读取配置文件中的用户信息
                    Properties userInfo = new Properties();
                    FileInputStream fis = new FileInputStream("src/main/resources/userInfo.properties");
                    userInfo.load(fis);
                    String existUser = userInfo.getProperty(username);
                    if(!(existUser == null) && existUser.equals(account.getPassword())) {
                        oos.writeObject(true);
                        oos.flush();

                        //将客户端和用户名置于map集合中
                        clientConnection.put(account.getUsername(), client);
                        //登录成功，启动连接线程
                        System.out.println(username + "已登录");
                        exitMessageThread = false;

                        MessageHandler messageHandler = new MessageHandler(client,username);
                        new Thread(messageHandler).start();

                        updateOnlineUser(true, username);

                    }else {
                        //用户名或密码错误
                        oos.writeObject(false);
                        oos.flush();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("客户端连接失败");
                }
            }
        }
    }

    //登录成功，建立对话的线程
    class MessageHandler implements Runnable{
        Socket sendClient;
        Socket receiveClient;
        String username;

        public MessageHandler(Socket sendClient, String username) {
            this.sendClient = sendClient;
            this.username = username;
        }

        @Override
        public void run() {
            while (!exitMessageThread){
                try {
                    //--------服务器接收并加工信息-------
                    InputStream sendIs = sendClient.getInputStream();
                    BufferedReader sendBr = new BufferedReader(new InputStreamReader(sendIs));
                    String rawMessage = sendBr.readLine();
                    //截取目标对话用户
                    String targetName = rawMessage.substring(0, rawMessage.indexOf(":"));
                    //截取发送的信息主体
                    String message = rawMessage.substring(rawMessage.indexOf(":") + 1);

                    //-----------服务器发送信息----------
                    //公屏情况下群发
                    if(targetName.equals("公屏")){
                        Collection<Socket> clients = clientConnection.values();
                        for (Socket client : clients) {
                            //除自身外，其他均发送信息
                            if(!client.equals(sendClient)){
                                OutputStream ops = client.getOutputStream();
                                PrintWriter pw = new PrintWriter(ops);
                                pw.println(message + "(公屏消息)");//增加消息可读性
                                pw.flush();
                            }
                        }
                    }
                    else{
                        //点对点发送
                        receiveClient = clientConnection.get(targetName);
                        OutputStream ops = receiveClient.getOutputStream();
                        PrintWriter pw = new PrintWriter(ops);
                        pw.println(message);
                        pw.flush();
                    }

                }catch (Exception e) {
                    System.out.println(username + "已退出");
                    clientConnection.remove(username);//删除退出的用户
                    updateOnlineUser(false, username);
                    exitMessageThread = true;//停止对话的线程
                }
            }
        }
    }
}
