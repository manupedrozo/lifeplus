package com.lifeplus.lifeplus.model.form;

import com.lifeplus.lifeplus.model.User;

/**
 * @author Manuel Pedrozo
 */
public class UserEditForm {

    private String name;
    private String lastName;
    private String username;
    private String phone;
    private String email;

    public UserEditForm() {
    }

    public UserEditForm(String name, String lastName, String username, String phone, String email) {
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void patch(User entity) {
        entity.setName(this.name);
        entity.setLastName(this.lastName);
        entity.setUsername(this.username);
        entity.setEmail(this.email);
        entity.setPhone(this.phone);
    }
}
