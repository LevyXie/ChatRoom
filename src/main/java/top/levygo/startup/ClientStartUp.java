package top.levygo.startup;

import com.alee.laf.WebLookAndFeel;
import top.levygo.frame.LoginFrame;

import javax.swing.*;

/**
 * @description：客户端启动类
 * @author：LevyXie
 * @create：2022-03-26 10:24
 */
public class ClientStartUp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WebLookAndFeel.install();//加载lookAndFeel界面美化插件
                try {
                    LoginFrame.instance.setVisible(true);//显示登录界面
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
