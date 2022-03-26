package top.levygo.frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Set;

/**
 * @description：注册页面，由登录界面的按钮绑定跳转
 * @author：LevyXie
 * @create：2022-03-26 10:27
 */
public class RegisterFrame extends JFrame{
    //单例模式
    public static RegisterFrame instance = new RegisterFrame();
    //初始化面板组件
    public JLabel lUsername = new JLabel("账号");
    public JLabel lPassword = new JLabel("密码");
    public JTextField tUsername = new JTextField();
    public JTextField tPassword = new JTextField();
    public JButton bRegister = new JButton("注册");

    private Properties addUserInfo;
    private Properties existUserInfo;

    public RegisterFrame() {
        //页面组件挂载
        this.setTitle("CHAT聊天室:注册界面");
        this.setSize(500, 300);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setLayout(null);
        lUsername.setBounds(100, 50, 150, 30);
        lPassword.setBounds(100, 100, 150, 30);
        tUsername.setBounds(180, 50, 200, 30);
        tPassword.setBounds(180, 100, 200, 30);
        bRegister.setBounds(230, 180, 80, 30);
        this.add(lUsername);
        this.add(lPassword);
        this.add(tUsername);
        this.add(tPassword);
        this.add(bRegister);

        //添加单击事件
        bRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tUsername.getText();
                String password = tPassword.getText();
                //判断用户名或密码是否输入为空
                if(username.equals("") || password.equals("")){
                    JOptionPane.showMessageDialog(instance, "用户名或密码不能为空，请重新注册！");
                    return;
                }
                try {
                    //判断用户名是否已存在
                    existUserInfo = new Properties();
                    FileInputStream fis = new FileInputStream("src/main/resources/userInfo.properties");
                    existUserInfo.load(fis);
                    Set<String> users = existUserInfo.stringPropertyNames();
                    for (String user : users) {
                        if(user.equals(username)){
                            JOptionPane.showMessageDialog(instance, "用户名已存在，请重新注册！");
                            tUsername.setText("");
                            tPassword.setText("");
                            return;
                        }
                    }

                    //添加用户
                    addUserInfo = new Properties();
                    FileOutputStream fos = new FileOutputStream("src/main/resources/userInfo.properties",true);
                    addUserInfo.setProperty(username,password);
                    addUserInfo.store(fos,null);
                    JOptionPane.showMessageDialog(instance, "注册成功，即将前往登录页面~");

                    //打开登录页面
                    LoginFrame.instance.setVisible(true);
                    LoginFrame.instance.tUsername.setText(username);
                    LoginFrame.instance.tPassword.setText(password);

                    //重置注册页面
                    RegisterFrame.instance.tUsername.setText("");
                    RegisterFrame.instance.tPassword.setText("");
                    RegisterFrame.instance.dispose();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
