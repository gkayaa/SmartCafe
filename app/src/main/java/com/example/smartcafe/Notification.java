package com.example.smartcafe;

import java.util.Date;

public class Notification
{
    private String content;
    private String notificationId;
    private String date;

    public Notification()
    {}
    public Notification(String notificationId,String content,String date)
    {
        this.notificationId = notificationId;
        this.content = content;
        this.date = date;
    }
    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


