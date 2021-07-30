package com.lifeplus.lifeplus.model.form;

import javax.validation.constraints.NotNull;

public class PasswordForm {

    @NotNull
    private String password;

    public PasswordForm(String password) {
        this.password = password;
    }

    public PasswordForm() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
