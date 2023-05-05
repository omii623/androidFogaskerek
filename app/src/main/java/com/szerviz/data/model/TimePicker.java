package com.szerviz.data.model;

public class TimePicker {
    private String userEmail;
    private String date;
    private String time;

    public TimePicker() {
    }

    public TimePicker(String userEmail, String date, String time) {
        this.userEmail = userEmail;
        this.date = date;
        this.time = time;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
