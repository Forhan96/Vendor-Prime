package com.shadigipay.shaprimevendor;

public class User {
    private String username;
    private String password;
    private String userId;

    User(String username, String password, String userId) {
        this.username = username;
        this.password = password;
        this.userId = userId;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getUserId() {
        return userId;
    }
}
