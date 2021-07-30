package com.lifeplus.lifeplus.model.form;

import javax.validation.constraints.NotNull;

public class NewPasswordForm {

    @NotNull
    private String token;

    @NotNull
    private String mail;

    @NotNull
    private String password;

    public NewPasswordForm() {
    }

    public NewPasswordForm(@NotNull String token, @NotNull String mail, @NotNull String password) {
        this.token = token;
        this.mail = mail;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "mail: " + this.mail + "\n"
                + "pass: " + this.password + "\n"
                + "token: " + this.token;
    }
}
