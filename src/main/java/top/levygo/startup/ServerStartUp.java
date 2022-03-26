package top.levygo.startup;

import top.levygo.server.ChatServer;

/**
 * @description：服务器启动类
 * @author：LevyXie
 * @create：2022-03-26 10:04
 */
public class ServerStartUp {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.startServer();
    }
}
