package com.szerviz.data.model;

public class User {
    private String userName = "";
    private String email = "";
    private String password = "";
    private String phoneNumber = "";
    private String addres = "";

    public User() {
    }

    public User(String userName, String email, String password, String phoneNumber, String addres) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.addres = addres;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddres() {
        return addres;
    }

}
