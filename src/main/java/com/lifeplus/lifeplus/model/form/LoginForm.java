package com.lifeplus.lifeplus.model.form;

import javax.validation.constraints.NotNull;

/**
 * @author Manuel Pedrozo
 */
public class LoginForm {
    @NotNull
    private String username;

    @NotNull
    private String password;

    public LoginForm() {
    }

    public LoginForm(@NotNull String username, @NotNull String password) {
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
}
