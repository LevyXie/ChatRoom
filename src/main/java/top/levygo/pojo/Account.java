package top.levygo.pojo;

import java.io.Serializable;

/**
 * @description：账户类
 * @author：LevyXie
 * @create：2022-03-25 15:22
 */
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public Account() {
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return username;
    }
}
