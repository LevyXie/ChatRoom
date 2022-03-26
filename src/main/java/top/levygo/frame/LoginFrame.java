package top.levygo.frame;

import top.levygo.pojo.Account;
import top.levygo.client.ChatClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;

/**
 * @description：登录界面
 * @author：LevyXie
 * @create：2022-03-25 15:01
 */
public class LoginFrame extends JFrame {
    //单例模式
    public static LoginFrame instance = new LoginFrame();
    //初始化面板组件
    public JLabel lUsername = new JLabel("账号");
    public JLabel lPassword = new JLabel("密码");
    public JTextField tUsername = new JTextField();
    public JPasswordField tPassword = new JPasswordField();
    public JButton bLogin = new JButton("登录");
    public JButton bRegister = new JButton("注册");

    public ChatClient client;


    public LoginFrame() {
        //页面组件挂载
        this.setTitle("CHAT聊天室:登录界面");
        this.setSize(500, 300);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setLayout(null);
        lUsername.setBounds(100, 50,150,30);
        lPassword.setBounds(100, 100,150,30);
        tUsername.setBounds(180, 50,200,30);
        tPassword.setBounds(180, 100,200,30);
        bLogin.setBounds(290, 180, 80, 30);
        bRegister.setBounds(150, 180, 80, 30);
        this.add(lUsername);
        this.add(lPassword);
        this.add(tUsername);
        this.add(tPassword);
        this.add(bLogin);
        this.add(bRegister);

        //按钮绑定单击事件
        bLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tUsername.getText();
                String password = tPassword.getText();
                if(username.equals("") || password.equals("")){
                    JOptionPane.showMessageDialog(LoginFrame.instance, "用户名或密码不能为空");
                }else{
                    Account account = new Account(username, password);
                    try {
                        client = new ChatClient();
                        client.oos.writeObject(account);
                        client.oos.flush();

                        //从服务器收到返还的判断用户名密码是否正确的flag
                        boolean flag = (boolean) client.ois.readObject();
                        if(flag){
                            //密码正确，打开会话窗体
                            new ClientFrame(username, client).setVisible(true);
                            //关闭登录框
                            LoginFrame.instance.dispose();
                        }else{
                            JOptionPane.showMessageDialog(LoginFrame.instance, "用户名或密码错误");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(LoginFrame.instance, "连接服务器失败，请稍后重试");
                    }
                }
            }
        });
        bRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterFrame.instance.setVisible(true);
                instance.dispose();
            }
        });
    }
}
