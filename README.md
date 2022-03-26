# JavaSE练手项目：Socket在线聊天室

基于Java网络编程和JavaSwing实现的简单的Socket聊天室。

实现对Java网络编程、JavaIO流、多线程、JavaSwing图形化界面的综合复习。

## 主要实现功能

- Javaswing编写的简单图形界面。

- 实现用户的登录和注册功能。
- 多客户端模式下，通过服务器中转，实现客户与客户的单独通信。
- 增加"公屏"功能，实现单用户对全体用户的广播通信功能。
- 增加在线用户列表功能，即时跟踪并显示在线用户情况。

## 主要知识点

- Java- swing图形化界面
- Java- IO流
- Java- 多线程
- Java-socket网络编程

## 启动入口

-  启动startup包下的ServerStartUp类。

-  启动多个startup包下的ClientStartUp类。可登录或注册。当前properties内预设了user1~user5共5个用户，密码分别为pwd1~pwd5。（为了同一个类启动多次，采用idea作为IDE时，可Edit Configuration=>Modify options =>Allow multiple instances）

- 登录后，客户端可实现单独通信和广播通信。

  ![GIF 2022-3-26 22-19-25](https://myimageserver.oss-cn-beijing.aliyuncs.com/img/GIF%202022-3-26%2022-19-25.gif)

## 备注

- CRUD框架用多了，不妨返璞归真，复习JavaSE的基础知识。聊天室小项目涵盖IO流，多线程，网络通信等知识点，适合练手。
- 本项目为个人练手项目，参考了很多别的聊天室的实现代码。用了一个周末肝完，代码逻辑简单粗暴，难免有些bug，有空再优化。
- 尚未修复的bug:
  - 新注册用户无法在所有用户列表显示，仅当重启server时，会重新加载properties文件，才会显示。这个小bug留待修复。
  - 为了美化图形界面，采用了WebLookAndFeel插件，该插件偶尔会报空指针，此bug停用该插件即可修复。
- 继续优化的方向
  - 采用数据库存储用户信息，取代properties文件存储。
  - 增加图片、文件等传输形式。

