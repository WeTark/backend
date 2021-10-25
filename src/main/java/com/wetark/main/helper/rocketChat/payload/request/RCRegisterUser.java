package com.wetark.main.helper.rocketChat.payload.request;

public class RCRegisterUser {
    private String username;
    private String email;
    private String pass;
    private String name;

    public RCRegisterUser(String username, String email, String pass, String name) {
        this.username = username;
        this.email = email;
        this.pass = pass;
        this.name = name;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
