package com.lifeplus.lifeplus.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RegistrationToken {

    @Id
    private Integer id;
    private String token;

    public RegistrationToken() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
